package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScamReport
import com.example.ui.theme.*
import com.example.ui.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScamShieldScreen(
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    val scamReports by viewModel.scamReports.collectAsState()
    val isCheckingUpi by viewModel.isCheckingUpi.collectAsState()
    val upiResult by viewModel.upiRiskResult.collectAsState()

    var textUpiInput by remember { mutableStateOf("") }
    var qrFileScanningState by remember { mutableStateOf<String?>(null) }
    var screenshotScanState by remember { mutableStateOf<String?>(null) }
    var qrIsProcessing by remember { mutableStateOf(false) }
    var screenshotIsProcessing by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BentoLavenderLight)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
    ) {
        // Core Header
        item {
            Column {
                Text(
                    text = "SCAM SHIELD AI",
                    style = MaterialTheme.typography.titleMedium.copy(color = AlertRed),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Phishing & Payment Safety Radar",
                    style = MaterialTheme.typography.headlineSmall.copy(color = BentoTextNavy),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Verify recipients, payment links, and screenshots before making offline transfers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BentoTextMuted
                )
            }
        }

        // 1. RECIPIENT UPI ID RISK CHECKER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_risk_checker_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Real-time UPI Phishing Scanner",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enter any UPI address to execute a deep security audit against blacklists & scam probability databases.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextMuted,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    OutlinedTextField(
                        value = textUpiInput,
                        onValueChange = { textUpiInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upi_id_input_field"),
                        placeholder = { Text("e.g., urgent.payment@ybl", color = BentoTextMuted) },
                        label = { Text("Enter Recipient UPI ID") },
                        leadingIcon = { Icon(Icons.Default.AlternateEmail, "UPI icon", tint = BentoBluePrimary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextDark,
                            unfocusedTextColor = BentoTextDark,
                            focusedBorderColor = BentoBluePrimary,
                            unfocusedBorderColor = BentoBorderLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.checkUpiRisk(textUpiInput) },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("scan_upi_action_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isCheckingUpi && textUpiInput.isNotEmpty()
                        ) {
                            if (isCheckingUpi) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = BrightWhite)
                            } else {
                                Icon(Icons.Default.Security, "Scan Icon")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Audit Address", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (upiResult != null) {
                            OutlinedButton(
                                onClick = {
                                    textUpiInput = ""
                                    viewModel.clearRiskScannerState()
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, BentoBorderLight),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Clear", color = BentoTextMuted)
                            }
                        }
                    }

                    // Interactive Scan Result Card
                    AnimatedVisibility(
                        visible = upiResult != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        upiResult?.let { result ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (result.isBlacklisted) Color(0xFFFFDAD6) else Color(0xFFD2EBD4)
                                ),
                                border = BorderStroke(1.dp, if (result.isBlacklisted) AlertRed else BentoGreen)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (result.isBlacklisted) "⚠ HIGH SCAM RISK REPORTED" else "✔ ACCESS SECURITY CLEARANCE",
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (result.isBlacklisted) AlertRed else BentoGreen,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = if (result.isBlacklisted) AlertRed else BentoGreen,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${result.riskScore}% Probability",
                                                color = BrightWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Address: ${result.upiId}",
                                        color = BentoTextDark,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = result.warningMessage,
                                        color = BentoTextDark,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. COUNTERFEIT DETECTION BENCH (QR & Screenshots scanner simulation)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Simulated Anti-Scam Forensic Analyzer",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Scan file images of payment receipts (screen details) and QRs to check for fake watermarks, duplicate logs or high risk URLs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextMuted,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // QR Scanner Box Button
                        Column(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = {
                                    qrIsProcessing = true
                                    qrFileScanningState = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F7)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.QrCodeScanner, null, tint = BentoBluePrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Scan Fake QR", color = BentoTextDark, fontSize = 12.sp)
                            }

                            LaunchedEffect(qrIsProcessing) {
                                if (qrIsProcessing) {
                                    kotlinx.coroutines.delay(2000)
                                    qrFileScanningState = "FRAUD REPORT: Scanned QR redirect endpoints match counterfeit domains ('invest-secure712.ru'). Status: BLOCKED."
                                    qrIsProcessing = false
                                }
                            }
                        }

                        // Screenshot Scanner Box Button
                        Column(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = {
                                    screenshotIsProcessing = true
                                    screenshotScanState = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F7)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.ImageSearch, null, tint = WarningAmber)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Receipt Audit", color = BentoTextDark, fontSize = 12.sp)
                            }

                            LaunchedEffect(screenshotIsProcessing) {
                                if (screenshotIsProcessing) {
                                    kotlinx.coroutines.delay(2000)
                                    screenshotScanState = "COUNTERFEIT RECEIPT AUDIT: Scanned receipt image template matches counterfeit generators (unofficial font spacing, missing transaction ID). Mismatch alerted!"
                                    screenshotIsProcessing = false
                                }
                            }
                        }
                    }

                    // Processing Indicator
                    if (qrIsProcessing || screenshotIsProcessing) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = WarningAmber)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Forensic AI checking image hashes...", color = BentoTextMuted, fontSize = 12.sp)
                        }
                    }

                    // QR scanning response text
                    AnimatedVisibility(visible = qrFileScanningState != null) {
                        qrFileScanningState?.let { msg ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDAD6)),
                                border = BorderStroke(1.dp, AlertRed)
                            ) {
                                Text(
                                    text = msg,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoTextDark)
                                )
                            }
                        }
                    }

                    // Screenshot scanning response text
                    AnimatedVisibility(visible = screenshotScanState != null) {
                        screenshotScanState?.let { msg ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1C5)),
                                border = BorderStroke(1.dp, WarningAmber)
                            ) {
                                Text(
                                    text = msg,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoTextDark)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. SCAM HEATMAP & GEOGRAPHICAL ANALYSIS
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "AI Threat Intelligence Scam Heatmap",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "National density matrix tracking payment frauds in real-time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // SVG/Canvas Geographical Heatmap drawing
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F3F7))
                            .border(1.dp, BentoBorderLight),
                        contentAlignment = Alignment.Center
                    ) {
                        val pulseAnim = rememberInfiniteTransition(label = "pulseHeatmap")
                        val radiusFactor by pulseAnim.animateFloat(
                            initialValue = 6f,
                            targetValue = 18f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulseAnimScalar"
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height

                            val points = listOf(
                                Offset(width * 0.15f, height * 0.25f),
                                Offset(width * 0.35f, height * 0.15f),
                                Offset(width * 0.45f, height * 0.55f),
                                Offset(width * 0.70f, height * 0.40f),
                                Offset(width * 0.65f, height * 0.80f),
                                Offset(width * 0.85f, height * 0.35f),
                                Offset(width * 0.25f, height * 0.75f)
                            )

                            // Connecting lines representing network transactions routing
                            for (i in 0 until points.size - 1) {
                                drawLine(
                                    color = BentoBorderLight,
                                    start = points[i],
                                    end = points[i + 1],
                                    strokeWidth = 2f
                                )
                            }

                            // Draw safe node coordinates
                            points.forEach { pt ->
                                drawCircle(color = BentoLavenderLight, radius = 6f, center = pt)
                                drawCircle(color = BentoBluePrimary.copy(alpha = 0.5f), radius = 3f, center = pt)
                            }

                            // Draw Fraud Alert Hotzones with animated pulsation
                            drawCircle(
                                color = AlertRed.copy(alpha = 0.2f),
                                radius = radiusFactor * 2.5f,
                                center = Offset(width * 0.45f, height * 0.55f)
                            )
                            drawCircle(color = AlertRed, radius = 7f, center = Offset(width * 0.45f, height * 0.55f))

                            drawCircle(
                                color = WarningAmber.copy(alpha = 0.2f),
                                radius = radiusFactor * 1.8f,
                                center = Offset(width * 0.70f, height * 0.40f)
                            )
                            drawCircle(color = WarningAmber, radius = 6f, center = Offset(width * 0.70f, height * 0.40f))

                            drawCircle(
                                color = AlertRed.copy(alpha = 0.15f),
                                radius = radiusFactor * 2.8f,
                                center = Offset(width * 0.25f, height * 0.75f)
                            )
                            drawCircle(color = AlertRed, radius = 7f, center = Offset(width * 0.25f, height * 0.75f))
                        }

                        // Coordinates description tags overlay
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = BrightWhite.copy(alpha = 0.9f)),
                            border = BorderStroke(1.dp, BentoBorderLight)
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Text("ZONE C1 PHISHING SPIKE", color = AlertRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("Zone A: Moderate Shield Cleanse", color = BentoGreen, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }

        // 4. BLACKLISTED UPI LIST (Fraud Intelligence Feed)
        item {
            Text(
                text = "Live Network Blacklist Feed",
                style = MaterialTheme.typography.titleLarge,
                color = BentoTextNavy,
                fontWeight = FontWeight.ExtraBold
            )
        }

        items(scamReports.filter { it.isBlacklisted }) { report ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, BentoBorderLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFDAD6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Block, "Block icon", tint = AlertRed, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = report.upiId,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextDark,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                        Text(
                            text = report.warningMessage,
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoTextMuted),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "RISK",
                            style = MaterialTheme.typography.bodySmall.copy(color = AlertRed, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                        )
                        Text(
                            text = "${report.riskScore}%",
                            style = MaterialTheme.typography.bodyMedium.copy(color = AlertRed, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
