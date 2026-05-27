package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.service.CorrectionResponse
import com.example.service.GeminiService
import com.example.service.SystemPrompts
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FluentoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FluentoRepository
    
    val userProfile: StateFlow<UserProfile?>
    val allLessons: StateFlow<List<DailyLesson>>
    val weakItems: StateFlow<List<WeakItem>>
    
    val textMessages: StateFlow<List<ChatMessage>>
    val voiceMessages: StateFlow<List<ChatMessage>>

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _voiceFeedback = MutableStateFlow<String?>(null)
    val voiceFeedback: StateFlow<String?> = _voiceFeedback.asStateFlow()

    private val _currentLesson = MutableStateFlow<DailyLesson?>(null)
    val currentLesson: StateFlow<DailyLesson?> = _currentLesson.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FluentoRepository(database)
        
        userProfile = repository.userProfileFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        viewModelScope.launch {
            val existing = repository.getUserProfile()
            if (existing == null) {
                repository.saveUserProfile(UserProfile(completedOnboarding = false))
            }
        }

        allLessons = repository.allLessonsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        weakItems = repository.weakItemsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        textMessages = repository.getChatMessagesFlow("Text").stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        voiceMessages = repository.getChatMessagesFlow("Voice").stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun completeOnboarding(
        targetLanguage: String,
        nativeLanguage: String,
        proficiencyLevel: String,
        studyReason: String,
        learningStyle: String,
        dailyTime: String,
        tutorPersonality: String
    ) {
        viewModelScope.launch {
            val freshProfile = UserProfile(
                targetLanguage = targetLanguage,
                nativeLanguage = nativeLanguage,
                proficiencyLevel = proficiencyLevel,
                studyReason = studyReason,
                learningStyle = learningStyle,
                dailyTimeCommitment = dailyTime,
                tutorPersonality = tutorPersonality,
                completedOnboarding = true,
                isPremium = false,
                xp = 50, // Starting bonus
                streak = 1,
                speakingMinutes = 0,
                sessionsCompleted = 0
            )
            repository.saveUserProfile(freshProfile)
            repository.prepopulateLessonsIfNeeded(targetLanguage)
        }
    }

    fun selectLesson(lesson: DailyLesson) {
        _currentLesson.value = lesson
    }

    fun completeLesson(lesson: DailyLesson) {
        viewModelScope.launch {
            repository.setLessonCompleted(lesson.id, true)
            // Reward statistics
            repository.addXpAndSpeakingTime(35, 5) // +35 XP, +5 Speaking mins
            
            // Check if there's a current lesson selected and update it
            if (_currentLesson.value?.id == lesson.id) {
                _currentLesson.value = lesson.copy(isCompleted = true)
            }
        }
    }

    fun sendTextChat(text: String) {
        if (text.trim().isEmpty()) return
        
        viewModelScope.launch {
            _isProcessing.value = true
            val profile = repository.getUserProfile() ?: UserProfile()
            
            // 1. Get Grammar Check
            val correction = GeminiService.getCorrection(text, profile.targetLanguage)
            
            // Save USER message with grammar feedback
            val userMsg = ChatMessage(
                sessionType = "Text",
                sender = "USER",
                text = text,
                isCorrected = !correction.isCorrect,
                correctedText = if (!correction.isCorrect) correction.correctedText else null,
                explanation = if (!correction.isCorrect) correction.explanation else null,
                alternative1 = correction.alternative1,
                alternative2 = correction.alternative2
            )
            repository.insertChatMessage(userMsg)

            // 2. If user had syntax errors, save to spacing review system (WeakItem)
            if (!correction.isCorrect) {
                val weakItem = WeakItem(
                    originalSentence = text,
                    correctedSentence = correction.correctedText,
                    explanation = correction.explanation,
                    language = profile.targetLanguage,
                    category = "Grammar"
                )
                repository.insertWeakItem(weakItem)
            }

            // 3. Increment stats slightly for practicing
            repository.addXpAndSpeakingTime(5, 1)

            // 4. Build message context thread
            val historical = repository.chatDao.getChatMessages("Text")
            val chatThreadPrompt = buildString {
                append("You are ${profile.tutorPersonality} AI language assistant tutoring the user in ${profile.targetLanguage}. ")
                append("Your native personality style requested by the user is: '${profile.tutorPersonality}'. ")
                append("Reply directly in ${profile.targetLanguage} as their coach. Keep answers encouraging, crisp (2-3 sentences), ")
                append("and occasionally suggest corrections if they make mistakes. Try to guide them in conversational threads.\n\n")
                append("Conversation flow:\n")
                historical.takeLast(6).forEach {
                    append("${it.sender}: ${it.text}\n")
                }
                append("AI:")
            }

            // 5. Call Gemini to get AI Tutor Response
            val aiResponseText = GeminiService.generateContent(
                prompt = chatThreadPrompt,
                systemInstruction = "You are a professional language coach tutoring ${profile.targetLanguage}. Speak normally, dynamically and help the student."
            )

            val aiMsg = ChatMessage(
                sessionType = "Text",
                sender = "AI",
                text = aiResponseText
            )
            repository.insertChatMessage(aiMsg)
            _isProcessing.value = false
        }
    }

    fun sendVoiceChat(userVoiceTranscript: String) {
        if (userVoiceTranscript.trim().isEmpty()) return

        viewModelScope.launch {
            _isProcessing.value = true
            val profile = repository.getUserProfile() ?: UserProfile()

            // 1. Get real-time grammar checks
            val correction = GeminiService.getCorrection(userVoiceTranscript, profile.targetLanguage)

            // 2. Mock or call Gemini Pronunciation feedback
            val prPrompt = """
                ${SystemPrompts.PRONUNCIATION_FEEDBACK}
                Learner Text: "$userVoiceTranscript"
                Target Language: ${profile.targetLanguage}
                User native: ${profile.nativeLanguage}
            """.trimIndent()
            
            val pronFeedbackResult = GeminiService.generateContent(prPrompt)
            _voiceFeedback.value = pronFeedbackResult

            // Save user message with speech correction & pronunciation notes
            val userMsg = ChatMessage(
                sessionType = "Voice",
                sender = "USER",
                text = userVoiceTranscript,
                isCorrected = !correction.isCorrect,
                correctedText = if (!correction.isCorrect) correction.correctedText else null,
                explanation = if (!correction.isCorrect) correction.explanation else null,
                pronunciationFeedback = pronFeedbackResult
            )
            repository.insertChatMessage(userMsg)

            if (!correction.isCorrect) {
                repository.insertWeakItem(
                    WeakItem(
                        originalSentence = userVoiceTranscript,
                        correctedSentence = correction.correctedText,
                        explanation = correction.explanation,
                        language = profile.targetLanguage,
                        category = "Pronunciation"
                    )
                )
            }

            // Add stats
            repository.addXpAndSpeakingTime(10, 2) // Speech calls give double stats

            // 3. Generate voice reply from AI Assistant
            val voiceThreadPrompt = buildString {
                append("You are an oral language tutor helping the user speak in ${profile.targetLanguage}. ")
                append("Your persona is ${profile.tutorPersonality}. Keep your voice reply natural, conversational, friendly and very short (1-2 sentences) ")
                append("as if calling on a phone. Answer in ${profile.targetLanguage} directly.\n\n")
                append("User stated: $userVoiceTranscript\nAI:")
            }

            val aiResponseText = GeminiService.generateContent(voiceThreadPrompt)
            val aiMsg = ChatMessage(
                sessionType = "Voice",
                sender = "AI",
                text = aiResponseText
            )
            repository.insertChatMessage(aiMsg)
            _isProcessing.value = false
        }
    }

    fun clearHistory(type: String) {
        viewModelScope.launch {
            repository.clearChatHistory(type)
        }
    }

    fun practiceReviewItem(item: WeakItem) {
        viewModelScope.launch {
            repository.deleteWeakItem(item)
            repository.addXpAndSpeakingTime(15, 0) // Reward XP for fixing mistakes
        }
    }

    fun togglePremium(isPremium: Boolean) {
        viewModelScope.launch {
            repository.updatePremiumStatus(isPremium)
        }
    }
}
