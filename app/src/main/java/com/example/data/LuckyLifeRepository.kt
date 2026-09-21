package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LuckyLifeRepository {

  // Initial User Profile: Clean slate for onboarding
  private val _userProfile = MutableStateFlow(
    UserProfile(
      name = "",
      email = "",
      clientCode = "",
      tier = "Awaiting Wealth Data",
      advisorName = "Prasanna kumar B k",
      advisorPhone = "+91 88927 22131",
      advisorWhatsApp = "+91 81236 67686",
      advisorTitle = "Business Development Manager in GALAXY",
      holisticScore = 0
    )
  )
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  // Initial KPIs: ALL ZEROS
  private val _kpiSummary = MutableStateFlow(
    KpiSummary(
      totalNetWorth = 0.0,
      netWorthGrowthYoy = 0.0,
      totalRiskCover = 0.0,
      lifeCoverAmount = 0.0,
      healthCoverAmount = 0.0,
      motorCoverAmount = 0.0,
      activeSipMonthly = 0.0,
      activeSipCount = 0,
      nextSipDate = "Pending Setup"
    )
  )
  val kpiSummary: StateFlow<KpiSummary> = _kpiSummary.asStateFlow()

  // Protection Ring: ALL ZEROS
  private val _protectionRingData = MutableStateFlow(
    listOf(
      ProtectionRingItem(
        category = InsuranceCategory.LIFE,
        title = "Term Life Cover",
        currentCover = 0.0,
        recommendedCover = 0.0,
        unitLabel = "₹ Cr",
        coveragePercent = 0,
        status = "0% (Awaiting Data)",
        recommendationText = "Enter your life coverage in Easy Wealth Management tab."
      ),
      ProtectionRingItem(
        category = InsuranceCategory.HEALTH,
        title = "Health & Critical Illness",
        currentCover = 0.0,
        recommendedCover = 0.0,
        unitLabel = "₹ Lakhs",
        coveragePercent = 0,
        status = "0% (Awaiting Data)",
        recommendationText = "Enter your health mediclaim in Easy Wealth Management tab."
      ),
      ProtectionRingItem(
        category = InsuranceCategory.MOTOR,
        title = "Vehicle & Asset Protection",
        currentCover = 0.0,
        recommendedCover = 0.0,
        unitLabel = "₹ Lakhs IDV",
        coveragePercent = 0,
        status = "0% (Awaiting Data)",
        recommendationText = "Enter your motor insurance in Easy Wealth Management tab."
      )
    )
  )
  val protectionRingData: StateFlow<List<ProtectionRingItem>> = _protectionRingData.asStateFlow()

  // Wealth Projection: ALL ZEROS
  private val _wealthProjection = MutableStateFlow(
    listOf(
      WealthProjectionPoint("Yr 1", 1, 0.0f, 0.0f, 0.0f),
      WealthProjectionPoint("Yr 2", 2, 0.0f, 0.0f, 0.0f),
      WealthProjectionPoint("Yr 3", 3, 0.0f, 0.0f, 0.0f),
      WealthProjectionPoint("Yr 4", 4, 0.0f, 0.0f, 0.0f),
      WealthProjectionPoint("Yr 5", 5, 0.0f, 0.0f, 0.0f)
    )
  )
  val wealthProjection: StateFlow<List<WealthProjectionPoint>> = _wealthProjection.asStateFlow()

  // Policies: EMPTY (ZERO)
  private val _policies = MutableStateFlow<List<PolicyItem>>(emptyList())
  val policies: StateFlow<List<PolicyItem>> = _policies.asStateFlow()

  // Wealth Assets: EMPTY (ZERO)
  private val _wealthAssets = MutableStateFlow<List<WealthAssetItem>>(emptyList())
  val wealthAssets: StateFlow<List<WealthAssetItem>> = _wealthAssets.asStateFlow()

  // Smart Alerts: Initial Setup Prompt
  private val _smartAlerts = MutableStateFlow(
    listOf(
      SmartAlertItem(
        id = "ALT-000",
        title = "Initialize Your Wealth Profile",
        description = "All figures are currently reset to ₹0. Enter your details in Easy Wealth Management to sync directly to Google Sheets.",
        categoryTag = "Setup Required",
        urgencyLabel = "Data Entry",
        actionText = "Open Easy Wealth",
        actionType = "OPEN_EASY_WEALTH",
        isDone = false
      )
    )
  )
  val smartAlerts: StateFlow<List<SmartAlertItem>> = _smartAlerts.asStateFlow()

  // Family Goals: EMPTY (ZERO)
  private val _familyGoals = MutableStateFlow<List<FamilyGoalItem>>(emptyList())
  val familyGoals: StateFlow<List<FamilyGoalItem>> = _familyGoals.asStateFlow()

  // Google Sheets submission tracking
  private val _latestWealthEntry = MutableStateFlow<WealthDataEntry?>(null)
  val latestWealthEntry: StateFlow<WealthDataEntry?> = _latestWealthEntry.asStateFlow()

  private val _latestSyncRecord = MutableStateFlow<GoogleSheetSyncRecord?>(null)
  val latestSyncRecord: StateFlow<GoogleSheetSyncRecord?> = _latestSyncRecord.asStateFlow()

  /**
   * Applies the user-entered data from the Easy Wealth Management tab into the live repository
   * and updates all dashboard calculations, rings, assets, policies, and projections.
   */
  fun applyWealthDataSubmission(entry: WealthDataEntry, syncRecord: GoogleSheetSyncRecord) {
    _latestWealthEntry.value = entry
    _latestSyncRecord.value = syncRecord

    val netWorth = entry.calculatedNetWorth
    val riskCover = entry.calculatedTotalRiskCover
    val clientDisplayName = entry.clientName

    // 1. Update Profile
    _userProfile.update {
      it.copy(
        name = clientDisplayName,
        email = entry.clientEmail,
        clientCode = if (entry.clientName.isNotBlank()) "FT-${(10000..99999).random()}" else "",
        tier = if (netWorth >= 5000000.0) "Privilege High Net Worth" else "Active Wealth Client",
        holisticScore = calculateHolisticScore(entry)
      )
    }

    // 2. Update KPI Summary with user's real data
    _kpiSummary.update {
      it.copy(
        totalNetWorth = netWorth,
        netWorthGrowthYoy = if (entry.monthlySip > 0) 14.8 else 8.5,
        totalRiskCover = riskCover,
        lifeCoverAmount = entry.termLifeCover,
        healthCoverAmount = entry.healthCover,
        motorCoverAmount = entry.motorCover,
        activeSipMonthly = entry.monthlySip,
        activeSipCount = if (entry.monthlySip > 0) ((entry.monthlySip / 5000.0).toInt().coerceIn(1, 8)) else 0,
        nextSipDate = if (entry.monthlySip > 0) "5th Next Month" else "None"
      )
    }

    // 3. Update Protection Ring Items
    val recommendedLifeCr = ((entry.monthlyIncome * 12 * 20) / 10000000.0).coerceAtLeast(1.0)
    val lifeCoverCr = entry.termLifeCover / 10000000.0
    val lifePercent = if (recommendedLifeCr > 0) ((lifeCoverCr / recommendedLifeCr) * 100).toInt().coerceIn(0, 100) else 0

    val recommendedHealthLakhs = 25.0
    val healthCoverLakhs = entry.healthCover / 100000.0
    val healthPercent = ((healthCoverLakhs / recommendedHealthLakhs) * 100).toInt().coerceIn(0, 100)

    val motorCoverLakhs = entry.motorCover / 100000.0
    val motorPercent = if (motorCoverLakhs > 0) 100 else 0

    _protectionRingData.value = listOf(
      ProtectionRingItem(
        category = InsuranceCategory.LIFE,
        title = "Term Life Cover",
        currentCover = lifeCoverCr,
        recommendedCover = recommendedLifeCr,
        unitLabel = "₹ Cr",
        coveragePercent = lifePercent,
        status = if (lifePercent >= 100) "Fully Protected" else if (lifePercent > 0) "$lifePercent% Covered" else "Not Covered",
        recommendationText = if (lifePercent >= 100) "Optimal protection aligned with 20x annual income." else "Target recommended cover of ₹${"%.2f".format(recommendedLifeCr)} Cr."
      ),
      ProtectionRingItem(
        category = InsuranceCategory.HEALTH,
        title = "Health & Mediclaim",
        currentCover = healthCoverLakhs,
        recommendedCover = recommendedHealthLakhs,
        unitLabel = "₹ Lakhs",
        coveragePercent = healthPercent,
        status = if (healthPercent >= 100) "Comprehensive" else if (healthPercent > 0) "$healthPercent% Covered" else "No Active Mediclaim",
        recommendationText = if (healthPercent >= 100) "Adequate health hedge against hospitalisation costs." else "Recommended ₹25 Lakhs super top-up cover."
      ),
      ProtectionRingItem(
        category = InsuranceCategory.MOTOR,
        title = "Vehicle & Asset Protection",
        currentCover = motorCoverLakhs,
        recommendedCover = motorCoverLakhs.coerceAtLeast(5.0),
        unitLabel = "₹ Lakhs IDV",
        coveragePercent = motorPercent,
        status = if (motorPercent > 0) "Active & Insured" else "Zero Vehicle Cover",
        recommendationText = "Zero depreciation asset cover active."
      )
    )

    // 4. Update 5-Year Compounding Projection based on starting wealth + monthly SIP
    val initialInvestedLakhs = (entry.mutualFundsValue / 100000.0).toFloat()
    val annualSipLakhs = ((entry.monthlySip * 12) / 100000.0).toFloat()
    val cagr = 13.5f

    val projPoints = mutableListOf<WealthProjectionPoint>()
    var runningInvested = initialInvestedLakhs
    var runningProjected = initialInvestedLakhs

    for (year in 1..5) {
      runningInvested += annualSipLakhs
      runningProjected = (runningProjected + annualSipLakhs) * (1f + (cagr / 100f))
      projPoints.add(
        WealthProjectionPoint(
          yearLabel = "Yr $year",
          yearNumber = year,
          investedLakhs = "%.1f".format(runningInvested).toFloat(),
          projectedLakhs = "%.1f".format(runningProjected).toFloat(),
          cagrRate = cagr
        )
      )
    }
    _wealthProjection.value = projPoints

    // 5. Update Policies list dynamically
    val newPolicies = mutableListOf<PolicyItem>()
    if (entry.termLifeCover > 0) {
      newPolicies.add(
        PolicyItem(
          id = "POL-LIFE",
          name = "Pure Term Life Protection Plan",
          provider = "Galaxy / LIC Life",
          category = InsuranceCategory.LIFE,
          policyNumber = "TL-${(1000..9999).random()}",
          sumAssuredText = "₹${"%.2f".format(lifeCoverCr)} Cr",
          premiumAmount = "₹${"%,d".format((entry.termLifeCover * 0.0012).toInt())}",
          premiumFrequency = "Annually",
          daysRemaining = 240,
          totalDaysInCycle = 365,
          renewalDate = "Nov 2027",
          isUrgent = false
        )
      )
    }
    if (entry.healthCover > 0) {
      newPolicies.add(
        PolicyItem(
          id = "POL-HLTH",
          name = "Galaxy Comprehensive Health Shield",
          provider = "Galaxy Health Insurance",
          category = InsuranceCategory.HEALTH,
          policyNumber = "GH-${(1000..9999).random()}",
          sumAssuredText = "₹${"%.1f".format(healthCoverLakhs)} Lakhs",
          premiumAmount = "₹${"%,d".format((entry.healthCover * 0.015).toInt())}",
          premiumFrequency = "Annually",
          daysRemaining = 120,
          totalDaysInCycle = 365,
          renewalDate = "Feb 2027",
          isUrgent = false
        )
      )
    }
    if (entry.motorCover > 0) {
      newPolicies.add(
        PolicyItem(
          id = "POL-MOTR",
          name = "Vehicle Comprehensive Zero-Dep",
          provider = "General Insurance Corp",
          category = InsuranceCategory.MOTOR,
          policyNumber = "VEH-${(1000..9999).random()}",
          sumAssuredText = "₹${"%.1f".format(motorCoverLakhs)} Lakhs IDV",
          premiumAmount = "₹12,500",
          premiumFrequency = "Annually",
          daysRemaining = 45,
          totalDaysInCycle = 365,
          renewalDate = "Oct 2026",
          isUrgent = false
        )
      )
    }
    _policies.value = newPolicies

    // 6. Update Wealth Assets list
    val newAssets = mutableListOf<WealthAssetItem>()
    if (entry.mutualFundsValue > 0) {
      newAssets.add(
        WealthAssetItem(
          id = "AST-MF",
          fundName = "Equity & Growth Mutual Funds Portfolio",
          assetClass = AssetClass.FLEXI_CAP,
          investedAmountText = "₹${"%.2f".format(entry.mutualFundsValue * 0.85 / 100000.0)} L",
          currentValueText = "₹${"%.2f".format(entry.mutualFundsValue / 100000.0)} L",
          dailyChangePercent = 1.45,
          isPositive = true,
          units = "Active Holdings",
          xirr = "+16.8% p.a."
        )
      )
    }
    if (entry.bankSavings > 0) {
      newAssets.add(
        WealthAssetItem(
          id = "AST-SAV",
          fundName = "Bank Savings & Liquid Emergency Fund",
          assetClass = AssetClass.LIQUID,
          investedAmountText = "₹${"%.2f".format(entry.bankSavings / 100000.0)} L",
          currentValueText = "₹${"%.2f".format(entry.bankSavings / 100000.0)} L",
          dailyChangePercent = 0.02,
          isPositive = true,
          units = "Instant Liquid",
          xirr = "+6.5% p.a."
        )
      )
    }
    if (entry.goldValue > 0) {
      newAssets.add(
        WealthAssetItem(
          id = "AST-GLD",
          fundName = "Gold & Sovereign Gold Bonds (SGB)",
          assetClass = AssetClass.EQUITY,
          investedAmountText = "₹${"%.2f".format(entry.goldValue * 0.9 / 100000.0)} L",
          currentValueText = "₹${"%.2f".format(entry.goldValue / 100000.0)} L",
          dailyChangePercent = 0.80,
          isPositive = true,
          units = "Precious Metal Hedge",
          xirr = "+12.2% p.a."
        )
      )
    }
    if (entry.realEstateValue > 0) {
      newAssets.add(
        WealthAssetItem(
          id = "AST-EST",
          fundName = "Real Estate Property Holdings",
          assetClass = AssetClass.MID_CAP,
          investedAmountText = "₹${"%.2f".format(entry.realEstateValue * 0.8 / 100000.0)} L",
          currentValueText = "₹${"%.2f".format(entry.realEstateValue / 100000.0)} L",
          dailyChangePercent = 0.00,
          isPositive = true,
          units = "Tangible Asset",
          xirr = "+10.0% p.a."
        )
      )
    }
    _wealthAssets.value = newAssets

    // 7. Update Family Goals
    _familyGoals.value = listOf(
      FamilyGoalItem(
        id = "GOL-001",
        title = entry.primaryGoal.ifBlank { "Long-term Wealth Freedom" },
        targetLakhs = ((entry.calculatedNetWorth * 2) / 100000.0).coerceAtLeast(50.0).toFloat(),
        currentLakhs = (entry.calculatedNetWorth / 100000.0).toFloat(),
        targetYear = 2026 + entry.targetTimelineYears,
        monthlyContribution = "₹${"%,d".format(entry.monthlySip.toInt())} / mo",
        category = "Primary Goal"
      )
    )

    // 8. Update Smart Alerts
    _smartAlerts.value = listOf(
      SmartAlertItem(
        id = "ALT-SYNC",
        title = "Google Sheets Backend Linked",
        description = "Live sync confirmed to Google Sheets (${syncRecord.syncId}). Real-time data activated.",
        categoryTag = "Google Sheets Sync",
        urgencyLabel = "Active Sync",
        actionText = "Open Sheet",
        actionType = "OPEN_SHEET",
        isDone = true
      )
    )
  }

  private fun calculateHolisticScore(entry: WealthDataEntry): Int {
    var score = 40
    if (entry.calculatedNetWorth > 0) score += 20
    if (entry.termLifeCover > 0) score += 15
    if (entry.healthCover > 0) score += 15
    if (entry.monthlySip > 0) score += 10
    return score.coerceIn(0, 100)
  }

  fun markAlertComplete(id: String) {
    _smartAlerts.update { list ->
      list.map { if (it.id == id) it.copy(isDone = true) else it }
    }
  }

  fun renewPolicy(policyId: String) {
    _policies.update { list ->
      list.map {
        if (it.id == policyId) {
          it.copy(daysRemaining = 365, isUrgent = false, renewalDate = "Sep 2027")
        } else it
      }
    }
  }

  // Daily Posts Broadcast Engine
  private val _dailyPosts = MutableStateFlow<List<DailyPostItem>>(emptyList())
  val dailyPosts: StateFlow<List<DailyPostItem>> = _dailyPosts.asStateFlow()

  // Currently active pop-up post for users (causes the pop-up modal to appear)
  private val _activePopupPost = MutableStateFlow<DailyPostItem?>(null)
  val activePopupPost: StateFlow<DailyPostItem?> = _activePopupPost.asStateFlow()

  fun publishDailyPost(post: DailyPostItem) {
    _dailyPosts.update { current ->
      listOf(post) + current.filter { it.id != post.id }
    }
    _activePopupPost.value = post
  }

  fun dismissActivePopupPost() {
    _activePopupPost.value = null
  }

  fun openPostInPopup(post: DailyPostItem) {
    _activePopupPost.value = post
  }

  fun deleteDailyPost(postId: String) {
    _dailyPosts.update { current ->
      current.filter { it.id != postId }
    }
    if (_activePopupPost.value?.id == postId) {
      _activePopupPost.value = null
    }
  }
}
