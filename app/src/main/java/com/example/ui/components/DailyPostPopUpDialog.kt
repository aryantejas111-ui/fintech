package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.DailyPostItem
import com.example.ui.theme.*

@Composable
fun DailyPostPopUpDialog(
  post: DailyPostItem,
  onDismiss: () -> Unit,
  onWhatsAppClick: (Context, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = true,
      usePlatformDefaultWidth = false
    )
  ) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      shadowElevation = 8.dp,
      modifier = modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 24.dp)
        .testTag("daily_post_popup_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // TOP BAR: Greenish-Black IRDAI Certified Tag + Close Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF04160E),
            border = BorderStroke(1.dp, Color(0xFF166534))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF22C55E)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color(0xFF04160E),
                  modifier = Modifier.size(7.dp)
                )
              }
              Text(
                text = "IRDAI DAILY BROADCAST",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  fontSize = 11.sp,
                  color = Color(0xFFDCFCE7),
                  letterSpacing = 0.6.sp
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Slate700,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Category & Date pill
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFEFF6FF),
            border = BorderStroke(1.dp, Color(0xFFBFDBFE))
          ) {
            Text(
              text = post.categoryTag,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D4ED8),
                fontSize = 12.5.sp
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }

          Text(
            text = post.dateFormatted,
            style = MaterialTheme.typography.labelSmall.copy(
              color = Slate700,
              fontSize = 12.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }

        // Post Title
        Text(
          text = post.title,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = Slate900,
            fontSize = 19.5.sp,
            lineHeight = 26.sp
          )
        )

        // Author Signature Row
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Slate50,
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFF0F172A),
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.AdminPanelSettings,
                  contentDescription = "Admin",
                  tint = Color(0xFF4ADE80),
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = post.authorName,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Slate900,
                  fontSize = 13.5.sp
                )
              )
              Text(
                text = "${post.authorRole} • ${post.authorEmail}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate700,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }
        }

        // Main Content Text
        Text(
          text = post.content,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = Slate800,
            fontSize = 14.5.sp,
            lineHeight = 22.sp
          )
        )

        // Important Takeaway Box (if present)
        if (post.importantTakeaway.isNotBlank()) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0FDF4),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Takeaway",
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(18.dp)
              )
              Column {
                Text(
                  text = "KEY TAKEAWAY FOR INVESTORS",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF166534),
                    fontSize = 11.5.sp,
                    letterSpacing = 0.5.sp
                  )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = post.importantTakeaway,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF14532D),
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp
                  )
                )
              }
            }
          }
        }

        HorizontalDivider(color = Color(0xFFF1F5F9))

        // Action Buttons
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                  Intent.EXTRA_SUBJECT,
                  "Fintech IRDAI Daily Post: ${post.title}"
                )
                putExtra(
                  Intent.EXTRA_TEXT,
                  "📢 *${post.title}*\n\n${post.content}\n\n— Broadcasted by ${post.authorName} (${post.authorRole})"
                )
              }
              context.startActivity(Intent.createChooser(shareIntent, "Share Daily Bulletin"))
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF25D366),
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("popup_share_whatsapp_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Share with Family / Network",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.5.sp
              )
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = {
                onWhatsAppClick(context, "Hello Prasanna kumar B k, I have a question regarding the Daily Post: ${post.title}")
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF0284C7)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Ask Advisor",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.5.sp,
                  color = Color(0xFF0284C7)
                )
              )
            }

            Button(
              onClick = onDismiss,
              colors = ButtonDefaults.buttonColors(
                containerColor = Slate900,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("popup_acknowledge_button")
            ) {
              Text(
                text = "Acknowledge",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.5.sp
                )
              )
            }
          }
        }
      }
    }
  }
}
