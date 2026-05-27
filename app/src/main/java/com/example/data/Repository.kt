package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FluentoRepository(private val database: AppDatabase) {

    val userDao = database.userDao()
    val chatDao = database.chatDao()
    val lessonDao = database.lessonDao()
    val reviewDao = database.reviewDao()

    val userProfileFlow: Flow<UserProfile?> = userDao.getUserProfileFlow()
    val allLessonsFlow: Flow<List<DailyLesson>> = lessonDao.getAllLessonsFlow()
    val weakItemsFlow: Flow<List<WeakItem>> = reviewDao.getWeakItemsFlow()

    fun getChatMessagesFlow(sessionType: String): Flow<List<ChatMessage>> {
        return chatDao.getChatMessagesFlow(sessionType)
    }

    suspend fun getUserProfile(): UserProfile? {
        return userDao.getUserProfile()
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userDao.insertOrUpdateProfile(profile)
    }

    suspend fun addXpAndSpeakingTime(xpAmount: Int, speakingMins: Int) {
        userDao.addXp(xpAmount)
        userDao.addSpeakingMinutes(speakingMins)
    }

    suspend fun insertChatMessage(message: ChatMessage) {
        chatDao.insertMessage(message)
    }

    suspend fun clearChatHistory(sessionType: String) {
        chatDao.clearChatHistory(sessionType)
    }

    suspend fun setLessonCompleted(id: Int, isCompleted: Boolean) {
        lessonDao.setLessonCompleted(id, isCompleted)
    }

    suspend fun getLessonById(id: Int): DailyLesson? {
        return lessonDao.getLessonById(id)
    }

    suspend fun insertWeakItem(item: WeakItem) {
        reviewDao.insertWeakItem(item)
    }

    suspend fun deleteWeakItem(item: WeakItem) {
        reviewDao.deleteWeakItem(item)
    }

    suspend fun incrementReview(itemId: Long) {
        reviewDao.incrementReviewCount(itemId)
    }

    suspend fun updatePremiumStatus(isPremium: Boolean) {
        userDao.updatePremiumStatus(isPremium)
    }

    suspend fun prepopulateLessonsIfNeeded(targetLanguage: String) {
        val existing = allLessonsFlow.firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            val defaultLessons = listOf(
                DailyLesson(
                    day = 1,
                    title = "Introductions & Greetings",
                    description = "Learn how to initiate conversations and exchange names.",
                    category = "General",
                    difficulty = "Beginner",
                    vocabulary = "Hola, Buenos días, Me llamo, Mucho gusto, ¿Cómo estás?",
                    grammarTip = "Use 'me llamo' for your name and the verb 'ser' (soy) for your profession or identity.",
                    practicePrompt = "Introduce yourself with your name, native country, and why you are learning $targetLanguage.",
                    conversationStarter = "¡Hola! Bienvenidos a Fluento. I am your language coach. Let's start by exchanging names! What's yours?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 2,
                    title = "Ordering at a Local Café",
                    description = "Master the art of ordering drinks and foods in a coffee house.",
                    category = "Cafe",
                    difficulty = "Beginner",
                    vocabulary = "Café con leche, Por favor, La cuenta, Azúcar, Croissant",
                    grammarTip = "In $targetLanguage, use polite requests like 'quisiera' or 'me gustaría' to ask for orders.",
                    practicePrompt = "Order a hot latte and a slice of chocolate cake from the barista politely.",
                    conversationStarter = "¡Buenas tardes! Welcome to Café Central. What can I get started for you today?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 3,
                    title = "Navigating Airport Security",
                    description = "Pass through checkpoints, answer questions about bags and paperwork.",
                    category = "Airport",
                    difficulty = "Intermediate",
                    vocabulary = "Pasaporte, Equipaje de mano, Vuelo, Tarjeta de embarque, Declarar",
                    grammarTip = "Expect verbs in formal imperative or indirect command structures from customs officials.",
                    practicePrompt = "Explain to the customs officer that you have nothing to declare and are visiting for a 1-week holiday.",
                    conversationStarter = "Next passenger please. Place your carry-on bags flat. May I see your passport and flight coupon details?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 4,
                    title = "Checking Into the Hotel",
                    description = "Verify room reservations, ask about keys and breakfast timings.",
                    category = "Hotel",
                    difficulty = "Beginner",
                    vocabulary = "Habitación, Reserva, Llave, Camas, Desayuno incluido",
                    grammarTip = "Distinguish between 'hay' (there is/are) and 'tener' (to have) when checking hotel amenities.",
                    practicePrompt = "Ask the receptionist if breakfast is included and request a room key clone.",
                    conversationStarter = "Hola! Welcome to Hotel de la Vista. Under which name is the reservation registered?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 5,
                    title = "The Mock Business Interview",
                    description = "Present your key skills, previous projects, and salary goals.",
                    category = "Meeting",
                    difficulty = "Advanced",
                    vocabulary = "Puesto, Salario, Experiencia laboral, Currículum, Fortalezas",
                    grammarTip = "Use the present perfect tense to express work experiences up to the current date.",
                    practicePrompt = "Describe your top 2 strengths and why you're interested in joining this international team.",
                    conversationStarter = "Thank you for coming in for our interview today. To start, could you please summarize your past industry experience?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 6,
                    title = "The Romantic Dinner Date",
                    description = "Practice casual, warm icebreakers and express food preferences.",
                    category = "Dating",
                    difficulty = "Intermediate",
                    vocabulary = "Hermoso/a, Cenar, Brindis, Encantado/a, Reír",
                    grammarTip = "Use subjunctive or conditional structures gently to suggest shared actions ('¿Te gustaría...?').",
                    practicePrompt = "Compliment your date on their look and suggest ordering a bottle of red wine to share.",
                    conversationStarter = "You look incredible tonight! I am so glad we found time for this. Have you looked at the dinner menu yet?",
                    isCompleted = false,
                    isLocked = false
                ),
                DailyLesson(
                    day = 7,
                    title = "Boutique Style Shopping",
                    description = "Inquire about clothing sizes, try-on rooms, price matches, and payment modes.",
                    category = "Shopping",
                    difficulty = "Beginner",
                    vocabulary = "Precio, Ropa, Probador, Descuento, Tarjeta de crédito",
                    grammarTip = "Use demonstrative pronouns like 'este' (this one) or 'aquel' (that over there) to point out outfits.",
                    practicePrompt = "State that the white shirt is too large and ask if they have a medium size in the back.",
                    conversationStarter = "Hello! Let me know if you need any assistance. We're running a buy-one-get-one-half-off discount today!",
                    isCompleted = false,
                    isLocked = false
                )
            )
            lessonDao.insertLessons(defaultLessons)
        }
    }
}
