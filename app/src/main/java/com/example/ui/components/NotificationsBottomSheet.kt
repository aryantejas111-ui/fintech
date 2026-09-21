package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
  alerts: List<SmartAlertItem>,
  onDismiss: () -> Unit,
  onAlertAction: (SmartAlertItem) -> Unit,
  modifier: Modifier = Modifier
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = White,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 10.dp, bottom = 6.dp)
          .width(40.dp)
          .height(4.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(Slate300)
      )
    },
    modifier = modifier.testTag("notifications_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
      // Header
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
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(BlueLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = BluePrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "Actionable Notifications",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.5.sp,
                color = Slate900
              )
            )
            Text(
              text = "Active Reminders, Renewals & SIPs",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Slate700
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(alerts) { alert ->
          val isDone = alert.isDone
          val isUrgent = alert.urgencyLabel.contains("15", ignoreCase = true)

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isDone) Slate200 else if (isUrgent) Color(0xFFFED7AA) else Slate200
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = alert.title,
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) Slate500 else Slate900,
                    fontSize = 15.sp
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
                    text = if (isDone) "Done" else alert.urgencyLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = when {
                        isDone -> EmeraldDark
                        isUrgent -> AmberWarning
                        else -> BlueDeep
                      }
                    ),
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(5.dp))

              Text(
                text = alert.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = if (isDone) Slate500 else Slate700,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                if (!isDone) {
                  Button(
                    onClick = { onAlertAction(alert) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                      containerColor = if (isUrgent) AmberWarning else BlueDeep,
                      contentColor = White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                  ) {
                    Text(
                      text = alert.actionText,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                      )
                    )
                  }
                } else {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = EmeraldPrimary,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = "Action Completed",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp
                      )
                    )
                  }
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}
