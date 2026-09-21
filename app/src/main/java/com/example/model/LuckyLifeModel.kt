package com.example.model

data class UserProfile(
  val name: String = "",
  val email: String = "",
  val clientCode: String = "",
  val tier: String = "Active Wealth Client",
  val advisorName: String = "Prasanna kumar B k",
  val advisorPhone: String = "+91 88927 22131",
  val advisorWhatsApp: String = "+91 81236 67686",
  val advisorTitle: String = "Business Development Manager in GALAXY",
  val holisticScore: Int = 0
)

data class KpiSummary(
  val totalNetWorth: Double = 0.0,
  val netWorthGrowthYoy: Double = 0.0,
  val totalRiskCover: Double = 0.0,
  val lifeCoverAmount: Double = 0.0,
  val healthCoverAmount: Double = 0.0,
  val motorCoverAmount: Double = 0.0,
  val activeSipMonthly: Double = 0.0,
  val activeSipCount: Int = 0,
  val nextSipDate: String = "Pending Setup"
)

data class WealthDataEntry(
  val clientName: String = "",
  val clientPhone: String = "",
  val clientEmail: String = "",
  val city: String = "",
  val monthlyIncome: Double = 0.0,
  val monthlyExpenses: Double = 0.0,
  val bankSavings: Double = 0.0,
  val mutualFundsValue: Double = 0.0,
  val monthlySip: Double = 0.0,
  val retirementCorpus: Double = 0.0,
  val realEstateValue: Double = 0.0,
  val goldValue: Double = 0.0,
  val termLifeCover: Double = 0.0,
  val healthCover: Double = 0.0,
  val motorCover: Double = 0.0,
  val primaryGoal: String = "Wealth Creation & Family Protection",
  val targetTimelineYears: Int = 10,
  val advisoryNotes: String = ""
) {
  val calculatedNetWorth: Double
    get() = bankSavings + mutualFundsValue + retirementCorpus + realEstateValue + goldValue

  val calculatedTotalRiskCover: Double
    get() = termLifeCover + healthCover + motorCover

  val monthlySavingsRatio: Double
    get() = if (monthlyIncome > 0) ((monthlyIncome - monthlyExpenses) / monthlyIncome * 100.0).coerceIn(0.0, 100.0) else 0.0
}

data class GoogleSheetSyncRecord(
  val syncId: String,
  val timestamp: String,
  val sheetOwnerEmail: String = "advisor@galaxy.com",
  val sheetUrl: String,
  val clientName: String,
  val totalNetWorth: Double,
  val totalRiskCover: Double,
  val isSuccess: Boolean,
  val statusMessage: String
)

enum class InsuranceCategory {
  LIFE, HEALTH, MOTOR
}

data class ProtectionRingItem(
  val category: InsuranceCategory,
  val title: String,
  val currentCover: Double,
  val recommendedCover: Double,
  val unitLabel: String,
  val coveragePercent: Int,
  val status: String,
  val recommendationText: String
)

data class WealthProjectionPoint(
  val yearLabel: String,
  val yearNumber: Int,
  val investedLakhs: Float,
  val projectedLakhs: Float,
  val cagrRate: Float = 13.5f
)

data class PolicyItem(
  val id: String,
  val name: String,
  val provider: String,
  val category: InsuranceCategory,
  val policyNumber: String,
  val sumAssuredText: String,
  val premiumAmount: String,
  val premiumFrequency: String,
  val daysRemaining: Int,
  val totalDaysInCycle: Int = 365,
  val renewalDate: String,
  val isUrgent: Boolean = false
) {
  val progressPercent: Float
    get() = (daysRemaining.toFloat() / totalDaysInCycle.toFloat()).coerceIn(0f, 1f)
}

enum class AssetClass {
  EQUITY, FLEXI_CAP, MID_CAP, LIQUID
}

data class WealthAssetItem(
  val id: String,
  val fundName: String,
  val assetClass: AssetClass,
  val investedAmountText: String,
  val currentValueText: String,
  val dailyChangePercent: Double,
  val isPositive: Boolean,
  val units: String,
  val xirr: String
)

data class SmartAlertItem(
  val id: String,
  val title: String,
  val description: String,
  val categoryTag: String,
  val urgencyLabel: String,
  val actionText: String,
  val actionType: String,
  val isDone: Boolean = false
)

data class FamilyGoalItem(
  val id: String,
  val title: String,
  val targetLakhs: Float,
  val currentLakhs: Float,
  val targetYear: Int,
  val monthlyContribution: String,
  val category: String
) {
  val completionPercent: Float
    get() = (currentLakhs / targetLakhs).coerceIn(0f, 1f)
}

data class DailyPostItem(
  val id: String,
  val title: String,
  val content: String,
  val categoryTag: String = "Market Pulse",
  val authorName: String = "Prasanna kumar B k",
  val authorRole: String = "Business Development Manager in GALAXY",
  val authorEmail: String = "",
  val timestampMillis: Long = System.currentTimeMillis(),
  val dateFormatted: String = "Today",
  val isHighPriority: Boolean = true,
  val broadcastCount: Int = 1,
  val isReadByUser: Boolean = false,
  val importantTakeaway: String = ""
)

enum class DashboardSection {
  DASHBOARD,
  PROTECTION,
  WEALTH,
  FAMILY_GOALS,
  SUPPORT_CLAIMS,
  ADMIN_POSTS
}
