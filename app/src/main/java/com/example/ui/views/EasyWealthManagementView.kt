package com.example.ui.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.GoogleSheetsService
import com.example.model.*
import com.example.ui.components.WealthTrackerChart
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyWealthManagementView(
  formDraft: WealthDataEntry,
  onUpdateDraft: (WealthDataEntry) -> Unit,
  onSubmitToGoogleSheets: (customWebhookUrl: String?) -> Unit,
  isSubmitting: Boolean,
  lastSyncRecord: GoogleSheetSyncRecord?,
  showSuccessDialog: Boolean,
  onDismissSuccessDialog: () -> Unit,
  assets: List<WealthAssetItem>,
  projectionPoints: List<WealthProjectionPoint>,
  selectedYear: Int,
  onSelectYear: (Int) -> Unit,
  onBackToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val googleSheetsService = remember { GoogleSheetsService(context) }
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Data Entry Form, 1: Live Portfolio & Projections, 2: Google Sheets Setup
  var showWebhookConfigDialog by remember { mutableStateOf(false) }
  var customWebhookUrlInput by remember { mutableStateOf("") }

  // Form State variables initialized from draft
  var name by remember(formDraft) { mutableStateOf(formDraft.clientName) }
  var phone by remember(formDraft) { mutableStateOf(formDraft.clientPhone) }
  var email by remember(formDraft) { mutableStateOf(formDraft.clientEmail) }
  var city by remember(formDraft) { mutableStateOf(formDraft.city) }

  var monthlyIncomeStr by remember(formDraft) { mutableStateOf(if (formDraft.monthlyIncome > 0) formDraft.monthlyIncome.toLong().toString() else "") }
  var monthlyExpensesStr by remember(formDraft) { mutableStateOf(if (formDraft.monthlyExpenses > 0) formDraft.monthlyExpenses.toLong().toString() else "") }
  var monthlySipStr by remember(formDraft) { mutableStateOf(if (formDraft.monthlySip > 0) formDraft.monthlySip.toLong().toString() else "") }

  var mutualFundsStr by remember(formDraft) { mutableStateOf(if (formDraft.mutualFundsValue > 0) formDraft.mutualFundsValue.toLong().toString() else "") }
  var bankSavingsStr by remember(formDraft) { mutableStateOf(if (formDraft.bankSavings > 0) formDraft.bankSavings.toLong().toString() else "") }
  var goldStr by remember(formDraft) { mutableStateOf(if (formDraft.goldValue > 0) formDraft.goldValue.toLong().toString() else "") }
  var realEstateStr by remember(formDraft) { mutableStateOf(if (formDraft.realEstateValue > 0) formDraft.realEstateValue.toLong().toString() else "") }
  var retirementStr by remember(formDraft) { mutableStateOf(if (formDraft.retirementCorpus > 0) formDraft.retirementCorpus.toLong().toString() else "") }

  var termLifeStr by remember(formDraft) { mutableStateOf(if (formDraft.termLifeCover > 0) formDraft.termLifeCover.toLong().toString() else "") }
  var healthCoverStr by remember(formDraft) { mutableStateOf(if (formDraft.healthCover > 0) formDraft.healthCover.toLong().toString() else "") }
  var motorCoverStr by remember(formDraft) { mutableStateOf(if (formDraft.motorCover > 0) formDraft.motorCover.toLong().toString() else "") }

  var primaryGoal by remember(formDraft) { mutableStateOf(formDraft.primaryGoal) }
  var timelineYears by remember(formDraft) { mutableIntStateOf(formDraft.targetTimelineYears) }
  var advisoryNotes by remember(formDraft) { mutableStateOf(formDraft.advisoryNotes) }

  // Sync back state to draft
  fun syncToDraft() {
    val updated = formDraft.copy(
      clientName = name,
      clientPhone = phone,
      clientEmail = email.ifBlank { "advisor@galaxy.com" },
      city = city,
      monthlyIncome = monthlyIncomeStr.toDoubleOrNull() ?: 0.0,
      monthlyExpenses = monthlyExpensesStr.toDoubleOrNull() ?: 0.0,
      monthlySip = monthlySipStr.toDoubleOrNull() ?: 0.0,
      mutualFundsValue = mutualFundsStr.toDoubleOrNull() ?: 0.0,
      bankSavings = bankSavingsStr.toDoubleOrNull() ?: 0.0,
      goldValue = goldStr.toDoubleOrNull() ?: 0.0,
      realEstateValue = realEstateStr.toDoubleOrNull() ?: 0.0,
      retirementCorpus = retirementStr.toDoubleOrNull() ?: 0.0,
      termLifeCover = termLifeStr.toDoubleOrNull() ?: 0.0,
      healthCover = healthCoverStr.toDoubleOrNull() ?: 0.0,
      motorCover = motorCoverStr.toDoubleOrNull() ?: 0.0,
      primaryGoal = primaryGoal,
      targetTimelineYears = timelineYears,
      advisoryNotes = advisoryNotes
    )
    onUpdateDraft(updated)
  }

  val liveCalculatedNetWorth = (bankSavingsStr.toDoubleOrNull() ?: 0.0) +
      (mutualFundsStr.toDoubleOrNull() ?: 0.0) +
      (goldStr.toDoubleOrNull() ?: 0.0) +
      (realEstateStr.toDoubleOrNull() ?: 0.0) +
      (retirementStr.toDoubleOrNull() ?: 0.0)

  val liveCalculatedRiskCover = (termLifeStr.toDoubleOrNull() ?: 0.0) +
      (healthCoverStr.toDoubleOrNull() ?: 0.0) +
      (motorCoverStr.toDoubleOrNull() ?: 0.0)

  val incomeVal = monthlyIncomeStr.toDoubleOrNull() ?: 0.0
  val expensesVal = monthlyExpensesStr.toDoubleOrNull() ?: 0.0
  val liveSavingsRatio = if (incomeVal > 0) (((incomeVal - expensesVal) / incomeVal) * 100.0).coerceIn(0.0, 100.0) else 0.0

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .testTag("easy_wealth_management_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Header Bar
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Easy Wealth Management",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Slate900,
              fontSize = 24.sp
            )
          )
          Text(
            text = "Data Entry & Backend Google Sheets Sync",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Slate600,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Button(
          onClick = onBackToDashboard,
          colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = Slate800),
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Dashboard", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // 2. Google Sheets Live Connection Banner (Impressive visual card)
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0F172A), // Dark slate
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                  .size(36.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF0F9D58)), // Google Sheets Green
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.TableChart,
                  contentDescription = "Google Sheets",
                  tint = White,
                  modifier = Modifier.size(20.dp)
                )
              }
              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(Color(0xFF22C55E)) // Pulsing green
                  )
                  Text(
                    text = "Google Sheets Backend Active",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = Color(0xFF86EFAC),
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    )
                  )
                }
                Text(
                  text = "Connected Google Sheets Cloud",
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.5.sp
                  )
                )
              }
            }

            // Direct link to open Google Sheets
            OutlinedButton(
              onClick = {
                googleSheetsService.openGoogleSheetInBrowser(context)
              },
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF86EFAC)),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22C55E)),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("open_google_sheets_btn")
            ) {
              Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Open Sheet", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Divider(color = Color(0xFF1E293B))
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (lastSyncRecord != null) "Last Synced: ${lastSyncRecord.timestamp}" else "Status: Ready for user data submission",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF94A3B8),
                fontSize = 12.sp
              )
            )

            Text(
              text = "Configure Webhook",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF38BDF8),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              ),
              modifier = Modifier
                .clickable { showWebhookConfigDialog = true }
                .padding(4.dp)
            )
          }
        }
      }
    }

    // 3. Navigation Sub-Tabs
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = White,
        contentColor = BluePrimary,
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .border(1.dp, Slate200, RoundedCornerShape(12.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Data Entry Form", fontWeight = FontWeight.Bold, fontSize = 13.5.sp) },
          icon = { Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp)) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Live Portfolio", fontWeight = FontWeight.Bold, fontSize = 13.5.sp) },
          icon = { Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp)) }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("Sheet Integration", fontWeight = FontWeight.Bold, fontSize = 13.5.sp) },
          icon = { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp)) }
        )
      }
    }

    // Tab 0: DATA ENTRY FORM
    if (selectedTab == 0) {
      // Real-Time Live Calculation Summary Card
      item {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFF0FDF4), // Light emerald
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "LIVE CALCULATION PREVIEW",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = EmeraldDark,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = "Updates dynamically as you type",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Slate600,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Net worth
              Column(
                modifier = Modifier
                  .weight(1f)
                  .background(White, RoundedCornerShape(10.dp))
                  .padding(10.dp)
              ) {
                Text(
                  "Total Net Worth",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate700,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                )
                Text(
                  text = formatCurrencyInr(liveCalculatedNetWorth),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark,
                    fontSize = 17.sp
                  )
                )
              }

              // Risk cover
              Column(
                modifier = Modifier
                  .weight(1f)
                  .background(White, RoundedCornerShape(10.dp))
                  .padding(10.dp)
              ) {
                Text(
                  "Total Protection Cover",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate700,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                )
                Text(
                  text = formatCurrencyInr(liveCalculatedRiskCover),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlueDeep,
                    fontSize = 17.sp
                  )
                )
              }
            }
          }
        }
      }

      // Card 1: Client Information
      item {
        FormSectionCard(
          title = "1. Client & Contact Information",
          icon = Icons.Default.Person,
          iconTint = BluePrimary
        ) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it; syncToDraft() },
            label = { Text("Client Full Name") },
            placeholder = { Text("e.g. Client Name") },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Slate400) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_client_name"),
            shape = RoundedCornerShape(10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = phone,
              onValueChange = { phone = it; syncToDraft() },
              label = { Text("Phone / WhatsApp") },
              placeholder = { Text("+91 81236 67686") },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Slate400) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_client_phone"),
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = city,
              onValueChange = { city = it; syncToDraft() },
              label = { Text("City / Location") },
              placeholder = { Text("e.g. Bangalore") },
              leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Slate400) },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_client_city"),
              shape = RoundedCornerShape(10.dp)
            )
          }

          OutlinedTextField(
            value = email,
            onValueChange = { email = it; syncToDraft() },
            label = { Text("Notification Email (Google Sheet Owner)") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Slate400) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_client_email"),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      // Card 2: Income & Cash Flow
      item {
        FormSectionCard(
          title = "2. Monthly Income & Cash Flow",
          icon = Icons.Default.CurrencyRupee,
          iconTint = EmeraldPrimary
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = monthlyIncomeStr,
              onValueChange = { monthlyIncomeStr = it; syncToDraft() },
              label = { Text("Monthly Income (₹)") },
              placeholder = { Text("e.g. 150000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_monthly_income"),
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = monthlyExpensesStr,
              onValueChange = { monthlyExpensesStr = it; syncToDraft() },
              label = { Text("Monthly Expenses (₹)") },
              placeholder = { Text("e.g. 60000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_monthly_expenses"),
              shape = RoundedCornerShape(10.dp)
            )
          }

          OutlinedTextField(
            value = monthlySipStr,
            onValueChange = { monthlySipStr = it; syncToDraft() },
            label = { Text("Monthly Mutual Fund SIP Target (₹)") },
            placeholder = { Text("e.g. 25000") },
            leadingIcon = { Icon(Icons.Default.Autorenew, contentDescription = null, tint = EmeraldPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_monthly_sip"),
            shape = RoundedCornerShape(10.dp)
          )

          if (liveSavingsRatio > 0) {
            Text(
              text = "Calculated Monthly Savings Rate: ${"%.1f".format(liveSavingsRatio)}%",
              style = MaterialTheme.typography.labelSmall.copy(
                color = EmeraldDark,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }

      // Card 3: Wealth & Asset Portfolio
      item {
        FormSectionCard(
          title = "3. Wealth & Asset Portfolio (₹)",
          icon = Icons.Default.AccountBalance,
          iconTint = PurpleAccent
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = mutualFundsStr,
              onValueChange = { mutualFundsStr = it; syncToDraft() },
              label = { Text("Mutual Funds / Equity") },
              placeholder = { Text("e.g. 2500000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_mutual_funds"),
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = bankSavingsStr,
              onValueChange = { bankSavingsStr = it; syncToDraft() },
              label = { Text("Bank Savings / FDs") },
              placeholder = { Text("e.g. 800000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_bank_savings"),
              shape = RoundedCornerShape(10.dp)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = goldStr,
              onValueChange = { goldStr = it; syncToDraft() },
              label = { Text("Gold & SGBs (₹)") },
              placeholder = { Text("e.g. 500000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_gold"),
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = realEstateStr,
              onValueChange = { realEstateStr = it; syncToDraft() },
              label = { Text("Real Estate (₹)") },
              placeholder = { Text("e.g. 6000000") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_real_estate"),
              shape = RoundedCornerShape(10.dp)
            )
          }

          OutlinedTextField(
            value = retirementStr,
            onValueChange = { retirementStr = it; syncToDraft() },
            label = { Text("EPF / PPF / NPS Corpus (₹)") },
            placeholder = { Text("e.g. 1200000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_retirement"),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      // Card 4: Protection & Risk Coverage
      item {
        FormSectionCard(
          title = "4. Insurance & Protection Covers (₹)",
          icon = Icons.Default.Security,
          iconTint = BluePrimary
        ) {
          OutlinedTextField(
            value = termLifeStr,
            onValueChange = { termLifeStr = it; syncToDraft() },
            label = { Text("Term Life Insurance Cover (₹)") },
            placeholder = { Text("e.g. 15000000 (1.5 Cr)") },
            leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BluePrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_term_life"),
            shape = RoundedCornerShape(10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = healthCoverStr,
              onValueChange = { healthCoverStr = it; syncToDraft() },
              label = { Text("Health Cover (₹)") },
              placeholder = { Text("e.g. 2500000") },
              leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null, tint = EmeraldPrimary) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_health_cover"),
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = motorCoverStr,
              onValueChange = { motorCoverStr = it; syncToDraft() },
              label = { Text("Vehicle IDV (₹)") },
              placeholder = { Text("e.g. 1000000") },
              leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = AmberWarning) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_motor_cover"),
              shape = RoundedCornerShape(10.dp)
            )
          }
        }
      }

      // Card 5: Goals & Notes
      item {
        FormSectionCard(
          title = "5. Goals & Advisory Notes",
          icon = Icons.Default.Flag,
          iconTint = AmberWarning
        ) {
          OutlinedTextField(
            value = primaryGoal,
            onValueChange = { primaryGoal = it; syncToDraft() },
            label = { Text("Primary Financial Milestone") },
            placeholder = { Text("e.g. Early Retirement Freedom & Child Education") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_primary_goal"),
            shape = RoundedCornerShape(10.dp)
          )

          Text("Target Horizon in Years: $timelineYears Years", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Slate700))
          Slider(
            value = timelineYears.toFloat(),
            onValueChange = { timelineYears = it.toInt(); syncToDraft() },
            valueRange = 1f..30f,
            steps = 28,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = advisoryNotes,
            onValueChange = { advisoryNotes = it; syncToDraft() },
            label = { Text("Advisory Notes / Custom Instructions") },
            placeholder = { Text("e.g. High tax bracket, focus on equity compounding & zero-dep motor renewal.") },
            modifier = Modifier.fillMaxWidth().height(90.dp).testTag("input_advisory_notes"),
            shape = RoundedCornerShape(10.dp),
            maxLines = 3
          )
        }
      }

      // SUBMIT TO GOOGLE SHEETS BUTTON (Centerpiece Action)
      item {
        Spacer(modifier = Modifier.height(4.dp))
        Button(
          onClick = {
            syncToDraft()
            onSubmitToGoogleSheets(customWebhookUrlInput.takeIf { it.isNotBlank() })
          },
          enabled = !isSubmitting,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F9D58), // Google Sheets Green
            contentColor = White
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .testTag("submit_wealth_data_button")
        ) {
          if (isSubmitting) {
            CircularProgressIndicator(
              color = White,
              modifier = Modifier.size(22.dp),
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Syncing to Google Sheets Cloud...", fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Submit & Link to Google Sheets",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    // Tab 1: LIVE PORTFOLIO & PROJECTIONS
    if (selectedTab == 1) {
      item {
        WealthTrackerChart(
          projectionPoints = projectionPoints,
          selectedYear = selectedYear,
          onSelectYear = onSelectYear,
          onExploreFundsClick = { selectedTab = 0 }
        )
      }

      item {
        Text(
          text = "CALCULATED PORTFOLIO ASSETS",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Slate500,
            letterSpacing = 0.5.sp
          )
        )
      }

      if (assets.isEmpty()) {
        item {
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Slate400, modifier = Modifier.size(40.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("No Assets Logged Yet", fontWeight = FontWeight.Bold, color = Slate800)
              Text("Enter your data in the form tab and submit to view live asset breakdown.", color = Slate500, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(12.dp))
              Button(
                onClick = { selectedTab = 0 },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
              ) {
                Text("Go to Data Entry Form")
              }
            }
          }
        }
      } else {
        items(assets) { asset ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(asset.fundName, fontWeight = FontWeight.Bold, color = Slate900, fontSize = 14.sp)
                Text(asset.units, color = Slate500, fontSize = 12.sp)
              }
              Column(horizontalAlignment = Alignment.End) {
                Text(asset.currentValueText, fontWeight = FontWeight.Bold, color = EmeraldDark, fontSize = 14.sp)
                Text(asset.xirr, color = EmeraldPrimary, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // Tab 2: GOOGLE SHEETS INTEGRATION & SCRIPT CODE
    if (selectedTab == 2) {
      item {
        GoogleSheetsIntegrationGuideCard(
          googleSheetsService = googleSheetsService,
          onOpenSheet = { googleSheetsService.openGoogleSheetInBrowser(context) }
        )
      }
    }
  }

  // SUCCESS DIALOG AFTER SUBMISSION
  if (showSuccessDialog && lastSyncRecord != null) {
    Dialog(onDismissRequest = onDismissSuccessDialog) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = White,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth().testTag("sync_success_dialog")
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Success",
              tint = Color(0xFF16A34A),
              modifier = Modifier.size(32.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Data Linked to Google Sheets!",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Slate900,
              fontSize = 18.sp
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Your submission has been captured and synchronized with your Google Sheets cloud database. All metrics have been loaded onto your live dashboard.",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Slate600,
              fontSize = 12.sp,
              lineHeight = 17.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Receipt details
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Slate50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Sync Reference:", fontSize = 11.sp, color = Slate500)
                Text(lastSyncRecord.syncId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Account Target:", fontSize = 11.sp, color = Slate500)
                Text("Connected Google Sheets", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Net Worth Recorded:", fontSize = 11.sp, color = Slate500)
                Text(formatCurrencyInr(lastSyncRecord.totalNetWorth), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Protection Cover:", fontSize = 11.sp, color = Slate500)
                Text(formatCurrencyInr(lastSyncRecord.totalRiskCover), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueDeep)
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Action 1: Open Google Sheets
          Button(
            onClick = {
              googleSheetsService.openGoogleSheetInBrowser(context)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF0F9D58),
              contentColor = White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(46.dp)
          ) {
            Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Open Connected Google Sheet", fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Action 2: Go to Dashboard
          OutlinedButton(
            onClick = {
              onDismissSuccessDialog()
              onBackToDashboard()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(46.dp)
          ) {
            Text("View Updated Dashboard", fontWeight = FontWeight.Bold, color = Slate800)
          }
        }
      }
    }
  }

  // Webhook Configuration Dialog
  if (showWebhookConfigDialog) {
    AlertDialog(
      onDismissRequest = { showWebhookConfigDialog = false },
      title = { Text("Google Sheets Webhook Configuration", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Target Google Sheets Cloud Database",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate700
          )
          Text(
            text = "If you have a Google Apps Script Web App URL for this spreadsheet, enter it below to receive live automated row inserts directly:",
            fontSize = 11.sp,
            color = Slate500
          )
          OutlinedTextField(
            value = customWebhookUrlInput,
            onValueChange = { customWebhookUrlInput = it },
            placeholder = { Text("https://script.google.com/macros/s/.../exec") },
            label = { Text("Apps Script Webhook URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showWebhookConfigDialog = false }
        ) {
          Text("Save & Close")
        }
      }
    )
  }
}

@Composable
fun FormSectionCard(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  content: @Composable ColumnScope.() -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    shadowElevation = 1.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(iconTint.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
        }
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Slate900,
            fontSize = 14.sp
          )
        )
      }
      Divider(color = Slate100)
      content()
    }
  }
}

@Composable
fun GoogleSheetsIntegrationGuideCard(
  googleSheetsService: GoogleSheetsService,
  onOpenSheet: () -> Unit
) {
  val context = LocalContext.current
  val scriptCode = remember { googleSheetsService.getGoogleAppsScriptTemplate() }

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Text(
        text = "Direct Google Sheets Cloud Setup",
        fontWeight = FontWeight.Bold,
        fontSize = 16.5.sp,
        color = Slate900
      )

      Text(
        text = "This app is configured to submit wealth data directly into Google Sheets. You can open the sheet directly or deploy the 1-minute Apps Script code to append new client entries automatically:",
        fontSize = 13.sp,
        color = Slate700,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.5.sp
      )

      Button(
        onClick = onOpenSheet,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F9D58)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Open Connected Google Sheet", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text("Google Apps Script Backend Code:", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = Slate900)

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = scriptCode,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFE2E8F0),
            fontSize = 11.5.sp,
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Apps Script", scriptCode)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Copied Apps Script code to clipboard!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Copy Apps Script to Clipboard", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

fun formatCurrencyInr(amount: Double): String {
  if (amount <= 0.0) return "₹0"
  return when {
    amount >= 10000000.0 -> "₹${"%.2f".format(amount / 10000000.0)} Cr"
    amount >= 100000.0 -> "₹${"%.2f".format(amount / 100000.0)} Lakhs"
    else -> "₹${"%,d".format(amount.toLong())}"
  }
}
