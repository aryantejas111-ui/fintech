package com.example.ui.views

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyPostItem
import com.example.ui.theme.*

private data class PostTemplate(
  val label: String,
  val title: String,
  val category: String,
  val content: String,
  val takeaway: String
)

private val SAMPLE_TEMPLATES = listOf(
  PostTemplate(
    label = "⚡ Cashless Mediclaim",
    title = "IRDAI Directive: 1-Hour Cashless Health Authorization Mandated",
    category = "Insurance Advisory",
    content = "Under the updated IRDAI operational master circular, all network hospitals and insurance TPAs must grant cashless preliminary authorization within 60 minutes of discharge or admission request. Ensure your family health mediclaim policy details are kept ready.",
    takeaway = "Health cashless authorizations now legally guaranteed within 1 hour."
  ),
  PostTemplate(
    label = "📈 Market All-Time High",
    title = "Daily Market Pulse: Sensex Gains & Systematic SIP Strategy",
    category = "Market Pulse",
    content = "Equity markets recorded solid gains today led by banking and IT index heavyweights. For long-term wealth creators, continuing regular monthly SIP mandates without attempting to time market peaks produces superior risk-adjusted compounding.",
    takeaway = "Stay disciplined with systematic monthly SIP investments regardless of short-term volatility."
  ),
  PostTemplate(
    label = "🛡️ 20X Life Cover",
    title = "Wealth Safety Rule: Ensure Pure Term Cover Equals 20X Annual Income",
    category = "Wealth Tip",
    content = "A financial health check reveals many households remain under-insured with endowment plans. Pure term life plans provide comprehensive family income replacement at a fraction of the premium. Calculate your coverage in My Protection tab.",
    takeaway = "Minimum term life insurance coverage should equal 15 to 20 times your annual household earnings."
  ),
  PostTemplate(
    label = "⚖️ Tax & SGB Advisory",
    title = "Tax-Saving & Gold Strategy: Smart Asset Allocation Review",
    category = "Regulatory Notice",
    content = "Tax-saving season approaches: Allocating up to ₹1.5 Lakh in ELSS mutual funds under Section 80C offers the shortest 3-year lock-in while building equity wealth. Combine with 10% gold allocation for ideal portfolio stability.",
    takeaway = "ELSS tax saving offers superior growth potential with the lowest lock-in among 80C instruments."
  )
)

private val CATEGORIES = listOf(
  "Market Pulse",
  "Insurance Advisory",
  "Wealth Tip",
  "Regulatory Notice",
  "Urgent Broadcast"
)

@Composable
fun AdminDailyPostView(
  pastPosts: List<DailyPostItem>,
  onBroadcastPost: (Context, String, String, String, Boolean, String) -> Unit,
  onPreviewPost: (DailyPostItem) -> Unit,
  onDeletePost: (String) -> Unit,
  onBackToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  var title by remember { mutableStateOf("IRDAI Update: 100% Cashless Health Settlement Rule & Market Surge") }
  var category by remember { mutableStateOf("Insurance Advisory") }
  var content by remember {
    mutableStateOf(
      "Under the latest IRDAI advisory directives, insurance companies must process cashless health authorizations within 1 hour. In addition, equity markets showed strong resilience today with key flexi-cap funds touching all-time highs. Ensure your health sum assured and active monthly SIP mandates are reviewed."
    )
  }
  var takeaway by remember {
    mutableStateOf("Health cashless authorizations now expedited to 1 hour across network hospitals.")
  }
  var isHighPriority by remember { mutableStateOf(true) }
  var broadcastSuccessMessage by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          IconButton(
            onClick = onBackToDashboard,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Slate800
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Admin Daily Post Studio",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Slate900,
                fontSize = 16.sp
              )
            )
            Text(
              text = "Broadcast Daily Bulletins & Instant Phone Pop-Ups",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Slate500,
                fontSize = 11.sp
              )
            )
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF04160E),
            border = BorderStroke(1.dp, Color(0xFF166534))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF22C55E))
              )
              Text(
                text = "ADMIN",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  fontSize = 11.sp,
                  color = Color(0xFFDCFCE7)
                )
              )
            }
          }
        }
      }
    },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Slate50),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Admin Verification Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFF1E293B),
              modifier = Modifier.size(44.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Campaign,
                  contentDescription = "Broadcast",
                  tint = Color(0xFF38BDF8),
                  modifier = Modifier.size(24.dp)
                )
              }
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Live Broadcast Transmitter",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              )
              Text(
                text = "Authorized Admin: Prasanna kumar B k (Business Development Manager in GALAXY)",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 13.sp
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Posts published here instantly pop up on user devices and appear in their notification tray.",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = Color(0xFFCBD5E1),
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }

      // Success notification feedback banner
      if (broadcastSuccessMessage != null) {
        item {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0FDF4),
            border = BorderStroke(1.dp, Color(0xFF86EFAC)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(20.dp)
              )
              Text(
                text = broadcastSuccessMessage ?: "",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF14532D),
                  fontSize = 12.sp
                ),
                modifier = Modifier.weight(1f)
              )
              IconButton(
                onClick = { broadcastSuccessMessage = null },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Dismiss",
                  tint = Color(0xFF16A34A),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }

      // 2. Quick Topic Templates (1-Tap Fill)
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "⚡ Quick Topic Templates (1-Tap Pre-fill)",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Slate800,
              fontSize = 13.sp
            )
          )

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(SAMPLE_TEMPLATES) { tmpl ->
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                onClick = {
                  title = tmpl.title
                  category = tmpl.category
                  content = tmpl.content
                  takeaway = tmpl.takeaway
                }
              ) {
                Text(
                  text = tmpl.label,
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700,
                    fontSize = 12.sp
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      }

      // 3. Compose Daily Post Form
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Text(
              text = "✍️ Create Today's Daily Post",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 15.sp
              )
            )

            // Post Title Input
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Post Headline / Title") },
              placeholder = { Text("e.g., IRDAI Directive: 100% Cashless Health Settlement") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_post_title_input"),
              shape = RoundedCornerShape(10.dp),
              singleLine = false,
              maxLines = 2
            )

            // Category Chips
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = "Select Category",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = Slate700,
                  fontSize = 12.sp
                )
              )
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                items(CATEGORIES) { cat ->
                  val isSelected = category == cat
                  FilterChip(
                    selected = isSelected,
                    onClick = { category = cat },
                    label = {
                      Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                      )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = Color(0xFF0284C7),
                      selectedLabelColor = Color.White
                    )
                  )
                }
              }
            }

            // Content Text Area
            OutlinedTextField(
              value = content,
              onValueChange = { content = it },
              label = { Text("Daily Bulletin Content") },
              placeholder = { Text("Write the detailed update, market analysis, or insurance rule for users...") },
              modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .testTag("admin_post_content_input"),
              shape = RoundedCornerShape(10.dp)
            )

            // Key Takeaway Input
            OutlinedTextField(
              value = takeaway,
              onValueChange = { takeaway = it },
              label = { Text("Key Takeaway (Summary Pill)") },
              placeholder = { Text("One crisp sentence explaining what the user should do") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_post_takeaway_input"),
              shape = RoundedCornerShape(10.dp),
              singleLine = true
            )

            // High Priority Switch
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Slate50,
              border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "High-Priority Heads-Up Pop-Up",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = Slate900,
                      fontSize = 13.5.sp
                    )
                  )
                  Text(
                    text = "Triggers audible heads-up notification and in-app modal on user devices",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = Slate700,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium
                    )
                  )
                }
                Switch(
                  checked = isHighPriority,
                  onCheckedChange = { isHighPriority = it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF16A34A)
                  )
                )
              }
            }

            // Broadcast & Preview Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = {
                  val previewItem = DailyPostItem(
                    id = "preview_${System.currentTimeMillis()}",
                    title = title,
                    content = content,
                    categoryTag = category,
                    authorName = "Prasanna kumar B k",
                    authorRole = "Business Development Manager in GALAXY",
                    authorEmail = "",
                    timestampMillis = System.currentTimeMillis(),
                    dateFormatted = "Live Preview",
                    isHighPriority = isHighPriority,
                    broadcastCount = 184,
                    importantTakeaway = takeaway
                  )
                  onPreviewPost(previewItem)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(0.42f)
                  .height(48.dp)
                  .testTag("admin_preview_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Visibility,
                  contentDescription = "Preview",
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Preview", fontSize = 12.sp)
              }

              Button(
                onClick = {
                  if (title.isNotBlank() && content.isNotBlank()) {
                    onBroadcastPost(
                      context,
                      title,
                      content,
                      category,
                      isHighPriority,
                      takeaway
                    )
                    broadcastSuccessMessage = "Post broadcasted! Android heads-up notification & in-app pop-up dispatched."
                  }
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF0284C7),
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(0.58f)
                  .height(48.dp)
                  .testTag("admin_publish_broadcast_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Send,
                  contentDescription = "Send",
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Publish & Pop Up",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                )
              }
            }
          }
        }
      }

      // 4. Past Broadcast History Section
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "📜 Past Daily Broadcasts (${pastPosts.size})",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Slate800,
              fontSize = 14.sp
            )
          )
        }
      }

      items(pastPosts, key = { it.id }) { post ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
              ) {
                Text(
                  text = post.categoryTag,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8),
                    fontSize = 11.5.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }

              Text(
                text = post.dateFormatted,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }

            Text(
              text = post.title,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Slate900,
                fontSize = 14.5.sp
              )
            )

            Text(
              text = post.content,
              style = MaterialTheme.typography.bodySmall.copy(
                color = Slate700,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium
              ),
              maxLines = 2
            )

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.PhoneAndroid,
                  contentDescription = null,
                  tint = Color(0xFF16A34A),
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "${post.broadcastCount} Device Pop-ups Delivered",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF166534),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                  )
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(
                  onClick = { onPreviewPost(post) },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text("View Pop-Up", fontSize = 11.sp, color = Color(0xFF0284C7))
                }
                IconButton(
                  onClick = { onDeletePost(post.id) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = RedDanger,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
