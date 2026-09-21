package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

private const val WHATSAPP_NUMBER = "8123667686"
private const val CALL_NUMBER = "8892722131"

fun openWhatsAppDirect(
  context: Context,
  phoneNumber: String = WHATSAPP_NUMBER,
  customMessage: String? = null
) {
  try {
    val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
    val internationalNumber = if (cleanNumber.length == 10) "91$cleanNumber" else cleanNumber
    val message = customMessage ?: "Hello, I would like to connect regarding my Fintech portfolio and services."
    val uri = Uri.parse("https://wa.me/$internationalNumber?text=" + Uri.encode(message))
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open WhatsApp: ${e.localizedMessage ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
  }
}

fun makeDirectCall(context: Context, phoneNumber: String = CALL_NUMBER) {
  try {
    val intent = Intent(Intent.ACTION_DIAL).apply {
      data = Uri.parse("tel:$phoneNumber")
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Unable to open phone dialer", Toast.LENGTH_SHORT).show()
  }
}

@Composable
fun DirectContactSection(
  modifier: Modifier = Modifier,
  whatsAppNumber: String = WHATSAPP_NUMBER,
  callNumber: String = CALL_NUMBER
) {
  val context = LocalContext.current
  val whatsAppGreen = Color(0xFF25D366)
  val whatsAppDarkGreen = Color(0xFF075E54)
  val whatsAppLightGreen = Color(0xFFE8F8EE)
  val callBlue = Color(0xFF1E40AF)
  val callLightBlue = Color(0xFFEFF6FF)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("direct_contact_section_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header row with active status indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Direct Advisor Connect",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Slate900,
              fontSize = 18.sp
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Instant 1-on-1 support for wealth & policies",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Slate600,
              fontSize = 13.sp
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFDCFCE7),
          border = BorderStroke(1.dp, Color(0xFF86EFAC))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF16A34A))
            )
            Text(
              text = "Live Now",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534),
                fontSize = 11.5.sp
              )
            )
          }
        }
      }

      // 1. Direct WhatsApp Link Card with WhatsApp Logo
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { openWhatsAppDirect(context, whatsAppNumber) }
          .testTag("whatsapp_direct_card"),
        shape = RoundedCornerShape(16.dp),
        color = whatsAppLightGreen,
        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Authentic WhatsApp Logo Box
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(Color.White)
              .padding(4.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_whatsapp),
              contentDescription = "WhatsApp Logo",
              tint = Color.Unspecified,
              modifier = Modifier.size(38.dp)
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "WhatsApp Chat",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = whatsAppDarkGreen,
                  fontSize = 16.5.sp
                )
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = whatsAppGreen
              ) {
                Text(
                  text = "DIRECT LINK",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 10.5.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "+91 $whatsAppNumber",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 14.5.sp
              )
            )
            Text(
              text = "Tap to chat instantly on WhatsApp",
              style = MaterialTheme.typography.bodySmall.copy(
                color = Slate700,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }

          FilledIconButton(
            onClick = { openWhatsAppDirect(context, whatsAppNumber) },
            colors = IconButtonDefaults.filledIconButtonColors(
              containerColor = whatsAppGreen,
              contentColor = Color.White
            ),
            modifier = Modifier
              .size(40.dp)
              .testTag("whatsapp_direct_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open WhatsApp",
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // 2. Direct Call Card
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { makeDirectCall(context, callNumber) }
          .testTag("call_direct_card"),
        shape = RoundedCornerShape(16.dp),
        color = callLightBlue,
        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Phone Dial Icon Box
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(callBlue),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Call,
              contentDescription = "Phone Call",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "Call Dedicated Line",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = callBlue,
                  fontSize = 16.5.sp
                )
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFDBEAFE)
              ) {
                Text(
                  text = "DIRECT DIAL",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = callBlue,
                    fontSize = 10.5.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "+91 $callNumber",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 14.5.sp
              )
            )
            Text(
              text = "Direct call for portfolio queries & assistance",
              style = MaterialTheme.typography.bodySmall.copy(
                color = Slate700,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }

          FilledIconButton(
            onClick = { makeDirectCall(context, callNumber) },
            colors = IconButtonDefaults.filledIconButtonColors(
              containerColor = callBlue,
              contentColor = Color.White
            ),
            modifier = Modifier
              .size(40.dp)
              .testTag("call_direct_button")
          ) {
            Icon(
              imageVector = Icons.Default.Phone,
              contentDescription = "Direct Call",
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
