package com.example.ui.views

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
import com.example.model.FamilyGoalItem
import com.example.ui.theme.*

@Composable
fun FamilyGoalsView(
  goals: List<FamilyGoalItem>,
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
            text = "Family Financial Goals",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Slate900
            )
          )
          Text(
            text = "Milestones Protected with Insurance & SIPs",
            style = MaterialTheme.typography.bodyMedium.copy(color = Slate500)
          )
        }

        Button(
          onClick = onBackToDashboard,
          colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = Slate800),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    items(goals) { goal ->
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("goal_card_${goal.id.lowercase()}")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                  .clip(RoundedCornerShape(8.dp))
                  .background(EmeraldLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (goal.category) {
                    "Child Education" -> Icons.Default.School
                    "Retirement" -> Icons.Default.BeachAccess
                    else -> Icons.Default.Home
                  },
                  contentDescription = null,
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              Column {
                Text(
                  text = goal.title,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 15.sp
                  )
                )
                Text(
                  text = "Target Year: ${goal.targetYear} • ${goal.category}",
                  style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = EmeraldLight,
              border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
            ) {
              Text(
                text = "${(goal.completionPercent * 100).toInt()}% Funded",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = EmeraldDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Target vs Current
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Current Corpus",
                style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
              )
              Text(
                text = "₹${goal.currentLakhs} Lakhs",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900
                )
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Target Corpus",
                style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
              )
              Text(
                text = "₹${goal.targetLakhs} Lakhs",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BlueDeep
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          LinearProgressIndicator(
            progress = { goal.completionPercent },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = EmeraldPrimary,
            trackColor = Slate200
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SIP Allocation: ${goal.monthlyContribution}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontWeight = FontWeight.Medium
              )
            )

            Text(
              text = "Term Life Protected",
              style = MaterialTheme.typography.labelSmall.copy(
                color = BluePrimary,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    }
  }
}
