package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun TopNavBar(
  userProfile: UserProfile,
  unreadAlertsCount: Int,
  onMenuClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onRequestClaimClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation = 2.dp, shape = RoundedCornerShape(0.dp), spotColor = Slate300),
    color = White,
    tonalElevation = 0.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 12.dp, vertical = 7.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Sidebar hamburger & Notification Bell (Compact, non-overlapping)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        IconButton(
          onClick = onMenuClick,
          modifier = Modifier
            .size(38.dp)
            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
            .testTag("sidebar_toggle_button")
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Toggle Sidebar",
            tint = Slate900,
            modifier = Modifier.size(20.dp)
          )
        }

        // Notification Bell with Badge
        IconButton(
          onClick = onNotificationsClick,
          modifier = Modifier
            .size(38.dp)
            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
            .testTag("notification_bell_button")
        ) {
          BadgedBox(
            badge = {
              if (unreadAlertsCount > 0) {
                Badge(
                  containerColor = RedDanger,
                  contentColor = White
                ) {
                  Text(text = unreadAlertsCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = Slate800,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Top Corner: SHOWCASE IN HIGHLIGHT 'Prasanna kumar B k (Business Development Manager in GALAXY)'
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF0F172A),
        border = BorderStroke(1.2.dp, Color(0xFF10B981)),
        shadowElevation = 3.dp,
        modifier = Modifier
          .clickable { onNotificationsClick() }
          .testTag("top_corner_highlight_prasanna")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(Color(0xFF10B981)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "Galaxy Verified",
              tint = Color(0xFF0F172A),
              modifier = Modifier.size(15.dp)
            )
          }

          Column(
            horizontalAlignment = Alignment.Start
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "Prasanna kumar B k",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.sp,
                  color = White,
                  letterSpacing = 0.15.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Box(
                modifier = Modifier
                  .size(5.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF10B981))
              )
            }
            Text(
              text = "(Business Development Manager in GALAXY)",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = Color(0xFFA7F3D0)
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}
