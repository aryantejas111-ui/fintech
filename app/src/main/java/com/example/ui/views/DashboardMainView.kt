package com.example.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardMainView(
  userProfile: UserProfile,
  kpiSummary: KpiSummary,
  protectionRingItems: List<ProtectionRingItem>,
  selectedRingCategory: InsuranceCategory,
  onSelectRingCategory: (InsuranceCategory) -> Unit,
  wealthProjectionPoints: List<WealthProjectionPoint>,
  selectedProjectionYear: Int,
  onSelectProjectionYear: (Int) -> Unit,
  policies: List<PolicyItem>,
  wealthAssets: List<WealthAssetItem>,
  smartAlerts: List<SmartAlertItem>,
  latestDailyPost: DailyPostItem? = null,
  onOpenPostPopup: (DailyPostItem) -> Unit = {},
  onNavigateToAdminPosts: () -> Unit = {},
  onRenewPolicy: (String) -> Unit,
  onAlertAction: (SmartAlertItem) -> Unit,
  onNavigateToProtection: () -> Unit,
  onNavigateToWealth: () -> Unit,
  onOpenEmergencyDesk: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .testTag("dashboard_main_scroll_list"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Welcome & Holistic Health Bar (Visualistic & Executive)
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            val welcomeGreeting = if (userProfile.name.isNotBlank()) "Welcome back, ${userProfile.name}" else "Financial Advisory Desk"
            Text(
              text = welcomeGreeting,
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Slate900,
                fontSize = 23.sp,
                letterSpacing = (-0.3).sp
              )
            )

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = Color(0xFFDCFCE7),
              border = BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16A34A))
                )
                Text(
                  text = "Active Portfolio",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF166534),
                    fontSize = 11.5.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Holistic Financial Health • Comprehensive Protection & Wealth Growth",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Slate700,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }
    }

    // Direct WhatsApp & Call Connection Section (Prominent Quick Connect)
    item {
      DirectContactSection(
        whatsAppNumber = "8123667686",
        callNumber = "8892722131"
      )
    }

    // TODAY'S DAILY POST HIGHLIGHT CARD (Pop-Up Trigger & Admin Studio Shortcut)
    if (latestDailyPost != null) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_post_dashboard_card")
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFF04160E),
                  border = BorderStroke(1.dp, Color(0xFF166534))
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                    )
                    Text(
                      text = "DAILY ADVISORY",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 10.5.sp,
                        color = Color(0xFFDCFCE7)
                      )
                    )
                  }
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFEFF6FF)
                ) {
                  Text(
                    text = latestDailyPost.categoryTag,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0284C7),
                      fontSize = 11.5.sp
                    ),
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                  )
                }
              }

              TextButton(
                onClick = onNavigateToAdminPosts,
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                modifier = Modifier.height(28.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.EditNote,
                  contentDescription = "Admin Studio",
                  modifier = Modifier.size(16.dp),
                  tint = Color(0xFF0284C7)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Admin Post Studio",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0284C7)
                )
              }
            }

            Text(
              text = latestDailyPost.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 16.5.sp,
                lineHeight = 22.sp
              )
            )

            Text(
              text = latestDailyPost.content,
              style = MaterialTheme.typography.bodySmall.copy(
                color = Slate800,
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
              ),
              maxLines = 2
            )

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${latestDailyPost.dateFormatted} • ${latestDailyPost.authorName}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              )

              Button(
                onClick = { onOpenPostPopup(latestDailyPost) },
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF0284C7),
                  contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .height(34.dp)
                  .testTag("open_daily_post_popup_button")
              ) {
                Icon(
                  imageVector = Icons.Default.OpenInFull,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                  text = "Read Pop-Up",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp
                  )
                )
              }
            }
          }
        }
      }
    }

    // 2. Top KPI Cards (Short & Crisp Data)
    item {
      KpiCardsSection(
        kpiSummary = kpiSummary,
        onViewWealthClick = onNavigateToWealth,
        onViewProtectionClick = onNavigateToProtection
      )
    }

    // 3. Flagship Centerpiece: 360° All-Round Protective Shield (Life, Health & Vehicle)
    item {
      AllRoundProtectionCard(
        onExploreProtection = onNavigateToProtection,
        onEmergencyDesk = onOpenEmergencyDesk
      )
    }

    // 4. The "Visualised Dashboard" Centerpiece (Charts)
    // Protection Ring Doughnut Chart
    item {
      ProtectionRingChart(
        items = protectionRingItems,
        selectedCategory = selectedRingCategory,
        onSelectCategory = onSelectRingCategory,
        onViewPoliciesClick = onNavigateToProtection
      )
    }

    // Wealth Tracker 5-Year Projection Line Chart
    item {
      WealthTrackerChart(
        projectionPoints = wealthProjectionPoints,
        selectedYear = selectedProjectionYear,
        onSelectYear = onSelectProjectionYear,
        onExploreFundsClick = onNavigateToWealth
      )
    }

    // 4. Policy & Asset Breakdown (Grid Layout)
    // Insurance Card
    item {
      PolicyBreakdownCard(
        policies = policies,
        onRenewPolicy = onRenewPolicy,
        onViewAllClick = onNavigateToProtection
      )
    }

    // Wealth Card
    item {
      WealthBreakdownCard(
        assets = wealthAssets,
        onAssetClick = {},
        onAddInvestmentClick = onNavigateToWealth
      )
    }

    // 5. Smart Alerts & Triggers Section
    item {
      SmartAlertsSection(
        alerts = smartAlerts,
        onAlertAction = onAlertAction
      )
    }

    // Footer Branding & Disclaimers
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      ) {
        Text(
          text = "Fintech Wealth Advisory Pvt. Ltd.",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Slate800,
            fontSize = 13.5.sp
          )
        )
        Text(
          text = "SEBI Registered Investment Advisor (INA00001928) & IRDAI Corporate Agent (CA-0821). Market investments are subject to risk. Policies governed by terms.",
          style = MaterialTheme.typography.labelSmall.copy(
            color = Slate600,
            fontSize = 11.5.sp,
            lineHeight = 16.sp
          )
        )
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
