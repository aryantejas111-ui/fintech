package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

/**
 * Centrally positioned flagship card highlighting 360-degree comprehensive protection
 * across Life, Health, and Vehicle pillars.
 */
@Composable
fun AllRoundProtectionCard(
  onExploreProtection: () -> Unit,
  onEmergencyDesk: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPillarIndex by remember { mutableIntStateOf(0) }

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = White),
    border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("all_round_protection_hero_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Top Header & Immunity Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFEFF6FF),
            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(20.dp)
              )
            }
          }

          Column {
            Text(
              text = "360° All-Round Protection",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Slate900,
                fontSize = 18.sp
              )
            )
            Text(
              text = "Triad of Survival: Life • Health • Vehicle",
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
          color = Color(0xFFDCFCE7),
          border = BorderStroke(1.dp, Color(0xFF86EFAC))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Color(0xFF16A34A))
            )
            Text(
              text = "3/3 Covered",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534),
                fontSize = 11.sp
              )
            )
          }
        }
      }

      // ==========================================================
      // BEST ATTRACTIVE IMAGE SHOWCASING ALL-ROUND 360 PROTECTION
      // ==========================================================
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.35f)), RoundedCornerShape(16.dp))
          .testTag("protection_umbrella_image_box")
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_all_sides_protection),
          contentDescription = "Comprehensive Protection Shield Covering Life, Health, and Vehicle",
          modifier = Modifier
            .fillMaxWidth()
            .height(185.dp),
          contentScale = ContentScale.Crop
        )

        // Overlay Badge At Top-Left of Image
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xCC0B1528),
          border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f)),
          modifier = Modifier
            .padding(10.dp)
            .align(Alignment.TopStart)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFF38BDF8),
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = "Complete Umbrella Shield",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFF0F9FF),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            )
          }
        }

        // Bottom Banner Overlay on Image: 3 Quick Labels
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0xEE0B1528))
              )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
            Text("Life: ₹1.5 Cr", color = Color(0xFFFEF08A), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF38BDF8)))
            Text("Health: ₹15 Lakh", color = Color(0xFFBAE6FD), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF4ADE80)))
            Text("Vehicle: Zero-Dep", color = Color(0xFFBBF7D0), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Impressive "Why 360° Protection is Mandatory" Narrative
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "Why You Need Protection on All 3 Sides",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = Slate900,
              fontSize = 13.5.sp
            )
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "A single uninsured hospital admission, critical accident, or total car loss can deplete years of investment gains. True wealth preservation requires an unbreakable 3-point shield.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Slate700,
              fontSize = 12.5.sp,
              lineHeight = 17.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      // Interactive 3 Pillars Selector Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val pillars = listOf(
          Triple("1. Life", Icons.Default.Favorite, Color(0xFFDC2626)),
          Triple("2. Health", Icons.Default.LocalHospital, Color(0xFF0284C7)),
          Triple("3. Vehicle", Icons.Default.DirectionsCar, Color(0xFF16A34A))
        )

        pillars.forEachIndexed { index, (label, icon, color) ->
          val isSelected = selectedPillarIndex == index
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) color.copy(alpha = 0.12f) else Slate50,
            border = BorderStroke(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) color else Slate200
            ),
            modifier = Modifier
              .weight(1f)
              .clickable { selectedPillarIndex = index }
              .testTag("protection_pillar_tab_$index")
          ) {
            Row(
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) color else Slate500,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                  color = if (isSelected) Slate900 else Slate600,
                  fontSize = 12.sp
                )
              )
            }
          }
        }
      }

      // Detailed Card for Selected Pillar
      when (selectedPillarIndex) {
        0 -> PillarDetailCard(
          title = "Life & Family Financial Immunity",
          coverAmount = "₹1.50 Crore Sum Assured",
          vitalNeed = "Guarantees family income replacement, loan clearances, and child education if you are not around.",
          status = "Active Term Shield (HDFC Life Click 2 Protect)",
          accentColor = Color(0xFFDC2626),
          bgColor = Color(0xFFFEF2F2),
          icon = Icons.Default.Favorite
        )
        1 -> PillarDetailCard(
          title = "Health & Critical Hospitalization",
          coverAmount = "₹15.00 Lakh Cashless Cover",
          vitalNeed = "Guards your savings against surging medical costs, ICU charges, and high-cost critical illness treatments.",
          status = "100% Cashless at 10,000+ Network Hospitals (Care Health)",
          accentColor = Color(0xFF0284C7),
          bgColor = Color(0xFFEFF6FF),
          icon = Icons.Default.LocalHospital
        )
        2 -> PillarDetailCard(
          title = "Vehicle & Mobility Armor",
          coverAmount = "Comprehensive Zero-Depreciation",
          vitalNeed = "Protects against accident repairs, third-party liability, natural calamities, and roadside breakdown expenses.",
          status = "Zero-Dep Active with 24x7 Roadside Assist (Tata AIG Drive)",
          accentColor = Color(0xFF16A34A),
          bgColor = Color(0xFFF0FDF4),
          icon = Icons.Default.DirectionsCar
        )
      }

      // Bottom Dual Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onEmergencyDesk,
          shape = RoundedCornerShape(10.dp),
          border = BorderStroke(1.dp, Color(0xFFFECACA)),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFFFEF2F2),
            contentColor = Color(0xFFDC2626)
          ),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("pillar_emergency_sos_button")
        ) {
          Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Emergency SOS",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp
            )
          )
        }

        Button(
          onClick = onExploreProtection,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F172A),
            contentColor = White
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("pillar_view_all_policies_button")
        ) {
          Text(
            text = "Review Policies",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp
            )
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}

@Composable
private fun PillarDetailCard(
  title: String,
  coverAmount: String,
  vitalNeed: String,
  status: String,
  accentColor: Color,
  bgColor: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
          Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = Slate900,
              fontSize = 13.5.sp
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = White,
          border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
        ) {
          Text(
            text = coverAmount,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = accentColor,
              fontSize = 11.5.sp
            ),
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
          )
        }
      }

      Text(
        text = vitalNeed,
        style = MaterialTheme.typography.bodySmall.copy(
          color = Slate700,
          fontSize = 12.5.sp,
          lineHeight = 17.5.sp,
          fontWeight = FontWeight.Medium
        )
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(14.dp))
        Text(
          text = status,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Slate800,
            fontSize = 11.5.sp
          )
        )
      }
    }
  }
}
