package com.example

import com.example.data.GoogleSheetsService
import com.example.data.LuckyLifeRepository
import com.example.model.GoogleSheetSyncRecord
import com.example.model.WealthDataEntry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

  @Test
  fun initialData_isAllZeros() {
    val repository = LuckyLifeRepository()
    val kpi = repository.kpiSummary.value
    assertEquals(0.0, kpi.totalNetWorth, 0.001)
    assertEquals(0.0, kpi.totalRiskCover, 0.001)
    assertEquals(0.0, kpi.activeSipMonthly, 0.001)
    assertEquals(0, kpi.activeSipCount)
    assertTrue(repository.policies.value.isEmpty())
    assertTrue(repository.wealthAssets.value.isEmpty())
    assertTrue(repository.familyGoals.value.isEmpty())
    assertEquals("Prasanna kumar B k", repository.userProfile.value.advisorName)
    assertEquals("Business Development Manager in GALAXY", repository.userProfile.value.advisorTitle)
  }

  @Test
  fun wealthDataEntry_calculationsAreAccurate() {
    val entry = WealthDataEntry(
      clientName = "Sample Client",
      clientPhone = "8123667686",
      clientEmail = "client@galaxy.com",
      city = "Bangalore",
      monthlyIncome = 200000.0,
      monthlyExpenses = 75000.0,
      bankSavings = 500000.0,
      mutualFundsValue = 2500000.0,
      monthlySip = 35000.0,
      retirementCorpus = 1000000.0,
      realEstateValue = 5000000.0,
      goldValue = 1000000.0,
      termLifeCover = 20000000.0, // 2 Cr
      healthCover = 2500000.0,    // 25 Lakhs
      motorCover = 1200000.0      // 12 Lakhs
    )

    // Net worth = 5L + 25L + 10L + 50L + 10L = 1,00,00,000 (1 Cr)
    assertEquals(10000000.0, entry.calculatedNetWorth, 0.001)
    // Risk cover = 2 Cr + 25L + 12L = 2,37,00,000
    assertEquals(23700000.0, entry.calculatedTotalRiskCover, 0.001)
  }

  @Test
  fun applyWealthDataSubmission_updatesRepositoryLive() {
    val repository = LuckyLifeRepository()
    val entry = WealthDataEntry(
      clientName = "Sample Client",
      clientPhone = "8123667686",
      clientEmail = "client@galaxy.com",
      city = "Bangalore",
      monthlyIncome = 200000.0,
      monthlyExpenses = 75000.0,
      bankSavings = 500000.0,
      mutualFundsValue = 2500000.0,
      monthlySip = 35000.0,
      retirementCorpus = 1000000.0,
      realEstateValue = 5000000.0,
      goldValue = 1000000.0,
      termLifeCover = 20000000.0,
      healthCover = 2500000.0,
      motorCover = 1200000.0
    )
    val syncRecord = GoogleSheetSyncRecord(
      syncId = "SYNC-TEST-001",
      timestamp = "2026-09-19 16:00:00",
      sheetOwnerEmail = "sheet@galaxy.com",
      sheetUrl = "https://docs.google.com/spreadsheets/u/0/",
      clientName = entry.clientName,
      totalNetWorth = entry.calculatedNetWorth,
      totalRiskCover = entry.calculatedTotalRiskCover,
      isSuccess = true,
      statusMessage = "Data linked to Google Sheets"
    )

    repository.applyWealthDataSubmission(entry, syncRecord)

    assertEquals(10000000.0, repository.kpiSummary.value.totalNetWorth, 0.001)
    assertEquals(23700000.0, repository.kpiSummary.value.totalRiskCover, 0.001)
    assertEquals("Sample Client", repository.userProfile.value.name)
    assertEquals("client@galaxy.com", repository.userProfile.value.email)
    assertEquals(3, repository.policies.value.size)
    assertFalse(repository.wealthAssets.value.isEmpty())
  }

  @Test
  fun googleSheetsService_buildsValidJson() {
    val service = GoogleSheetsService()
    val entry = WealthDataEntry(
      clientName = "Sample Client",
      clientPhone = "8123667686",
      clientEmail = "client@galaxy.com",
      monthlyIncome = 150000.0,
      bankSavings = 400000.0,
      mutualFundsValue = 2000000.0
    )
    val json = service.buildWealthDataJson(entry, "SYNC-12345")
    assertTrue(json.contains("client@galaxy.com"))
    assertTrue(json.contains("Sample Client"))
    assertTrue(json.contains("SYNC-12345"))
  }
}
