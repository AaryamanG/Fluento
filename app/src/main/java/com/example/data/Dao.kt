package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET xp = xp + :amount WHERE id = 1")
    suspend fun addXp(amount: Int)

    @Query("UPDATE user_profile SET speakingMinutes = speakingMinutes + :amount WHERE id = 1")
    suspend fun addSpeakingMinutes(amount: Int)

    @Query("UPDATE user_profile SET streak = :newStreak WHERE id = 1")
    suspend fun updateStreak(newStreak: Int)

    @Query("UPDATE user_profile SET isPremium = :isPremium WHERE id = 1")
    suspend fun updatePremiumStatus(isPremium: Boolean)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE sessionType = :type ORDER BY timestamp ASC")
    fun getChatMessagesFlow(type: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE sessionType = :type ORDER BY timestamp ASC")
    suspend fun getChatMessages(type: String): List<ChatMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE sessionType = :type")
    suspend fun clearChatHistory(type: String)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY day ASC")
    fun getAllLessonsFlow(): Flow<List<DailyLesson>>

    @Query("SELECT * FROM lessons WHERE id = :id LIMIT 1")
    suspend fun getLessonById(id: Int): DailyLesson?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<DailyLesson>)

    @Query("UPDATE lessons SET isCompleted = :completed WHERE id = :lessonId")
    suspend fun setLessonCompleted(lessonId: Int, completed: Boolean)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM weak_items ORDER BY lastReviewed ASC")
    fun getWeakItemsFlow(): Flow<List<WeakItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeakItem(item: WeakItem)

    @Delete
    suspend fun deleteWeakItem(item: WeakItem)

    @Query("UPDATE weak_items SET reviewCount = reviewCount + 1, lastReviewed = :timestamp WHERE id = :itemId")
    suspend fun incrementReviewCount(itemId: Long, timestamp: Long = System.currentTimeMillis())
}
