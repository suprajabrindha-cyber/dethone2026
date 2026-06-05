package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. Entities
// ==========================================

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val isExpense: Boolean, // true = expense, false = income
    val category: String,   // e.g., "Dining", "Shopping", "Salary", "Rent"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "financial_goals")
data class FinancialGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val category: String, // e.g., "House", "Car", "Education", "Vacation", "Retirement"
    val targetDate: String
)

@Entity(tableName = "scam_reports")
data class ScamReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val upiId: String,
    val riskScore: Int,
    val warningMessage: String,
    val isBlacklisted: Boolean = true,
    val reportCount: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "finance_scores")
data class FinanceScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val overallScore: Int,
    val savingsScore: Int,
    val spendingScore: Int,
    val debtScore: Int,
    val investmentScore: Int,
    val aiInsight: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "simulations")
data class FinancialSimulation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioName: String,
    val predictionDetails: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// ==========================================
// 2. Data Access Object (DAO)
// ==========================================

@Dao
interface FinanceDao {
    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    // Goals
    @Query("SELECT * FROM financial_goals")
    fun getAllGoals(): Flow<List<FinancialGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: FinancialGoal)

    @Query("DELETE FROM financial_goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)

    // Scam Shield Reports
    @Query("SELECT * FROM scam_reports ORDER BY timestamp DESC")
    fun getAllScamReports(): Flow<List<ScamReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScamReport(report: ScamReport)

    @Query("SELECT * FROM scam_reports WHERE upiId = :upiId LIMIT 1")
    suspend fun getReportByUpi(upiId: String): ScamReport?

    // Financial Scores
    @Query("SELECT * FROM finance_scores ORDER BY timestamp DESC LIMIT 1")
    fun getLatestScore(): Flow<FinanceScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: FinanceScore)

    // Simulations
    @Query("SELECT * FROM simulations ORDER BY timestamp DESC")
    fun getAllSimulations(): Flow<List<FinancialSimulation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulation(simulation: FinancialSimulation)

    // Conversations Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatHistory(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()
}

// ==========================================
// 3. Database
// ==========================================

@Database(
    entities = [
        Transaction::class,
        FinancialGoal::class,
        ScamReport::class,
        FinanceScore::class,
        FinancialSimulation::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finguard_finance_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ==========================================
// 4. Repository
// ==========================================

class FinanceRepository(private val financeDao: FinanceDao) {
    val allTransactions: Flow<List<Transaction>> = financeDao.getAllTransactions()
    val allGoals: Flow<List<FinancialGoal>> = financeDao.getAllGoals()
    val allScamReports: Flow<List<ScamReport>> = financeDao.getAllScamReports()
    val latestScore: Flow<FinanceScore?> = financeDao.getLatestScore()
    val allSimulations: Flow<List<FinancialSimulation>> = financeDao.getAllSimulations()
    val chatMessages: Flow<List<ChatMessage>> = financeDao.getChatHistory()

    // Transaction utilities
    suspend fun addTransaction(transaction: Transaction) {
        financeDao.insertTransaction(transaction)
    }

    suspend fun removeTransaction(id: Long) {
        financeDao.deleteTransaction(id)
    }

    suspend fun clearAllTransactions() {
        financeDao.clearTransactions()
    }

    // Goal utilities
    suspend fun saveGoal(goal: FinancialGoal) {
        financeDao.insertGoal(goal)
    }

    suspend fun deleteGoal(id: Long) {
        financeDao.deleteGoal(id)
    }

    // Scam Shield utilities
    suspend fun registerScamReport(report: ScamReport) {
        financeDao.insertScamReport(report)
    }

    suspend fun searchUpiScam(upiId: String): ScamReport? {
        return financeDao.getReportByUpi(upiId)
    }

    // Score History
    suspend fun saveFinanceScore(score: FinanceScore) {
        financeDao.insertScore(score)
    }

    // Simulations
    suspend fun saveSimulation(simulation: FinancialSimulation) {
        financeDao.insertSimulation(simulation)
    }

    // Chat conversions
    suspend fun saveChatMessage(message: ChatMessage) {
        financeDao.insertChatMessage(message)
    }

    suspend fun wipeChat() {
        financeDao.clearChatHistory()
    }
}
