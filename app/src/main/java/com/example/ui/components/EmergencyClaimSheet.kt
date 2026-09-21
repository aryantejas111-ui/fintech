package com.example.ui.components

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyClaimSheet(
  userProfile: UserProfile,
  onDismiss: () -> Unit,
  onSubmitClaim: (category: String, subject: String, details: String, urgency: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("Health (Galaxy Plan)") }
  var subjectText by remember { mutableStateOf("Emergency Hospital Admission") }
  var hospitalOrLocation by remember { mutableStateOf("Apollo Hospital, Bannerghatta") }
  var cashlessPreferred by remember { mutableStateOf(true) }

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
    modifier = modifier.testTag("emergency_claim_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Emergency Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(RedLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = "Emergency",
              tint = RedDanger,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = "Emergency Claim & Support",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.5.sp,
                color = Slate900
              )
            )
            Text(
              text = "Fast-track 24x7 Cashless Desk for ${userProfile.name}",
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

      // 24x7 Hotlines Card
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate50),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(EmeraldLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "Toll-Free SOS Hotline",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                text = "1800-FINTECH-SOS (346832)",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 14.5.sp
                )
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = EmeraldPrimary
          ) {
            Text(
              text = "24x7 Active",
              style = MaterialTheme.typography.labelSmall.copy(
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // Policy Selection
      Text(
        text = "SELECT RELEVANT POLICY",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = Slate700,
          fontSize = 12.sp,
          letterSpacing = 0.5.sp
        )
      )

      val policyOptions = listOf(
        "Health (Galaxy Plan)",
        "Motor (BMW 3 Series)",
        "Life (LIC Tech Term)"
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        policyOptions.forEach { opt ->
          val isSelected = opt == selectedCategory
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) BlueLight else Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) BlueBorder else Slate200
            ),
            modifier = Modifier
              .weight(1f)
              .clickable { selectedCategory = opt }
          ) {
            Text(
              text = opt,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) BlueDeep else Slate800,
                fontSize = 12.sp
              ),
              modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
            )
          }
        }
      }

      // Input: Subject / Incident
      OutlinedTextField(
        value = subjectText,
        onValueChange = { subjectText = it },
        label = { Text("Incident / Claim Reason") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = BluePrimary,
          unfocusedBorderColor = Slate300
        )
      )

      // Input: Hospital / Workshop Location
      OutlinedTextField(
        value = hospitalOrLocation,
        onValueChange = { hospitalOrLocation = it },
        label = { Text("Hospital / Workshop Network Location") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = BluePrimary,
          unfocusedBorderColor = Slate300
        )
      )

      // Cashless Preference Toggle
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Slate50,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { cashlessPreferred = !cashlessPreferred }
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CreditScore,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Column {
              Text(
                text = "Instant Cashless Authorization",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 13.5.sp
                )
              )
              Text(
                text = "Direct TPA settlement at recognized hospital network",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }

          Switch(
            checked = cashlessPreferred,
            onCheckedChange = { cashlessPreferred = it }
          )
        }
      }

      // Submit Button
      Button(
        onClick = {
          onSubmitClaim(
            selectedCategory,
            subjectText,
            hospitalOrLocation,
            "HIGH_PRIORITY"
          )
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = RedDanger,
          contentColor = White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("submit_claim_intimation_button")
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Submit Fast-Track Claim Intimation",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = White,
            fontSize = 15.sp
          )
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
