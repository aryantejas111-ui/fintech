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
import com.example.model.AssetClass
import com.example.model.WealthAssetItem
import com.example.ui.theme.*

@Composable
fun WealthBreakdownCard(
  assets: List<WealthAssetItem>,
  onAssetClick: (String) -> Unit,
  onAddInvestmentClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("wealth_breakdown_card")
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
              .background(EmeraldLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AccountBalance,
              contentDescription = "Wealth Card",
              tint = EmeraldPrimary,
              modifier = Modifier.size(18.dp)
            )
          }

          Column {
            Text(
              text = "Portfolio Holdings",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 17.5.sp
              )
            )
            Text(
              text = "Current Assets & Daily NAV Movers",
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
          color = EmeraldLight,
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
        ) {
          Text(
            text = if (assets.isEmpty()) "0 Assets" else "${assets.size} Active Holdings",
            style = MaterialTheme.typography.labelSmall.copy(
              color = EmeraldDark,
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      if (assets.isEmpty()) {
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
            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Slate400, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("No Wealth Assets Logged", fontWeight = FontWeight.Bold, color = Slate900, fontSize = 14.5.sp)
            Text(
              "Enter your mutual funds, bank savings, gold & real estate in Easy Wealth Management to sync to Google Sheets.",
              color = Slate700,
              fontSize = 12.5.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      } else {
        // List of Assets with green/red NAV tags
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        assets.forEach { asset ->
          val isPositive = asset.isPositive
          val tagColor = if (isPositive) EmeraldLight else RedLight
          val tagTextColor = if (isPositive) EmeraldDark else RedDanger
          val tagBorder = if (isPositive) EmeraldBorder else Color(0xFFFECACA)

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Slate50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("wealth_asset_${asset.id.lowercase()}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = asset.fundName,
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = Slate900,
                      fontSize = 14.5.sp
                    )
                  )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                  text = "${asset.units} • XIRR ${asset.xirr}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate600,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                  )
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = asset.currentValueText,
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 15.5.sp
                  )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Green/Red Indicator Tag for Daily NAV
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = tagColor,
                  border = androidx.compose.foundation.BorderStroke(1.dp, tagBorder)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                  ) {
                    Icon(
                      imageVector = if (isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                      contentDescription = null,
                      tint = tagTextColor,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = "${if (isPositive) "+" else ""}${asset.dailyChangePercent}%",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = tagTextColor,
                        fontSize = 11.sp
                      )
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
}
}
