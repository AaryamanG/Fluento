package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val targetLanguage: String = "Spanish",
    val nativeLanguage: String = "English",
    val proficiencyLevel: String = "Beginner (A1)",
    val studyReason: String = "Travel & Culture",
    val learningStyle: String = "Interactive Practice",
    val dailyTimeCommitment: String = "15 mins/day",
    val tutorPersonality: String = "Friendly & Motivating",
    val streak: Int = 3,
    val xp: Int = 120,
    val speakingMinutes: Int = 12,
    val sessionsCompleted: Int = 3,
    val isPremium: Boolean = false,
    val completedOnboarding: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionType: String = "General", // "Text" or "Voice"
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isCorrected: Boolean = false,
    val correctedText: String? = null,
    val explanation: String? = null,
    val alternative1: String? = null,
    val alternative2: String? = null,
    val pronunciationFeedback: String? = null
)

@Entity(tableName = "lessons")
data class DailyLesson(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day: Int,
    val title: String,
    val description: String,
    val category: String, // "Airport", "Cafe", "Grammar", "Hotel", "Meeting", "Shopping", "Dating"
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val vocabulary: String, // Comma separated words
    val grammarTip: String,
    val practicePrompt: String,
    val conversationStarter: String,
    val isCompleted: Boolean = false,
    val isLocked: Boolean = false
)

@Entity(tableName = "weak_items")
data class WeakItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalSentence: String,
    val correctedSentence: String,
    val explanation: String,
    val language: String,
    val category: String, // "Grammar", "Vocabulary", "Pronunciation"
    val reviewCount: Int = 0,
    val lastReviewed: Long = System.currentTimeMillis()
)
