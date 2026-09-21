package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.DashboardSection
import com.example.ui.components.*
import com.example.ui.theme.Slate50
import com.example.ui.views.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyLifeApp(
  viewModel: LuckyLifeViewModel = viewModel(),
  initialShowSplashScreen: Boolean = true,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val kpiSummary by viewModel.kpiSummary.collectAsStateWithLifecycle()
  val protectionRingData by viewModel.protectionRingData.collectAsStateWithLifecycle()
  val wealthProjection by viewModel.wealthProjection.collectAsStateWithLifecycle()
  val policies by viewModel.policies.collectAsStateWithLifecycle()
  val wealthAssets by viewModel.wealthAssets.collectAsStateWithLifecycle()
  val smartAlerts by viewModel.smartAlerts.collectAsStateWithLifecycle()
  val familyGoals by viewModel.familyGoals.collectAsStateWithLifecycle()
  val wealthFormDraft by viewModel.wealthFormDraft.collectAsStateWithLifecycle()
  val latestSyncRecord by viewModel.latestSyncRecord.collectAsStateWithLifecycle()
  val dailyPosts by viewModel.dailyPosts.collectAsStateWithLifecycle()
  val activePopupPost by viewModel.activePopupPost.collectAsStateWithLifecycle()

  var showSplashScreen by remember { mutableStateOf(initialShowSplashScreen) }

  val drawerState = rememberDrawerState(
    initialValue = if (uiState.isSidebarOpen) DrawerValue.Open else DrawerValue.Closed
  )
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }
  val context = LocalContext.current

  // Android 13+ Notification Permission Launcher for Phone Pop-Up Alerts
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { /* Handled gracefully */ }

  LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  // Sync drawerState with uiState
  LaunchedEffect(uiState.isSidebarOpen) {
    if (uiState.isSidebarOpen && drawerState.isClosed) {
      drawerState.open()
    } else if (!uiState.isSidebarOpen && drawerState.isOpen) {
      drawerState.close()
    }
  }

  LaunchedEffect(drawerState.isOpen) {
    if (drawerState.isOpen != uiState.isSidebarOpen) {
      viewModel.setSidebarOpen(drawerState.isOpen)
    }
  }

  // Handle feedback messages
  LaunchedEffect(uiState.actionFeedbackMessage, uiState.claimSubmittedMessage) {
    val msg = uiState.claimSubmittedMessage ?: uiState.actionFeedbackMessage
    if (msg != null) {
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      viewModel.clearFeedbackMessage()
    }
  }

  if (showSplashScreen) {
    GalaxyCongratulationsSplashScreen(
      onTimeout = { showSplashScreen = false },
      modifier = modifier
    )
  } else {
    ModalNavigationDrawer(
      drawerState = drawerState,
      drawerContent = {
        ModalDrawerSheet(
          modifier = Modifier.widthIn(max = 320.dp),
          drawerContainerColor = MaterialTheme.colorScheme.surface
        ) {
          SidebarContent(
            currentSection = uiState.currentSection,
            userProfile = userProfile,
            onSelectSection = { section ->
              viewModel.setSection(section)
              scope.launch { drawerState.close() }
            },
            onCloseSidebar = {
              scope.launch { drawerState.close() }
            },
            onRequestClaim = {
              scope.launch { drawerState.close() }
              viewModel.setClaimSheetOpen(true)
            },
            onViewCongratulations = {
              showSplashScreen = true
            }
          )
        }
      }
    ) {
    Scaffold(
      modifier = modifier
        .fillMaxSize()
        .background(Slate50)
        .testTag("fintech_scaffold"),
      topBar = {
        TopNavBar(
          userProfile = userProfile,
          unreadAlertsCount = smartAlerts.count { !it.isDone },
          onMenuClick = {
            scope.launch {
              if (drawerState.isClosed) drawerState.open() else drawerState.close()
            }
          },
          onNotificationsClick = {
            viewModel.setNotificationsSheetOpen(true)
          }
        )
      },
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      },
      floatingActionButton = {
        ExtendedFloatingActionButton(
          onClick = { openWhatsAppDirect(context, "8123667686") },
          containerColor = Color(0xFF25D366),
          contentColor = Color.White,
          shape = RoundedCornerShape(24.dp),
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
          modifier = Modifier.testTag("floating_whatsapp_button"),
          icon = {
            Icon(
              painter = painterResource(id = R.drawable.ic_whatsapp),
              contentDescription = "WhatsApp",
              tint = Color.Unspecified,
              modifier = Modifier.size(24.dp)
            )
          },
          text = {
            Text(
              text = "Chat 8123667686",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(Slate50)
      ) {
        when (uiState.currentSection) {
          DashboardSection.DASHBOARD -> {
            DashboardMainView(
              userProfile = userProfile,
              kpiSummary = kpiSummary,
              protectionRingItems = protectionRingData,
              selectedRingCategory = uiState.selectedRingCategory,
              onSelectRingCategory = { cat -> viewModel.selectRingCategory(cat) },
              wealthProjectionPoints = wealthProjection,
              selectedProjectionYear = uiState.selectedProjectionYear,
              onSelectProjectionYear = { yr -> viewModel.selectProjectionYear(yr) },
              policies = policies,
              wealthAssets = wealthAssets,
              smartAlerts = smartAlerts,
              latestDailyPost = dailyPosts.firstOrNull(),
              onOpenPostPopup = { post -> viewModel.openPostPopup(post) },
              onNavigateToAdminPosts = { viewModel.setSection(DashboardSection.ADMIN_POSTS) },
              onRenewPolicy = { policyId -> viewModel.renewPolicy(policyId) },
              onAlertAction = { alert -> viewModel.handleAlertAction(alert, context) },
              onNavigateToProtection = { viewModel.setSection(DashboardSection.PROTECTION) },
              onNavigateToWealth = { viewModel.setSection(DashboardSection.WEALTH) },
              onOpenEmergencyDesk = { viewModel.setClaimSheetOpen(true) }
            )
          }
          DashboardSection.PROTECTION -> {
            ProtectionDeepDiveView(
              ringItems = protectionRingData,
              selectedCategory = uiState.selectedRingCategory,
              onSelectCategory = { cat -> viewModel.selectRingCategory(cat) },
              policies = policies,
              onRenewPolicy = { policyId -> viewModel.renewPolicy(policyId) },
              onRequestClaim = { viewModel.setClaimSheetOpen(true) },
              onBackToDashboard = { viewModel.setSection(DashboardSection.DASHBOARD) }
            )
          }
          DashboardSection.WEALTH -> {
            EasyWealthManagementView(
              formDraft = wealthFormDraft,
              onUpdateDraft = { viewModel.updateWealthFormDraft(it) },
              onSubmitToGoogleSheets = { customUrl ->
                viewModel.submitWealthDataToGoogleSheets(customUrl)
              },
              isSubmitting = uiState.isSubmittingWealthData,
              lastSyncRecord = latestSyncRecord,
              showSuccessDialog = uiState.showSyncSuccessDialog,
              onDismissSuccessDialog = { viewModel.dismissSyncSuccessDialog() },
              assets = wealthAssets,
              projectionPoints = wealthProjection,
              selectedYear = uiState.selectedProjectionYear,
              onSelectYear = { yr -> viewModel.selectProjectionYear(yr) },
              onBackToDashboard = { viewModel.setSection(DashboardSection.DASHBOARD) }
            )
          }
          DashboardSection.FAMILY_GOALS -> {
            FamilyGoalsView(
              goals = familyGoals,
              onBackToDashboard = { viewModel.setSection(DashboardSection.DASHBOARD) }
            )
          }
          DashboardSection.SUPPORT_CLAIMS -> {
            SupportClaimsView(
              userProfile = userProfile,
              onRequestClaim = { viewModel.setClaimSheetOpen(true) },
              onBackToDashboard = { viewModel.setSection(DashboardSection.DASHBOARD) }
            )
          }
          DashboardSection.ADMIN_POSTS -> {
            AdminDailyPostView(
              pastPosts = dailyPosts,
              onBroadcastPost = { ctx, t, c, cat, prio, take ->
                viewModel.createAndBroadcastDailyPost(ctx, t, c, cat, prio, take)
              },
              onPreviewPost = { post -> viewModel.openPostPopup(post) },
              onDeletePost = { postId -> viewModel.deleteDailyPost(postId) },
              onBackToDashboard = { viewModel.setSection(DashboardSection.DASHBOARD) }
            )
          }
        }
      }

      // Live Phone Pop-Up Dialog for Daily Posts (Appears on User's Screen)
      activePopupPost?.let { post ->
        DailyPostPopUpDialog(
          post = post,
          onDismiss = { viewModel.dismissPostPopup() },
          onWhatsAppClick = { ctx, text -> openWhatsAppDirect(ctx, "8123667686", text) }
        )
      }

      // Emergency Claim Modal Sheet
      if (uiState.isClaimSheetOpen) {
        EmergencyClaimSheet(
          userProfile = userProfile,
          onDismiss = { viewModel.setClaimSheetOpen(false) },
          onSubmitClaim = { cat, sub, det, urg ->
            viewModel.submitEmergencyClaim(cat, sub, det, urg)
          }
        )
      }

      // Actionable Notifications Modal Sheet
      if (uiState.isNotificationsSheetOpen) {
        NotificationsBottomSheet(
          alerts = smartAlerts,
          onDismiss = { viewModel.setNotificationsSheetOpen(false) },
          onAlertAction = { alert -> viewModel.handleAlertAction(alert, context) }
        )
      }
    }
  }
}
}
