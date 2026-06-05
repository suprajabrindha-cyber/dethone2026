package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwinSimulatorScreen(
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    val simulations by viewModel.simulations.collectAsState()
    val isSimulating by viewModel.isSimulatingTwin.collectAsState()
    val twinResult by viewModel.twinSimulationResult.collectAsState()

    var customScenarioTitle by remember { mutableStateOf("") }
    var customAssetCost by remember { mutableStateOf("") }

    // Multi-criteria presets
    val presets = listOf(
        PresetScenario("🚗 Buy Car", "Buy premium vehicle", 800000.0),
        PresetScenario("🏡 Home Mortgage Loan", "Buy residential villa", 4500000.0),
        PresetScenario("📈 Get Salary Increase", "Receive 25% annual hike", 240000.0),
        PresetScenario("💍 Marriage Plans", "Fund complete wedding expenses", 600000.0),
        PresetScenario("🎓 Education Loan Pay", "Acquire postgraduate degree", 1200000.0),
        PresetScenario("📊 Complex Investment", "Deploy index venture assets", 300000.0)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BentoLavenderLight)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
    ) {
        // Portal Header
        item {
            Column {
                Text(
                    text = "FINANCIAL TWIN SIMULATOR",
                    style = MaterialTheme.typography.titleMedium.copy(color = BentoBluePrimary),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Your AI Financial Sandbox",
                    style = MaterialTheme.typography.headlineSmall.copy(color = BentoTextNavy),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simulate big life steps in a safe sandbox before committing capital reserves.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BentoTextMuted
                )
            }
        }

        // 1. CHOOSE LIFE STEPS PRE-SET BUTTONS
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("twin_simulator_preset_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Step 1: Choose Scenario Preset",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset layout grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.chunked(2).forEach { rowPresets ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowPresets.forEach { preset ->
                                    Button(
                                        onClick = {
                                            viewModel.runTwinSimulation(preset.name, preset.value)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F7)),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(1.dp, BentoBorderLight),
                                        enabled = !isSimulating
                                    ) {
                                        Text(
                                            text = preset.name,
                                            color = BentoTextDark,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. OR DEFINE CUSTOM SANDBOX
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("twin_simulator_custom_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Or Model a Custom Sandbox",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = customScenarioTitle,
                        onValueChange = { customScenarioTitle = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_scenario_input"),
                        label = { Text("Scenario Description") },
                        placeholder = { Text("e.g., Buy a MacBook Pro M4 Max") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextDark,
                            unfocusedTextColor = BentoTextDark,
                            focusedBorderColor = BentoBluePrimary,
                            unfocusedBorderColor = BentoBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customAssetCost,
                        onValueChange = { customAssetCost = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_cost_input"),
                        label = { Text("Capital Asset Cost (₹)") },
                        placeholder = { Text("e.g., 250000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextDark,
                            unfocusedTextColor = BentoTextDark,
                            focusedBorderColor = BentoBluePrimary,
                            unfocusedBorderColor = BentoBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val cost = customAssetCost.toDoubleOrNull() ?: 0.0
                            if (customScenarioTitle.isNotEmpty() && cost > 0) {
                                viewModel.runTwinSimulation(customScenarioTitle, cost)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("run_custom_twin_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isSimulating && customScenarioTitle.isNotEmpty() && customAssetCost.isNotEmpty()
                    ) {
                        if (isSimulating) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = BrightWhite)
                        } else {
                            Icon(Icons.Default.Science, "Simulation Physics", tint = BrightWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simulate Custom Sandbox", color = BrightWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. SIMULATOR TERMINAL AND GRAPHICAL PREDICTIONS OUTPUT
        item {
            AnimatedVisibility(
                visible = isSimulating || twinResult != null,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("simulation_terminal_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F7)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, BentoBluePrimary)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSimulating) WarningAmber else BentoGreen)
                            )
                            Text(
                                text = if (isSimulating) "SIMULATION ACTIVE..." else "SIMULATION RESOLVED STATS",
                                color = if (isSimulating) WarningAmber else BentoGreen,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isSimulating) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(2.dp)),
                                color = BentoBluePrimary,
                                trackColor = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Recalculating Twin Emergency reserves, debt coverage ratio, and life metrics values against Gemini forecasting kernels...",
                                color = BentoTextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        } else {
                            twinResult?.let { result ->
                                Text(
                                    text = result,
                                    color = BentoTextDark,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedButton(
                                    onClick = { viewModel.clearTwinResult() },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(1.dp, BentoBorderLight),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Reset Sandbox Terminal", color = BentoTextDark)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. PREVIOUS SIMULATION ARCHIVES LEDGER
        item {
            Text(
                text = "Simulation History Ledger",
                style = MaterialTheme.typography.titleLarge,
                color = BentoTextNavy,
                fontWeight = FontWeight.ExtraBold
            )
        }

        if (simulations.isEmpty()) {
            item {
                Text(
                    text = "No saved simulations in this session. Run presets above to build your Twin's history dossier.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BentoTextMuted
                )
            }
        } else {
            items(simulations) { sim ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = BorderStroke(1.dp, BentoBorderLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Science, null, tint = BentoBluePrimary, modifier = Modifier.size(18.dp))
                            Text(
                                text = sim.scenarioName,
                                color = BentoTextDark,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                              )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = sim.predictionDetails,
                            color = BentoTextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            maxLines = 5
                        )
                    }
                }
            }
        }
    }
}

data class PresetScenario(val name: String, val desc: String, val value: Double)
