package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WealthProjectionPoint
import com.example.ui.theme.*

@Composable
fun WealthTrackerChart(
  projectionPoints: List<WealthProjectionPoint>,
  selectedYear: Int,
  onSelectYear: (Int) -> Unit,
  onExploreFundsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val selectedPoint = projectionPoints.find { it.yearNumber == selectedYear }
    ?: projectionPoints.last()

  val animatedProgress = animateFloatAsState(
    targetValue = 1f,
    animationSpec = tween(1200),
    label = "lineProgress"
  )

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("wealth_tracker_chart_card")
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
              imageVector = Icons.Default.TrendingUp,
              contentDescription = "Wealth Tracker",
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "The Wealth Tracker",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 17.5.sp
              )
            )
            Text(
              text = "5-Year SIP & Mutual Fund Growth",
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
          color = EmeraldLight,
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
        ) {
          Text(
            text = "13.5% CAGR Model",
            style = MaterialTheme.typography.labelSmall.copy(
              color = EmeraldDark,
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Highlight Metric Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Text(
            text = "Projected Wealth (${selectedPoint.yearLabel})",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate600,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          )
          Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "₹${selectedPoint.projectedLakhs} L",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = EmeraldDark,
                fontSize = 28.sp
              )
            )
            Text(
              text = "Invested: ₹${selectedPoint.investedLakhs} L",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = Slate600,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        val estimatedProfit = selectedPoint.projectedLakhs - selectedPoint.investedLakhs
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Slate50,
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Text(
            text = "+₹${"%.1f".format(estimatedProfit)} L Profit",
            style = MaterialTheme.typography.labelSmall.copy(
              color = EmeraldPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Custom Line Chart Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Canvas(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(projectionPoints) {
              detectTapGestures { offset ->
                val stepX = size.width / (projectionPoints.size - 1)
                val tappedIndex = (offset.x / stepX)
                  .toInt()
                  .coerceIn(0, projectionPoints.size - 1)
                onSelectYear(projectionPoints[tappedIndex].yearNumber)
              }
            }
            .testTag("wealth_tracker_chart_canvas")
        ) {
          val width = size.width
          val height = size.height
          val paddingBottom = 24.dp.toPx()
          val paddingTop = 16.dp.toPx()
          val chartHeight = height - paddingBottom - paddingTop

          val maxVal = 120f
          val minVal = 0f

          val count = projectionPoints.size
          val stepX = width / (count - 1)

          // Background Grid Horizontal Lines
          val gridLines = 4
          for (i in 0..gridLines) {
            val y = paddingTop + (chartHeight * (i.toFloat() / gridLines))
            drawLine(
              color = Slate100,
              start = Offset(0f, y),
              end = Offset(width, y),
              strokeWidth = 1.dp.toPx()
            )
          }

          // Points coordinates
          val projectedCoords = projectionPoints.mapIndexed { index, pt ->
            val x = index * stepX
            val ratio = (pt.projectedLakhs - minVal) / (maxVal - minVal)
            val y = height - paddingBottom - (chartHeight * ratio * animatedProgress.value)
            Offset(x, y)
          }

          val investedCoords = projectionPoints.mapIndexed { index, pt ->
            val x = index * stepX
            val ratio = (pt.investedLakhs - minVal) / (maxVal - minVal)
            val y = height - paddingBottom - (chartHeight * ratio * animatedProgress.value)
            Offset(x, y)
          }

          // Gradient Area Fill under Projected Wealth Curve
          val fillPath = Path().apply {
            moveTo(projectedCoords.first().x, height - paddingBottom)
            projectedCoords.forEachIndexed { i, pt ->
              if (i == 0) {
                lineTo(pt.x, pt.y)
              } else {
                val prev = projectedCoords[i - 1]
                val midX = (prev.x + pt.x) / 2f
                cubicTo(midX, prev.y, midX, pt.y, pt.x, pt.y)
              }
            }
            lineTo(projectedCoords.last().x, height - paddingBottom)
            close()
          }

          drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
              colors = listOf(
                EmeraldPrimary.copy(alpha = 0.25f),
                EmeraldPrimary.copy(alpha = 0.02f)
              ),
              startY = paddingTop,
              endY = height - paddingBottom
            )
          )

          // Draw Invested Capital line (dashed)
          val investedPath = Path().apply {
            investedCoords.forEachIndexed { i, pt ->
              if (i == 0) moveTo(pt.x, pt.y)
              else {
                val prev = investedCoords[i - 1]
                val midX = (prev.x + pt.x) / 2f
                cubicTo(midX, prev.y, midX, pt.y, pt.x, pt.y)
              }
            }
          }
          drawPath(
            path = investedPath,
            color = Slate400,
            style = Stroke(
              width = 2.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
          )

          // Draw Projected Wealth line (bold smooth curve)
          val projectedPath = Path().apply {
            projectedCoords.forEachIndexed { i, pt ->
              if (i == 0) moveTo(pt.x, pt.y)
              else {
                val prev = projectedCoords[i - 1]
                val midX = (prev.x + pt.x) / 2f
                cubicTo(midX, prev.y, midX, pt.y, pt.x, pt.y)
              }
            }
          }
          drawPath(
            path = projectedPath,
            color = EmeraldPrimary,
            style = Stroke(
              width = 3.5.dp.toPx(),
              cap = StrokeCap.Round,
              join = StrokeJoin.Round
            )
          )

          // Highlight selected year vertical guideline & points
          val selectedIndex = projectionPoints.indexOfFirst { it.yearNumber == selectedYear }
          if (selectedIndex >= 0) {
            val selectedOffset = projectedCoords[selectedIndex]
            drawLine(
              color = Slate300,
              start = Offset(selectedOffset.x, paddingTop),
              end = Offset(selectedOffset.x, height - paddingBottom),
              strokeWidth = 1.5.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            )

            // Draw outer glow and inner circle
            drawCircle(
              color = EmeraldPrimary.copy(alpha = 0.2f),
              radius = 12.dp.toPx(),
              center = selectedOffset
            )
            drawCircle(
              color = White,
              radius = 6.dp.toPx(),
              center = selectedOffset
            )
            drawCircle(
              color = EmeraldPrimary,
              radius = 4.dp.toPx(),
              center = selectedOffset
            )
          }

          // Small dots at other points
          projectedCoords.forEachIndexed { idx, offset ->
            if (idx != selectedIndex) {
              drawCircle(
                color = White,
                radius = 4.dp.toPx(),
                center = offset
              )
              drawCircle(
                color = EmeraldPrimary,
                radius = 2.5.dp.toPx(),
                center = offset
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Year Selector Interactive Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        projectionPoints.forEach { pt ->
          val isSelected = pt.yearNumber == selectedYear
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSelected) EmeraldPrimary else Slate50,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) EmeraldPrimary else Slate200
            ),
            modifier = Modifier
              .clickable { onSelectYear(pt.yearNumber) }
              .testTag("wealth_year_button_${pt.yearNumber}")
          ) {
            Text(
              text = pt.yearLabel,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) White else Slate800,
                fontSize = 12.sp
              ),
              modifier = Modifier.padding(horizontal = 11.dp, vertical = 4.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Chart Legend
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary)
          )
          Text(
            text = "Projected Wealth (13.5% CAGR)",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .width(12.dp)
              .height(2.dp)
              .background(Slate500)
          )
          Text(
            text = "Invested Capital",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }
    }
  }
}
