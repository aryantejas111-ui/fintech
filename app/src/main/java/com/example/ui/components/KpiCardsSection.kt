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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KpiSummary
import com.example.ui.theme.*

@Composable
fun KpiCardsSection(
  kpiSummary: KpiSummary,
  onViewWealthClick: () -> Unit,
  onViewProtectionClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val netWorthText = when {
    kpiSummary.totalNetWorth <= 0.0 -> "₹0"
    kpiSummary.totalNetWorth >= 10000000.0 -> "₹${"%.2f".format(kpiSummary.totalNetWorth / 10000000.0)} Cr"
    kpiSummary.totalNetWorth >= 100000.0 -> "₹${"%.2f".format(kpiSummary.totalNetWorth / 100000.0)} L"
    else -> "₹${"%,d".format(kpiSummary.totalNetWorth.toLong())}"
  }

  val riskCoverText = when {
    kpiSummary.totalRiskCover <= 0.0 -> "₹0"
    kpiSummary.totalRiskCover >= 10000000.0 -> "₹${"%.2f".format(kpiSummary.totalRiskCover / 10000000.0)} Cr"
    kpiSummary.totalRiskCover >= 100000.0 -> "₹${"%.2f".format(kpiSummary.totalRiskCover / 100000.0)} L"
    else -> "₹${"%,d".format(kpiSummary.totalRiskCover.toLong())}"
  }

  val sipText = when {
    kpiSummary.activeSipMonthly <= 0.0 -> "₹0"
    else -> "₹${"%,d".format(kpiSummary.activeSipMonthly.toLong())}"
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 1. Total Net Worth Card (Wealth Centerpiece)
    KpiCard(
      title = "TOTAL NET WORTH",
      mainValue = netWorthText,
      secondaryValue = if (kpiSummary.totalNetWorth > 0) "+${kpiSummary.netWorthGrowthYoy}% YoY" else "0.0% YoY",
      secondaryIsPositive = kpiSummary.totalNetWorth > 0,
      subDescription = if (kpiSummary.totalNetWorth > 0) "Across Active Assets & Liquid Reserves" else "Zero assets • Tap to enter in Easy Wealth",
      icon = Icons.Default.AccountBalanceWallet,
      iconTint = EmeraldPrimary,
      iconBackground = EmeraldLight,
      badgeText = if (kpiSummary.totalNetWorth > 0) "Wealth Engine" else "Data Pending",
      badgeColor = if (kpiSummary.totalNetWorth > 0) EmeraldLight else Slate100,
      badgeTextColor = if (kpiSummary.totalNetWorth > 0) EmeraldDark else Slate600,
      onClick = onViewWealthClick,
      testTag = "kpi_net_worth_card"
    )

    // Row for the 2 complementary KPIs on mobile / side-by-side or stacked cleanly
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 2. Total Risk Cover
      KpiCompactCard(
        modifier = Modifier.weight(1f),
        title = "TOTAL RISK COVER",
        mainValue = riskCoverText,
        tag = if (kpiSummary.totalRiskCover > 0) "Life + Health" else "No Cover",
        subLabel = if (kpiSummary.totalRiskCover > 0) "Adequate Protection" else "0% Cover • Tap to Add",
        icon = Icons.Default.HealthAndSafety,
        iconTint = BluePrimary,
        iconBackground = BlueLight,
        accentColor = BluePrimary,
        testTag = "kpi_risk_cover_card",
        onClick = onViewProtectionClick
      )

      // 3. Active SIPs
      KpiCompactCard(
        modifier = Modifier.weight(1f),
        title = "ACTIVE SIPS",
        mainValue = sipText,
        tag = "/ month",
        subLabel = if (kpiSummary.activeSipMonthly > 0) "${kpiSummary.activeSipCount} Funds • ${kpiSummary.nextSipDate}" else "0 Funds • Tap to Add",
        icon = Icons.Default.Autorenew,
        iconTint = EmeraldPrimary,
        iconBackground = EmeraldLight,
        accentColor = EmeraldPrimary,
        testTag = "kpi_active_sips_card",
        onClick = onViewWealthClick
      )
    }
  }
}

@Composable
fun KpiCard(
  title: String,
  mainValue: String,
  secondaryValue: String,
  secondaryIsPositive: Boolean,
  subDescription: String,
  icon: ImageVector,
  iconTint: Color,
  iconBackground: Color,
  badgeText: String,
  badgeColor: Color,
  badgeTextColor: Color,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
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
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(iconBackground),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = title,
              tint = iconTint,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Slate700,
                letterSpacing = 0.8.sp,
                fontSize = 12.sp
              )
            )
            Text(
              text = "Aggregated Portfolio Value",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        // Pill badge
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = badgeColor,
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = if (secondaryIsPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
              contentDescription = null,
              tint = badgeTextColor,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = secondaryValue,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = badgeTextColor,
                fontSize = 12.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Text(
            text = mainValue,
            style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 34.sp,
              color = Slate900,
              letterSpacing = (-0.5).sp
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = subDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Slate700,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Slate50,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Text(
            text = "Verified Valuation",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}

@Composable
fun KpiCompactCard(
  title: String,
  mainValue: String,
  tag: String,
  subLabel: String,
  icon: ImageVector,
  iconTint: Color,
  iconBackground: Color,
  accentColor: Color,
  testTag: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier.testTag(testTag)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(iconBackground),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Slate50,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = Slate700,
          letterSpacing = 0.5.sp,
          fontSize = 11.5.sp
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = mainValue,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.ExtraBold,
          fontSize = 24.sp,
          color = Slate900
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(accentColor)
        )
        Text(
          text = subLabel,
          style = MaterialTheme.typography.labelSmall.copy(
            color = Slate800,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }
  }
}
