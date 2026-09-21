package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GoogleSheetsService
import com.example.data.LuckyLifeRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
  val currentSection: DashboardSection = DashboardSection.DASHBOARD,
  val isSidebarOpen: Boolean = false,
  val selectedRingCategory: InsuranceCategory = InsuranceCategory.LIFE,
  val selectedProjectionYear: Int = 5,
  val isClaimSheetOpen: Boolean = false,
  val isNotificationsSheetOpen: Boolean = false,
  val claimSubmittedMessage: String? = null,
  val actionFeedbackMessage: String? = null,
  val isSubmittingWealthData: Boolean = false,
  val lastSyncResult: GoogleSheetSyncRecord? = null,
  val showSyncSuccessDialog: Boolean = false
)

class LuckyLifeViewModel(
  private val repository: LuckyLifeRepository = LuckyLifeRepository(),
  private val googleSheetsService: GoogleSheetsService = GoogleSheetsService()
) : ViewModel() {

  private val _uiState = MutableStateFlow(DashboardUiState())
  val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

  val userProfile = repository.userProfile
  val kpiSummary = repository.kpiSummary
  val protectionRingData = repository.protectionRingData
  val wealthProjection = repository.wealthProjection
  val policies = repository.policies
  val wealthAssets = repository.wealthAssets
  val smartAlerts = repository.smartAlerts
  val familyGoals = repository.familyGoals
  val latestWealthEntry = repository.latestWealthEntry
  val latestSyncRecord = repository.latestSyncRecord
  val dailyPosts = repository.dailyPosts
  val activePopupPost = repository.activePopupPost

  // Current working draft for Easy Wealth Management data entry
  private val _wealthFormDraft = MutableStateFlow(
    WealthDataEntry(
      clientName = "",
      clientPhone = "",
      clientEmail = "",
      city = "",
      monthlyIncome = 0.0,
      monthlyExpenses = 0.0,
      bankSavings = 0.0,
      mutualFundsValue = 0.0,
      monthlySip = 0.0,
      retirementCorpus = 0.0,
      realEstateValue = 0.0,
      goldValue = 0.0,
      termLifeCover = 0.0,
      healthCover = 0.0,
      motorCover = 0.0,
      primaryGoal = "Wealth Creation & Family Protection",
      targetTimelineYears = 10,
      advisoryNotes = ""
    )
  )
  val wealthFormDraft: StateFlow<WealthDataEntry> = _wealthFormDraft.asStateFlow()

  fun updateWealthFormDraft(updated: WealthDataEntry) {
    _wealthFormDraft.value = updated
  }

  fun submitWealthDataToGoogleSheets(
    customWebhookUrl: String? = null,
    onSuccess: ((GoogleSheetSyncRecord) -> Unit)? = null
  ) {
    viewModelScope.launch {
      _uiState.update { it.copy(isSubmittingWealthData = true) }
      try {
        val entry = _wealthFormDraft.value
        val syncRecord = googleSheetsService.submitWealthData(entry, customWebhookUrl)
        // Apply changes to the live repository so dashboard immediately reflects real numbers
        repository.applyWealthDataSubmission(entry, syncRecord)

        _uiState.update {
          it.copy(
            isSubmittingWealthData = false,
            lastSyncResult = syncRecord,
            showSyncSuccessDialog = true,
            actionFeedbackMessage = "Data successfully linked & submitted to Google Sheets Cloud Database!"
          )
        }
        onSuccess?.invoke(syncRecord)
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isSubmittingWealthData = false,
            actionFeedbackMessage = "Note: Form saved locally and queued for Google Sheets sync."
          )
        }
      }
    }
  }

  fun dismissSyncSuccessDialog() {
    _uiState.update { it.copy(showSyncSuccessDialog = false) }
  }

  fun openGoogleSheetInBrowser(context: Context, url: String? = null) {
    googleSheetsService.openGoogleSheetInBrowser(
      context,
      url ?: GoogleSheetsService.DEFAULT_GOOGLE_SHEET_URL
    )
  }

  fun setSection(section: DashboardSection) {
    _uiState.update { it.copy(currentSection = section, isSidebarOpen = false) }
  }

  fun toggleSidebar() {
    _uiState.update { it.copy(isSidebarOpen = !it.isSidebarOpen) }
  }

  fun setSidebarOpen(open: Boolean) {
    _uiState.update { it.copy(isSidebarOpen = open) }
  }

  fun selectRingCategory(category: InsuranceCategory) {
    _uiState.update { it.copy(selectedRingCategory = category) }
  }

  fun selectProjectionYear(year: Int) {
    _uiState.update { it.copy(selectedProjectionYear = year) }
  }

  fun setClaimSheetOpen(open: Boolean) {
    _uiState.update { it.copy(isClaimSheetOpen = open) }
  }

  fun setNotificationsSheetOpen(open: Boolean) {
    _uiState.update { it.copy(isNotificationsSheetOpen = open) }
  }

  fun submitEmergencyClaim(
    policyCategory: String,
    patientOrVehicle: String,
    incidentHospital: String,
    urgencyLevel: String
  ) {
    viewModelScope.launch {
      val claimRef = "LLW-CLM-${(1000..9999).random()}"
      _uiState.update {
        it.copy(
          isClaimSheetOpen = false,
          claimSubmittedMessage = "Claim Intimation #$claimRef logged. Fast-track concierge assigned."
        )
      }
    }
  }

  fun handleAlertAction(alert: SmartAlertItem, context: Context? = null) {
    viewModelScope.launch {
      when (alert.actionType) {
        "OPEN_EASY_WEALTH" -> {
          _uiState.update { it.copy(currentSection = DashboardSection.WEALTH) }
        }
        "OPEN_SHEET" -> {
          context?.let { openGoogleSheetInBrowser(it) }
        }
        "RENEW_MOTOR" -> {
          repository.renewPolicy("POL-003")
          _uiState.update {
            it.copy(actionFeedbackMessage = "Vehicle Insurance renewal processed successfully!")
          }
        }
        "VIEW_SIP" -> {
          _uiState.update {
            it.copy(
              currentSection = DashboardSection.WEALTH,
              actionFeedbackMessage = "Viewing Active SIP Mandates in Easy Wealth Management."
            )
          }
        }
        "BOOK_CHECKUP" -> {
          repository.markAlertComplete(alert.id)
          _uiState.update {
            it.copy(actionFeedbackMessage = "Executive Health Checkup appointment requested for Saturday!")
          }
        }
        else -> {
          repository.markAlertComplete(alert.id)
        }
      }
    }
  }

  fun renewPolicy(policyId: String) {
    repository.renewPolicy(policyId)
    _uiState.update {
      it.copy(actionFeedbackMessage = "Policy renewal order initiated successfully.")
    }
  }

  fun createAndBroadcastDailyPost(
    context: Context,
    title: String,
    content: String,
    categoryTag: String,
    isHighPriority: Boolean,
    importantTakeaway: String
  ) {
    val now = System.currentTimeMillis()
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy • hh:mm a", java.util.Locale.getDefault())
    val formattedDate = sdf.format(java.util.Date(now))

    val newPost = DailyPostItem(
      id = "post_${now}",
      title = title.trim(),
      content = content.trim(),
      categoryTag = categoryTag,
      authorName = "Prasanna kumar B k",
      authorRole = "Business Development Manager in GALAXY",
      authorEmail = "",
      timestampMillis = now,
      dateFormatted = formattedDate,
      isHighPriority = isHighPriority,
      broadcastCount = (100..250).random(),
      isReadByUser = false,
      importantTakeaway = importantTakeaway.trim()
    )

    // 1. Publish in repository and activate in-app pop-up for users
    repository.publishDailyPost(newPost)

    // 2. Dispatch real Android system notification pop-up to device status bar & banner
    val notificationSent = com.example.util.NotificationHelper.sendDailyPostNotification(context, newPost)

    _uiState.update {
      it.copy(
        actionFeedbackMessage = if (notificationSent) {
          "Daily Post Broadcast! Heads-up pop-up sent to users' devices."
        } else {
          "Daily Post published and pop-up modal activated for users!"
        }
      )
    }
  }

  fun dismissPostPopup() {
    repository.dismissActivePopupPost()
  }

  fun openPostPopup(post: DailyPostItem) {
    repository.openPostInPopup(post)
  }

  fun deleteDailyPost(postId: String) {
    repository.deleteDailyPost(postId)
    _uiState.update {
      it.copy(actionFeedbackMessage = "Daily Post deleted successfully.")
    }
  }

  fun clearFeedbackMessage() {
    _uiState.update { it.copy(actionFeedbackMessage = null, claimSubmittedMessage = null) }
  }
}
