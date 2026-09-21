package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 4K Ultra-Crisp Greenish-Black & Emerald Palette for IRDAI Certification
private val DeepGreenishBlack = Color(0xFF04160E)
private val GreenishBlackSurface = Color(0xFF082618)
private val EmeraldBorderHairline = Color(0xFF166534)
private val EmeraldGlow = Color(0xFF22C55E)
private val EmeraldTextHighContrast = Color(0xFFDCFCE7)
private val EmeraldSubText = Color(0xFF86EFAC)

// 4K Luxury Light Canvas Colors
private val SlateCanvasStart = Color(0xFFF8FAFC)
private val SlateCanvasMid = Color(0xFFF1F5F9)
private val SlateCanvasEnd = Color(0xFFE2E8F0)
private val Slate900 = Color(0xFF0F172A)
private val Slate800 = Color(0xFF1E293B)
private val Slate700 = Color(0xFF334155)
private val Slate600 = Color(0xFF475569)
private val Slate500 = Color(0xFF64748B)

@Composable
fun GalaxyCongratulationsSplashScreen(
  onTimeout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val progress = remember { Animatable(0f) }
  var remainingSeconds by remember { mutableIntStateOf(3) }

  val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  // 3-second timer for every visit of the app
  LaunchedEffect(Unit) {
    val animationJob = launch {
      progress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
      )
    }

    for (sec in 3 downTo 1) {
      remainingSeconds = sec
      delay(1000L)
    }
    remainingSeconds = 0
    animationJob.join()
    onTimeout()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            SlateCanvasStart,
            SlateCanvasMid,
            SlateCanvasEnd
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("greetings_splash_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // -------------------------------------------------------------
      // TOP CORNER BAR: Countdown Pill (Left) & 4K IRDAI Certified (Right)
      // -------------------------------------------------------------
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 2.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // High-Precision Timer Pill
        Surface(
          shape = RoundedCornerShape(24.dp),
          color = Color.White,
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
          ) {
            Box(
              modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(Color(0xFF0284C7).copy(alpha = pulseAlpha))
            )
            Text(
              text = if (remainingSeconds > 0) "Opening in ${remainingSeconds}s" else "Launching...",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                fontSize = 11.5.sp,
                letterSpacing = 0.2.sp
              )
            )
          }
        }

        // TOP CORNER: 4K Greenish-Black IRDAI Certified Jewel Badge
        Surface(
          shape = RoundedCornerShape(24.dp),
          color = DeepGreenishBlack,
          border = BorderStroke(1.2.dp, EmeraldBorderHairline),
          shadowElevation = 4.dp,
          modifier = Modifier.testTag("splash_irdai_certified_badge")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
          ) {
            Box(
              modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    colors = listOf(Color(0xFF22C55E), Color(0xFF15803D))
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Verified",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
            }

            Column(horizontalAlignment = Alignment.Start) {
              Text(
                text = "IRDAI CERTIFIED",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  color = EmeraldTextHighContrast,
                  fontSize = 12.sp,
                  letterSpacing = 0.8.sp
                )
              )
              Text(
                text = "Govt. Recognized Advisory",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Medium,
                  color = EmeraldSubText,
                  fontSize = 10.sp,
                  letterSpacing = 0.3.sp
                )
              )
            }
          }
        }
      }

      // 4K Ultra-Crisp Precision Progress Track
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(Color(0xFFE2E8F0))
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(fraction = progress.value)
            .fillMaxHeight()
            .clip(RoundedCornerShape(3.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color(0xFF0284C7),
                  Color(0xFF0EA5E9),
                  Color(0xFF38BDF8)
                )
              )
            )
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // -------------------------------------------------------------
      // 4K GREETINGS HEADER: Brand Identity & Value Proposition
      // -------------------------------------------------------------
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFFE0F2FE),
          border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
          modifier = Modifier.padding(bottom = 8.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Diamond,
              contentDescription = null,
              tint = Color(0xFF0284C7),
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = "LUCKY LIFE WEALTH ADVISORY",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0369A1),
                fontSize = 11.5.sp,
                letterSpacing = 1.2.sp
              )
            )
          }
        }

        Text(
          text = "Welcome to Fintech",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Black,
            color = Slate900,
            fontSize = 28.sp,
            letterSpacing = (-0.5).sp
          ),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Institutional Wealth Intelligence & 360° Protection",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = Slate700,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Medium
          ),
          textAlign = TextAlign.Center
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // -------------------------------------------------------------
      // 4K GREETINGS FORM: "ABOUT IT" MASTER CARD
      // -------------------------------------------------------------
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 4.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("about_platform_card")
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Card Title Bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    tint = Color(0xFF1D4ED8),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Column {
                Text(
                  text = "About This Platform",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 17.5.sp
                  )
                )
                Text(
                  text = "Designed for high-net-worth clarity & security",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )
                )
              }
            }

            // 4K Quality Micro Tag
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF8FAFC),
              border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
              Text(
                text = "ULTRA HD",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  color = Slate700,
                  fontSize = 10.5.sp,
                  letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
              )
            }
          }

          HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

          // 4 Core Architecture Pillars with 4K Micro-Cards
          FourKFeaturePillar(
            icon = Icons.Default.Shield,
            iconColor = Color(0xFF0284C7),
            iconContainerBg = Color(0xFFE0F2FE),
            categoryPill = "360° COVERAGE",
            title = "Unified Insurance Shield",
            description = "Track Life, Health Mediclaim, and Vehicle IDV in real-time with zero-gap renewal alerts and emergency claim filing."
          )

          FourKFeaturePillar(
            icon = Icons.Default.TrendingUp,
            iconColor = Color(0xFF16A34A),
            iconContainerBg = Color(0xFFDCFCE7),
            categoryPill = "COMPOUNDING",
            title = "Wealth & SIP Growth Engine",
            description = "Live Net Worth aggregation across Mutual Funds, Gold, Real Estate, and EPF with 5-year compounding trajectory models."
          )

          FourKFeaturePillar(
            icon = Icons.Default.Sync,
            iconColor = Color(0xFFD97706),
            iconContainerBg = Color(0xFFFEF3C7),
            categoryPill = "CLOUD PORTAL",
            title = "Direct Google Sheets Integration",
            description = "Native bidirectional link directly into connected Google Sheets for real-time automated portfolio entry."
          )

          FourKFeaturePillar(
            icon = Icons.Default.PhoneInTalk,
            iconColor = Color(0xFF2563EB),
            iconContainerBg = Color(0xFFDBEAFE),
            categoryPill = "CONCIERGE",
            title = "24x7 Direct Advisory Hotlines",
            description = "Instant WhatsApp portfolio chat at 8123667686 and direct emergency advisory phone call at 8892722131."
          )

          // 4K Regulatory Compliance Jewel Banner
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = DeepGreenishBlack,
            border = BorderStroke(1.2.dp, EmeraldBorderHairline),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF0F3D26)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.VerifiedUser,
                  contentDescription = "Verified Advisory",
                  tint = EmeraldGlow,
                  modifier = Modifier.size(16.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "IRDAI Certified & Strict Privacy Framework",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldTextHighContrast,
                    fontSize = 12.sp
                  )
                )
                Text(
                  text = "License IRDAI/WMA/2024 • End-to-end encrypted financial portal",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = EmeraldSubText,
                    fontSize = 10.5.sp
                  )
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // -------------------------------------------------------------
      // 4K BOTTOM ACTION CONTROLS: Instant Launch & Timer
      // -------------------------------------------------------------
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onTimeout,
          colors = ButtonDefaults.buttonColors(
            containerColor = Slate900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .testTag("enter_dashboard_button"),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "Enter Dashboard",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.5.sp,
                letterSpacing = 0.3.sp
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Enter",
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "Auto-launching in ${remainingSeconds}s",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.5.sp
            )
          )
          Text(
            text = "• Tap button to enter immediately",
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate600,
              fontSize = 12.sp
            )
          )
        }
      }
    }
  }
}

@Composable
private fun FourKFeaturePillar(
  icon: ImageVector,
  iconColor: Color,
  iconContainerBg: Color,
  categoryPill: String,
  title: String,
  description: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = iconContainerBg,
      border = BorderStroke(1.dp, iconColor.copy(alpha = 0.2f)),
      modifier = Modifier.size(36.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = iconColor,
          modifier = Modifier.size(19.dp)
        )
      }
    }

    Column(modifier = Modifier.weight(1f)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Slate900,
            fontSize = 14.5.sp
          )
        )
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = iconContainerBg.copy(alpha = 0.6f)
        ) {
          Text(
            text = categoryPill,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Black,
              color = iconColor,
              fontSize = 10.5.sp,
              letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall.copy(
          color = Slate700,
          fontSize = 12.5.sp,
          lineHeight = 17.5.sp,
          fontWeight = FontWeight.Medium
        )
      )
    }
  }
}
