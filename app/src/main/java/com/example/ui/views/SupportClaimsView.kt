package com.example.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun SupportClaimsView(
  userProfile: UserProfile,
  onRequestClaim: () -> Unit,
  onBackToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Claims & Support Desk",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Slate900,
              fontSize = 24.sp
            )
          )
          Text(
            text = "24x7 Emergency Assistance & Concierge",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Slate600,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Button(
          onClick = onBackToDashboard,
          colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = Slate800),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Dashboard", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Emergency Action Banner
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = RedLight,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(RedDanger),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(22.dp)
              )
            }

            Column {
              Text(
                text = "Fast-Track Emergency Claim",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RedDanger,
                  fontSize = 16.sp
                )
              )
              Text(
                text = "Instant cashless admission & emergency vehicle tow",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = Slate800,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onRequestClaim,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = RedDanger,
              contentColor = White
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("File Instant Emergency Claim", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
        }
      }
    }

    // Dedicated Wealth & Protection Manager
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "YOUR DEDICATED WEALTH & CLAIM ADVISOR",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Slate700,
              fontSize = 12.sp,
              letterSpacing = 0.5.sp
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(EmeraldLight)
                .border(1.5.dp, EmeraldBorder, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "VR",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = EmeraldDark
                )
              )
            }

            Column {
              Text(
                text = userProfile.advisorName,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 16.sp
                )
              )
              Text(
                text = "${userProfile.advisorTitle} • SEBI & IRDAI Certified",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = Slate600,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                text = "Direct: ${userProfile.advisorPhone}",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = BluePrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              )
            }
          }
        }
      }
    }

    // Active Claims Status
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "RECENT CLAIM HISTORY",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Slate700,
              fontSize = 12.sp,
              letterSpacing = 0.5.sp
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Slate50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Claim #CLM-94021 - Cashless Dental & OPD",
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 14.sp
                  )
                )
                Text(
                  text = "Star Galaxy Plan • Settled ₹14,200 on 12 Jul 2026",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )
                )
              }

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = EmeraldLight
              ) {
                Text(
                  text = "Settled",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = EmeraldDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp
                  ),
                  modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
