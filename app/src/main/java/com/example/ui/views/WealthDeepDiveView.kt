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
import com.example.model.WealthAssetItem
import com.example.model.WealthProjectionPoint
import com.example.ui.components.WealthTrackerChart
import com.example.ui.theme.*

@Composable
fun WealthDeepDiveView(
  assets: List<WealthAssetItem>,
  projectionPoints: List<WealthProjectionPoint>,
  selectedYear: Int,
  onSelectYear: (Int) -> Unit,
  onBackToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .padding(16.dp),
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
            text = "My Wealth Portfolio",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Slate900
            )
          )
          Text(
            text = "Mutual Funds, Equity & Emergency Reserves",
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

    item {
      WealthTrackerChart(
        projectionPoints = projectionPoints,
        selectedYear = selectedYear,
        onSelectYear = onSelectYear,
        onExploreFundsClick = {}
      )
    }

    item {
      Text(
        text = "PORTFOLIO ASSETS & DAILY NAV PERFORMANCE",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = Slate500,
          letterSpacing = 0.5.sp
        )
      )
    }

    items(assets) { asset ->
      val isPositive = asset.isPositive
      val tagColor = if (isPositive) EmeraldLight else RedLight
      val tagTextColor = if (isPositive) EmeraldDark else RedDanger
      val tagBorder = if (isPositive) EmeraldBorder else Color(0xFFFECACA)

      Surface(
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = asset.fundName,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 14.sp
                )
              )
              Text(
                text = "${asset.assetClass.name} • ${asset.units}",
                style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = tagColor,
              border = androidx.compose.foundation.BorderStroke(1.dp, tagBorder)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                  contentDescription = null,
                  tint = tagTextColor,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "${if (isPositive) "+" else ""}${asset.dailyChangePercent}% Today",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = tagTextColor,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Invested", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
              Text(
                asset.investedAmountText,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = Slate700
                )
              )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Current Value", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
              Text(
                asset.currentValueText,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900
                )
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text("Annualized XIRR", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
              Text(
                asset.xirr,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = EmeraldPrimary
                )
              )
            }
          }
        }
      }
    }
  }
}
