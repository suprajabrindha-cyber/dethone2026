package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FinancialGoal
import com.example.data.Transaction
import com.example.ui.theme.*
import com.example.ui.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    val income by viewModel.incomeState.collectAsState()
    val expenses by viewModel.expensesState.collectAsState()
    val savings by viewModel.savingsState.collectAsState()
    val loans by viewModel.loansState.collectAsState()
    val investments by viewModel.investmentsState.collectAsState()

    val transactions by viewModel.transactions.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val healthScore by viewModel.healthScoreState.collectAsState()
    val isCalculatingScore by viewModel.isCalculatingScore.collectAsState()

    var showAddTxDialog by remember { mutableStateOf(false) }
    var showAddGoalDialog by remember { mutableStateOf(false) }

    // Dynamic Emergency Survival coverage calculation
    val survivalMonths = remember(income, expenses, loans) {
        val recurringCost = expenses + loans
        if (recurringCost <= 0) 12.0 else (savings / recurringCost) * 12.0
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBG)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
    ) {
        // Welcome Header Label
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PORTFOLIO OVERVIEW",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = BentoTextMuted,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "FinGuard AI",
                            style = MaterialTheme.typography.headlineMedium.copy(color = BentoTextNavy),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.calculateHealthScore() },
                    modifier = Modifier
                        .background(SurfaceCard, CircleShape)
                        .testTag("ai_recalculate_header")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Recalculate AI Score",
                        tint = BentoBluePrimary
                    )
                }
            }
        }

        // 1. DYNAMIC AI HEALTH SCORE PANEL
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("health_score_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AI Financial Health Score",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // AI Score Ring Graphic Canvas drawing
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(120.dp)
                                .weight(1f)
                        ) {
                            val activeScore = healthScore?.overallScore ?: 70
                            val animatedScore = animateFloatAsState(
                                targetValue = activeScore.toFloat(),
                                animationSpec = tween(1200, easing = LinearOutSlowInEasing),
                                label = "scoreDrawAnim"
                            )

                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawArc(
                                    color = BentoLavenderLight,
                                    startAngle = -220f,
                                    sweepAngle = 260f,
                                    useCenter = false,
                                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = BentoBluePrimary,
                                    startAngle = -220f,
                                    sweepAngle = (animatedScore.value / 100f) * 260f,
                                    useCenter = false,
                                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$activeScore",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        color = BentoTextNavy,
                                        fontSize = 32.sp
                                    )
                                )
                                Text(
                                    text = "HEALTH SCORE",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoTextMuted,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }

                        // Categories breakdown
                        Column(
                            modifier = Modifier
                                .weight(1.2f)
                                .padding(start = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CategoryProgress("Savings", healthScore?.savingsScore ?: 75, CyberEmerald)
                            CategoryProgress("Spending", healthScore?.spendingScore ?: 70, ShieldBlue)
                            CategoryProgress("Debt", healthScore?.debtScore ?: 85, WarningAmber)
                            CategoryProgress("Investment", healthScore?.investmentScore ?: 60, ShieldBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = BentoBorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    // AI Insight Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F3F7))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "AI Insight icon",
                            tint = BentoBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        if (isCalculatingScore) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = BentoBluePrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini processing data ratios...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = BentoTextDark)
                            )
                        } else {
                            Text(
                                text = healthScore?.aiInsight ?: "Tap 'Calculate AI Score' above for custom advisors.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = BentoTextDark,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                ),
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.calculateHealthScore() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_health_score_calc_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.AutoAwesome, "AutoAwesome", tint = BrightWhite)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calculate Live AI Health Score", color = BrightWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 2. LIVE PROFILE ADJUSTERS (Sliders to interact with score in real time)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("metrics_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Adjust Your Real-time Profile",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Slide values to see direct impact on health scores & emergency coverage.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextMuted,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InteractiveSlider("Monthly Income", income, 20000.0, 300000.0, "₹") {
                        viewModel.incomeState.value = it
                    }
                    InteractiveSlider("Expenses", expenses, 5000.0, 150000.0, "₹") {
                        viewModel.expensesState.value = it
                        viewModel.savingsState.value = (income - it - loans).coerceAtLeast(0.0)
                    }
                    InteractiveSlider("Savings Reserve", savings, 5000.0, 500000.0, "₹") {
                        viewModel.savingsState.value = it
                    }
                    InteractiveSlider("Active Loans", loans, 0.0, 100000.0, "₹") {
                        viewModel.loansState.value = it
                        viewModel.savingsState.value = (income - expenses - it).coerceAtLeast(0.0)
                    }
                    InteractiveSlider("Investments", investments, 0.0, 200000.0, "₹") {
                        viewModel.investmentsState.value = it
                    }
                }
            }
        }

        // 3. EMERGENCY FUND PREDICTOR
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BentoBlueLight),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBlueBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Emergency Fund Coverage",
                            style = MaterialTheme.typography.titleMedium,
                            color = BentoTextNavy,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield Protection Icon",
                            tint = BentoBluePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(
                                text = "%.1f Mo".format(survivalMonths),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = BentoTextNavy
                                )
                            )
                            Text(
                                text = "Safety Buffer",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoTextMuted)
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier
                                .height(48.dp)
                                .padding(horizontal = 16.dp),
                            color = BentoBlueBorder
                        )

                        Column(modifier = Modifier.weight(2f)) {
                            val advice = when {
                                survivalMonths >= 6.0 -> "Premium! You meet the gold-standard 6-month safety threshold buffer."
                                survivalMonths >= 3.0 -> "Save ₹%.0f more monthly to achieve full 6-month safety coverage.".format((income * 0.05).coerceAtLeast(2000.0))
                                else -> "Warning: Highly exposed. Limit discretionary dining by 15% and save more immediately."
                            }
                            Text(
                                text = advice,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoTextDark,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. SMART BUDGET OPTIMIZER & DISCOVERED SUB EXPOSURES
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Smart Budget Optimizer Insights",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    BudgetTipItem(
                        icon = Icons.Default.DoNotDisturbOn,
                        tint = AlertRed,
                        category = "Unused Subscriptions",
                        msg = "FinGuard detected 3 duplicate fitness streaming packages leaking ₹899/mo. Tap chatbot to auto-cancel."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetTipItem(
                        icon = Icons.Default.TrendingDown,
                        tint = WarningAmber,
                        category = "Overspending Dining Out",
                        msg = "Your food delivery charges rose 18% last week. Set a strict weekly limit of ₹2,500 to save ₹4,000/mo."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetTipItem(
                        icon = Icons.Default.Savings,
                        tint = CyberEmerald,
                        category = "Cost-Saving Actionable Play",
                        msg = "Automate salary transfers of 15% within 1 hour of deposit to avoid impulse credit usage."
                    )
                }
            }
        }

        // 5. FINANCIAL PLAN GOAL TRACKER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Goal Trackers",
                    style = MaterialTheme.typography.titleLarge,
                    color = BentoTextNavy,
                    fontWeight = FontWeight.ExtraBold
                )
                TextButton(onClick = { showAddGoalDialog = true }) {
                    Icon(Icons.Default.AddCircle, "Add Goal Icon", tint = BentoBluePrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Goal", color = BentoBluePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (goals.isEmpty()) {
            item {
                Text(
                    text = "No active financial target goals yet. Click 'Add Goal' to register your vacations or house saving reserves.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MutedSlate),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(goals) { goal ->
                GoalItem(goal = goal, onRemove = { viewModel.removeGoal(goal.id) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // 6. RECENT SPENDING TRANSACTIONS LISTING
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transactions Ledger",
                    style = MaterialTheme.typography.titleLarge,
                    color = BentoTextNavy,
                    fontWeight = FontWeight.ExtraBold
                )
                TextButton(onClick = { showAddTxDialog = true }) {
                    Icon(Icons.Default.AddCircle, "Add Tx Icon", tint = BentoBluePrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Record", color = BentoBluePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    text = "Transaction list empty. Keep track of earnings and spend patterns to protect balances.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MutedSlate)
                )
            }
        } else {
            items(transactions) { tx ->
                TransactionCard(transaction = tx, onRemove = { viewModel.removeTransaction(tx.id) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Modal dialogs
    if (showAddTxDialog) {
        AddTransactionDialog(
            onDismiss = { showAddTxDialog = false },
            onSave = { name, amt, isExp, cat ->
                viewModel.addTransaction(name, amt, isExp, cat)
                showAddTxDialog = false
            }
        )
    }

    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onSave = { name, target, current, cat, date ->
                viewModel.addGoal(name, target, current, cat, date)
                showAddGoalDialog = false
            }
        )
    }
}

// ==========================================
// 7. Sub-components & Helpers
// ==========================================

@Composable
fun CategoryProgress(label: String, score: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = BentoTextDark, fontSize = 11.sp))
            Text("$score%", style = MaterialTheme.typography.bodySmall.copy(color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp))
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape),
            color = color,
            trackColor = BentoLavenderLight
        )
    }
}

@Composable
fun InteractiveSlider(
    label: String,
    value: Double,
    min: Double,
    max: Double,
    prefix: String,
    onValueChange: (Double) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = BentoTextDark, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$prefix%s".format(NumberFormat.getNumberInstance(Locale.getDefault()).format(value.toInt())),
                color = BentoBluePrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toDouble()) },
            valueRange = min.toFloat()..max.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = BentoBluePrimary,
                activeTrackColor = BentoBluePrimary,
                inactiveTrackColor = BentoLavenderLight
            )
        )
    }
}

@Composable
fun BudgetTipItem(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, category: String, msg: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F3F7))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(category, style = MaterialTheme.typography.bodyMedium.copy(color = tint, fontWeight = FontWeight.Bold, fontSize = 13.sp))
            Text(msg, style = MaterialTheme.typography.bodySmall.copy(color = BentoTextDark, fontSize = 12.sp))
        }
    }
}

@Composable
fun GoalItem(goal: FinancialGoal, onRemove: () -> Unit) {
    val progress = if (goal.targetAmount > 0) (goal.currentAmount / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BentoLavenderLight),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BentoLavenderBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (goal.category.lowercase()) {
                            "house" -> Icons.Default.Home
                            "car" -> Icons.Default.DirectionsCar
                            "education" -> Icons.Default.School
                            "vacation" -> Icons.Default.Flight
                            else -> Icons.Default.Stars
                        },
                        contentDescription = goal.category,
                        tint = BentoBluePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(goal.name, style = MaterialTheme.typography.titleMedium.copy(color = BentoTextDark, fontWeight = FontWeight.Bold))
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, "Remove goal", tint = BentoTextMuted, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Filled: ₹%.0f / ₹%.0f".format(goal.currentAmount, goal.targetAmount),
                    style = MaterialTheme.typography.bodySmall.copy(color = BentoTextMuted)
                )
                Text(
                    text = "${(progress * 100).toInt()}% Done",
                    style = MaterialTheme.typography.bodySmall.copy(color = BentoBluePrimary, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (progress >= 0.8) BentoGreen else BentoBluePrimary,
                trackColor = BrightWhite
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Milestone Expected: ${goal.targetDate}",
                style = MaterialTheme.typography.bodySmall.copy(color = BentoTextDark, fontSize = 11.sp)
            )
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, BentoBorderLight)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (transaction.isExpense) Color(0xFFFFDAD6) else Color(0xFFD2EBD4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.isExpense) Icons.Default.ArrowOutward else Icons.Default.ArrowDownward,
                    contentDescription = if (transaction.isExpense) "Spent" else "Earned",
                    tint = if (transaction.isExpense) Color(0xFFBA1A1A) else BentoGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyMedium.copy(color = BentoTextDark, fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodySmall.copy(color = BentoTextMuted)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (transaction.isExpense) "-" else "+"}₹${transaction.amount.toInt()}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (transaction.isExpense) BentoTextDark else BentoGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Default.Clear, "Remove tx", tint = BentoTextMuted, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onSave: (String, Double, Boolean, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("Food & Dining") }

    val categoriesList = listOf("Food & Dining", "Shopping", "Rent & Bills", "Investment", "Salary", "Scam Shield Risk", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Financial Record", color = BentoTextNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Record Name (e.g. Starbucks)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )
                TextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Type:", color = BentoTextNavy, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isExpense, onClick = { isExpense = true })
                        Text("Expense", color = BentoTextDark)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = !isExpense, onClick = { isExpense = false })
                        Text("Income", color = BentoTextDark)
                    }
                }

                Text("Category:", color = BentoTextNavy, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesList.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amt = amountStr.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && amt > 0.0) {
                        onSave(title, amt, isExpense, category)
                    }
                }
            ) {
                Text("Save Record", color = BentoBluePrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BentoTextMuted)
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun AddGoalDialog(onDismiss: () -> Unit, onSave: (String, Double, Double, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var targetStr by remember { mutableStateOf("") }
    var currentStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("House") }
    var targetDate by remember { mutableStateOf("2028-12") }

    val categories = listOf("House", "Car", "Education", "Vacation", "Retirement")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Establish Financial Target Goal", color = BentoTextNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Title (e.g. Dream House)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )
                TextField(
                    value = targetStr,
                    onValueChange = { targetStr = it },
                    label = { Text("Target Capital Value (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )
                TextField(
                    value = currentStr,
                    onValueChange = { currentStr = it },
                    label = { Text("Initial Saved Cache (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )
                TextField(
                    value = targetDate,
                    onValueChange = { targetDate = it },
                    label = { Text("Goal Milestone Horizon (YYYY-MM)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextMuted,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    )
                )

                Text("Category Icons Selection:", color = BentoTextNavy, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val target = targetStr.toDoubleOrNull() ?: 0.0
                    val current = currentStr.toDoubleOrNull() ?: 0.0
                    if (name.isNotEmpty() && target > 0) {
                        onSave(name, target, current, category, targetDate)
                    }
                }
            ) {
                Text("Create Target", color = BentoBluePrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BentoTextMuted)
            }
        },
        containerColor = Color.White
    )
}
