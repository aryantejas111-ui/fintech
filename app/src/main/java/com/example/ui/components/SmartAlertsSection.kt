package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SmartAlertItem
import com.example.ui.theme.*

@Composable
fun SmartAlertsSection(
  alerts: List<SmartAlertItem>,
  onAlertAction: (SmartAlertItem) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("smart_alerts_section_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Section Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(AmberLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = "Smart Alerts",
              tint = AmberWarning,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "Smart Alerts & Triggers",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 17.5.sp
              )
            )
            Text(
              text = "Actionable Tasks & Due Dates",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        val activeCount = alerts.count { !it.isDone }
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (activeCount > 0) RedLight else Slate100,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (activeCount > 0) Color(0xFFFECACA) else Slate200
          )
        ) {
          Text(
            text = "$activeCount Actions Required",
            style = MaterialTheme.typography.labelSmall.copy(
              color = if (activeCount > 0) RedDanger else Slate700,
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Minimalist Notification Feed
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        alerts.forEach { alert ->
          val isDone = alert.isDone
          val isUrgent = alert.urgencyLabel.contains("15", ignoreCase = true)

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isDone) Slate50.copy(alpha = 0.6f) else Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isDone) Slate200 else if (isUrgent) Color(0xFFFED7AA) else Slate200
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("alert_item_${alert.id.lowercase()}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                      if (isDone) EmeraldLight
                      else if (isUrgent) AmberLight
                      else BlueLight
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = when {
                      isDone -> Icons.Default.Check
                      alert.categoryTag.contains("Protection") -> Icons.Default.TimeToLeave
                      alert.categoryTag.contains("Wealth") -> Icons.Default.Payment
                      else -> Icons.Default.MedicalServices
                    },
                    contentDescription = null,
                    tint = when {
                      isDone -> EmeraldPrimary
                      isUrgent -> AmberWarning
                      else -> BluePrimary
                    },
                    modifier = Modifier.size(16.dp)
                  )
                }

                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = alert.title,
                      style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) Slate500 else Slate900,
                        fontSize = 14.5.sp
                      )
                    )

                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = when {
                        isDone -> EmeraldLight
                        isUrgent -> AmberLight
                        else -> BlueLight
                      }
                    ) {
                      Text(
                        text = if (isDone) "Completed" else alert.urgencyLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontSize = 10.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = when {
                            isDone -> EmeraldDark
                            isUrgent -> AmberWarning
                            else -> BlueDeep
                          }
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(3.dp))

                  Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      color = if (isDone) Slate500 else Slate700,
                      fontSize = 12.5.sp,
                      lineHeight = 17.sp,
                      fontWeight = FontWeight.Medium
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.width(10.dp))

              // Action Trigger Button
              if (!isDone) {
                Button(
                  onClick = { onAlertAction(alert) },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUrgent) AmberWarning else BlueDeep,
                    contentColor = White
                  ),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                  modifier = Modifier
                    .height(34.dp)
                    .testTag("alert_action_${alert.id.lowercase()}")
                ) {
                  Text(
                    text = alert.actionText,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.5.sp
                    )
                  )
                }
              } else {
                Surface(
                  shape = CircleShape,
                  color = EmeraldLight,
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = EmeraldPrimary,
                    modifier = Modifier.padding(4.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
