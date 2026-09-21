package com.example.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.ProtectionRingItem
import com.example.ui.components.ProtectionRingChart
import com.example.ui.theme.*

@Composable
fun ProtectionDeepDiveView(
  ringItems: List<ProtectionRingItem>,
  selectedCategory: InsuranceCategory,
  onSelectCategory: (InsuranceCategory) -> Unit,
  policies: List<PolicyItem>,
  onRenewPolicy: (String) -> Unit,
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
            text = "My Protection Portfolio",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Slate900,
              fontSize = 24.sp
            )
          )
          Text(
            text = "Life, Health & General Risk Coverage",
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

    item {
      ProtectionRingChart(
        items = ringItems,
        selectedCategory = selectedCategory,
        onSelectCategory = onSelectCategory,
        onViewPoliciesClick = {}
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ALL ACTIVE POLICIES (${policies.size})",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Slate700,
            fontSize = 12.sp,
            letterSpacing = 0.5.sp
          )
        )

        Button(
          onClick = onRequestClaim,
          colors = ButtonDefaults.buttonColors(containerColor = RedLight, contentColor = RedDanger),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(32.dp)
        ) {
          Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Fast-Track Claim", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    items(policies) { policy ->
      val isUrgent = policy.daysRemaining <= 30
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isUrgent) Color(0xFFFED7AA) else Slate200
        ),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = policy.name,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 15.sp
                )
              )
              Text(
                text = "${policy.provider} • Policy #${policy.policyNumber}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isUrgent) AmberLight else BlueLight
            ) {
              Text(
                text = if (isUrgent) "Expires in ${policy.daysRemaining}d" else "Active",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isUrgent) AmberWarning else BlueDeep,
                  fontSize = 11.5.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                "Sum Assured",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                policy.sumAssuredText,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 14.5.sp
                )
              )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                "Annual Premium",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                policy.premiumAmount,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 14.5.sp
                )
              )
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(
                "Renewal Date",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                policy.renewalDate,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 14.5.sp
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LinearProgressIndicator(
            progress = { policy.progressPercent },
            modifier = Modifier
              .fillMaxWidth()
              .height(5.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = if (isUrgent) AmberWarning else BluePrimary,
            trackColor = Slate200
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Button(
              onClick = { onRenewPolicy(policy.id) },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isUrgent) AmberWarning else BlueDeep,
                contentColor = White
              ),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
              modifier = Modifier.height(34.dp)
            ) {
              Text("Pay / Renew Premium", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
