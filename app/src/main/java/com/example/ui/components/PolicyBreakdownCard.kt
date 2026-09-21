package com.example.ui.components

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
import com.example.model.InsuranceCategory
import com.example.model.PolicyItem
import com.example.ui.theme.*

@Composable
fun PolicyBreakdownCard(
  policies: List<PolicyItem>,
  onRenewPolicy: (String) -> Unit,
  onViewAllClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("policy_breakdown_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
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
              .size(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(BlueLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Insurance Card",
              tint = BluePrimary,
              modifier = Modifier.size(18.dp)
            )
          }

          Column {
            Text(
              text = "Insurance Policies",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 17.5.sp
              )
            )
            Text(
              text = "Active Protection & Renewal Timers",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = BlueLight,
          border = androidx.compose.foundation.BorderStroke(1.dp, BlueBorder)
        ) {
          Text(
            text = "${policies.size} Active",
            style = MaterialTheme.typography.labelSmall.copy(
              color = BluePrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      if (policies.isEmpty()) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Slate50,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.Security, contentDescription = null, tint = Slate400, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("No Active Policies Logged", fontWeight = FontWeight.Bold, color = Slate900, fontSize = 14.5.sp)
            Text(
              "Enter your insurance cover in Easy Wealth Management to activate your policy tracker and sync to Google Sheets.",
              color = Slate700,
              fontSize = 12.5.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      } else {
        // List of Policies with Renewal Progress Bars
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        policies.forEach { policy ->
          val isUrgent = policy.daysRemaining <= 30
          val accentTint = when (policy.category) {
            InsuranceCategory.LIFE -> BluePrimary
            InsuranceCategory.HEALTH -> EmeraldPrimary
            InsuranceCategory.MOTOR -> if (isUrgent) AmberWarning else PurpleAccent
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isUrgent) Color(0xFFFED7AA) else Slate200
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("policy_item_${policy.id.lowercase()}")
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
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
                      .size(28.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .background(White)
                      .border(1.dp, Slate200, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = when (policy.category) {
                        InsuranceCategory.LIFE -> Icons.Default.Favorite
                        InsuranceCategory.HEALTH -> Icons.Default.LocalHospital
                        InsuranceCategory.MOTOR -> Icons.Default.DirectionsCar
                      },
                      contentDescription = policy.category.name,
                      tint = accentTint,
                      modifier = Modifier.size(16.dp)
                    )
                  }

                  Column {
                    Text(
                      text = policy.name,
                      style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Slate900,
                        fontSize = 14.5.sp
                      )
                    )
                    Text(
                      text = "${policy.provider} • #${policy.policyNumber}",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = Slate600,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                      )
                    )
                  }
                }

                // Sum Assured
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = policy.sumAssuredText,
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = Slate900,
                      fontSize = 14.5.sp
                    )
                  )
                  Text(
                    text = "Cover",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = Slate600,
                      fontSize = 10.5.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Renewal Progress Bar Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = if (isUrgent) Icons.Default.Timer else Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (isUrgent) RedDanger else Slate600,
                    modifier = Modifier.size(13.dp)
                  )
                  Text(
                    text = "Renewal in ${policy.daysRemaining} days (${policy.renewalDate})",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (isUrgent) RedDanger else Slate700,
                      fontWeight = if (isUrgent) FontWeight.Bold else FontWeight.SemiBold,
                      fontSize = 11.5.sp
                    )
                  )
                }

                if (isUrgent) {
                  TextButton(
                    onClick = { onRenewPolicy(policy.id) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                  ) {
                    Text(
                      text = "Renew Now",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = AmberWarning,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                      )
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              // Time Until Renewal Progress Bar
              LinearProgressIndicator(
                progress = { policy.progressPercent },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(5.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = if (isUrgent) AmberWarning else accentTint,
                trackColor = Slate200
              )
            }
          }
        }
      }
    }
  }
}
}
