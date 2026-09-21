package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DashboardSection
import com.example.model.UserProfile
import com.example.ui.theme.*

data class NavItem(
  val section: DashboardSection,
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val badgeText: String? = null
)

@Composable
fun SidebarContent(
  currentSection: DashboardSection,
  userProfile: UserProfile,
  onSelectSection: (DashboardSection) -> Unit,
  onCloseSidebar: () -> Unit,
  onRequestClaim: () -> Unit = {},
  onViewCongratulations: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val navItems = listOf(
    NavItem(
      section = DashboardSection.DASHBOARD,
      title = "Dashboard",
      subtitle = "Holistic Health Overview",
      icon = Icons.Default.Dashboard,
      badgeText = "Live"
    ),
    NavItem(
      section = DashboardSection.PROTECTION,
      title = "My Protection",
      subtitle = "Insurance & Risk Cover",
      icon = Icons.Default.Security,
      badgeText = "Shield"
    ),
    NavItem(
      section = DashboardSection.WEALTH,
      title = "Easy Wealth Management",
      subtitle = "Data Entry & Google Sheets",
      icon = Icons.Default.TableChart,
      badgeText = "Sheets Sync"
    ),
    NavItem(
      section = DashboardSection.FAMILY_GOALS,
      title = "Family Goals",
      subtitle = "Target & Milestones",
      icon = Icons.Default.FamilyRestroom,
      badgeText = "Milestones"
    ),
    NavItem(
      section = DashboardSection.ADMIN_POSTS,
      title = "Admin Daily Post Studio",
      subtitle = "Broadcast & Phone Pop-Up",
      icon = Icons.Default.Campaign,
      badgeText = "ADMIN"
    )
  )

  Surface(
    modifier = modifier
      .widthIn(max = 320.dp)
      .fillMaxHeight(),
    color = White,
    tonalElevation = 0.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Top Header inside Drawer
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BlueDeep),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "App Logo",
                tint = White,
                modifier = Modifier.size(24.dp)
              )
            }
            Column {
              Text(
                text = "Fintech",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = Slate900
                )
              )
              Text(
                text = if (userProfile.clientCode.isNotBlank()) "Client Portal • ${userProfile.clientCode}" else "Client Portal",
                style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
              )
            }
          }

          IconButton(
            onClick = onCloseSidebar,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Sidebar",
              tint = Slate600
            )
          }
        }

        // Holistic Financial Health Score Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Slate50),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Holistic Health Score",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = Slate700
                )
              )
              Surface(
                color = if (userProfile.holisticScore > 0) EmeraldLight else Slate100,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (userProfile.holisticScore > 0) EmeraldBorder else Slate300)
              ) {
                Text(
                  text = "${userProfile.holisticScore} / 100",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (userProfile.holisticScore > 0) EmeraldPrimary else Slate600
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { (userProfile.holisticScore / 100f).coerceIn(0f, 1f) },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = EmeraldPrimary,
              trackColor = Slate200,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (userProfile.holisticScore > 0) "Synchronized with Google Sheets portfolio." else "Pending wealth & protection data entry.",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Divider(color = Slate100)

        // Navigation Items
        Text(
          text = "MAIN NAVIGATION",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Slate600,
            fontSize = 11.5.sp,
            letterSpacing = 1.sp
          ),
          modifier = Modifier.padding(horizontal = 6.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          navItems.forEach { item ->
            val isSelected = currentSection == item.section
            val backgroundColor = if (isSelected) BlueLight else Color.Transparent
            val contentColor = if (isSelected) BlueDeep else Slate800
            val iconTint = if (isSelected) BluePrimary else Slate600
            val borderStroke = if (isSelected) {
              androidx.compose.foundation.BorderStroke(1.dp, BlueBorder)
            } else null

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = backgroundColor,
              border = borderStroke,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onSelectSection(item.section)
                }
                .testTag("nav_item_${item.section.name.lowercase()}")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(if (isSelected) White else Slate100),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = item.icon,
                      contentDescription = item.title,
                      tint = iconTint,
                      modifier = Modifier.size(20.dp)
                    )
                  }

                  Column {
                    Text(
                      text = item.title,
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = contentColor,
                        fontSize = 15.5.sp
                      )
                    )
                    Text(
                      text = item.subtitle,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSelected) BluePrimary else Slate600,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                      )
                    )
                  }
                }

                if (item.badgeText != null) {
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) White else Slate100,
                    border = androidx.compose.foundation.BorderStroke(
                      1.dp,
                      if (isSelected) BlueBorder else Slate200
                    )
                  ) {
                    Text(
                      text = item.badgeText,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) BluePrimary else Slate700,
                        fontSize = 11.sp
                      ),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Bottom Area: Dedicated Wealth Advisor Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Galaxy Health Insurance Congratulations Poster Re-open Banner
        // About & IRDAI Platform Overview
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF072116),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E5E3A)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
              onCloseSidebar()
              onViewCongratulations()
            }
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "IRDAI Certified",
              tint = Color(0xFF4ADE80),
              modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "IRDAI Certified Advisory",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFDCFCE7),
                  fontSize = 13.5.sp
                )
              )
              Text(
                text = "Tap to view Welcome & Platform overview",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color(0xFF86EFAC),
                  fontSize = 11.sp
                )
              )
            }
          }
        }

        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Slate50),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(EmeraldLight)
                  .border(1.dp, EmeraldBorder, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "PK",
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                  )
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = userProfile.advisorName,
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 14.5.sp
                  )
                )
                Text(
                  text = userProfile.advisorTitle,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate600,
                    fontSize = 11.5.sp
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { openWhatsAppDirect(context, "8123667686") },
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF25D366),
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(36.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_whatsapp),
                  contentDescription = "WhatsApp",
                  tint = Color.Unspecified,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "WhatsApp",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp
                  )
                )
              }

              Button(
                onClick = { makeDirectCall(context, "8892722131") },
                colors = ButtonDefaults.buttonColors(
                  containerColor = BlueDeep,
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(36.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Call,
                  contentDescription = "Call",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Call",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp
                  )
                )
              }
            }
          }
        }

        Text(
          text = "Fintech • v2.4 Enterprise",
          style = MaterialTheme.typography.labelSmall.copy(
            color = Slate600,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          ),
          modifier = Modifier.align(Alignment.CenterHorizontally)
        )
      }
    }
  }
}
