package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InsuranceCategory
import com.example.model.ProtectionRingItem
import com.example.ui.theme.*

@Composable
fun ProtectionRingChart(
  items: List<ProtectionRingItem>,
  selectedCategory: InsuranceCategory,
  onSelectCategory: (InsuranceCategory) -> Unit,
  onViewPoliciesClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentItem = items.find { it.category == selectedCategory } ?: items.first()

  // Ring colors
  val lifeColor = BluePrimary
  val healthColor = EmeraldPrimary
  val motorColor = PurpleAccent

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("protection_ring_chart_card")
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
              imageVector = Icons.Default.Security,
              contentDescription = "Protection Ring",
              tint = BluePrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "The Protection Ring",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 17.5.sp
              )
            )
            Text(
              text = "Coverage Gauge vs. Recommended",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate600,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Slate50,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Text(
            text = "Holistic Risk Gauge",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Centerpiece: Concentric Multi-Ring Doughnut Chart & Center Badge
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp),
        contentAlignment = Alignment.Center
      ) {
        val lifeSweep = animateFloatAsState(
          targetValue = 1.0f,
          animationSpec = tween(1000),
          label = "lifeSweep"
        )
        val healthSweep = animateFloatAsState(
          targetValue = 0.70f,
          animationSpec = tween(1000),
          label = "healthSweep"
        )
        val motorSweep = animateFloatAsState(
          targetValue = 1.0f,
          animationSpec = tween(1000),
          label = "motorSweep"
        )

        Canvas(
          modifier = Modifier
            .size(190.dp)
            .testTag("protection_ring_canvas")
        ) {
          val strokeWidth = 12.dp.toPx()
          val centerOffset = Offset(size.width / 2f, size.height / 2f)

          // Ring 1 (Outer - Life)
          val r1 = (size.width / 2f) - strokeWidth
          drawCircle(
            color = Slate100,
            radius = r1,
            center = centerOffset,
            style = Stroke(width = strokeWidth)
          )
          drawArc(
            color = lifeColor,
            startAngle = -90f,
            sweepAngle = 360f * lifeSweep.value,
            useCenter = false,
            topLeft = Offset(centerOffset.x - r1, centerOffset.y - r1),
            size = Size(r1 * 2, r1 * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
          )

          // Ring 2 (Middle - Health)
          val r2 = r1 - strokeWidth - 6.dp.toPx()
          drawCircle(
            color = Slate100,
            radius = r2,
            center = centerOffset,
            style = Stroke(width = strokeWidth)
          )
          drawArc(
            color = healthColor,
            startAngle = -90f,
            sweepAngle = 360f * healthSweep.value,
            useCenter = false,
            topLeft = Offset(centerOffset.x - r2, centerOffset.y - r2),
            size = Size(r2 * 2, r2 * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
          )

          // Ring 3 (Inner - Motor)
          val r3 = r2 - strokeWidth - 6.dp.toPx()
          drawCircle(
            color = Slate100,
            radius = r3,
            center = centerOffset,
            style = Stroke(width = strokeWidth)
          )
          drawArc(
            color = motorColor,
            startAngle = -90f,
            sweepAngle = 360f * motorSweep.value,
            useCenter = false,
            topLeft = Offset(centerOffset.x - r3, centerOffset.y - r3),
            size = Size(r3 * 2, r3 * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
          )
        }

        // Center Text
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "90%",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 24.sp,
              color = Slate900
            )
          )
          Text(
            text = "ADEQUACY",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
              color = Slate600,
              letterSpacing = 0.5.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Category Toggle Pills (Life, Health, Motor)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items.forEach { item ->
          val isSelected = item.category == selectedCategory
          val pillColor = when (item.category) {
            InsuranceCategory.LIFE -> lifeColor
            InsuranceCategory.HEALTH -> healthColor
            InsuranceCategory.MOTOR -> motorColor
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) Slate100 else Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) pillColor else Slate200
            ),
            modifier = Modifier
              .weight(1f)
              .clickable { onSelectCategory(item.category) }
              .testTag("ring_category_${item.category.name.lowercase()}")
          ) {
            Column(
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(pillColor)
                )
                Text(
                  text = when (item.category) {
                    InsuranceCategory.LIFE -> "Life"
                    InsuranceCategory.HEALTH -> "Health"
                    InsuranceCategory.MOTOR -> "Motor"
                  },
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) Slate900 else Slate700,
                    fontSize = 12.5.sp
                  )
                )
              }

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = "${item.coveragePercent}%",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (item.coveragePercent >= 100) EmeraldPrimary else AmberWarning,
                  fontSize = 12.5.sp
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Selected Ring Detail Card
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate50),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = currentItem.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp,
                color = Slate900
              )
            )

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (currentItem.coveragePercent >= 100) EmeraldLight else AmberLight,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (currentItem.coveragePercent >= 100) EmeraldBorder else Color(0xFFFDE68A)
              )
            ) {
              Text(
                text = currentItem.status,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (currentItem.coveragePercent >= 100) EmeraldDark else AmberWarning,
                  fontSize = 11.5.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Current vs Recommended Comparison Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Current Cover",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                text = "${currentItem.currentCover} ${currentItem.unitLabel}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 16.5.sp
                )
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Recommended Cover",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                text = "${currentItem.recommendedCover} ${currentItem.unitLabel}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BlueDeep,
                  fontSize = 16.5.sp
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Recommendation Callout
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = if (currentItem.coveragePercent >= 100) EmeraldPrimary else AmberWarning,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = currentItem.recommendationText,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.5.sp,
                color = Slate700,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Action Button
          OutlinedButton(
            onClick = onViewPoliciesClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueDeep),
            border = androidx.compose.foundation.BorderStroke(1.dp, BlueBorder),
            modifier = Modifier
              .fillMaxWidth()
              .height(34.dp),
            contentPadding = PaddingValues(0.dp)
          ) {
            Text(
              text = "View All Policies & Documents",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(13.dp)
            )
          }
        }
      }
    }
  }
}
