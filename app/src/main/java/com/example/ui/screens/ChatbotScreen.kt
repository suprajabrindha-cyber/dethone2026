package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val chatLoading by viewModel.chatLoading.collectAsState()
    val listState = rememberLazyListState()

    var userMessageText by remember { mutableStateOf("") }

    // Auto scroll down to latest chats
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    // Default clickable suggestion chips
    val shortcuts = listOf(
        "Can I buy a ₹50k laptop next month?",
        "How can I improve my financial score?",
        "Explain ways to block scanning scams"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BentoLavenderLight)
    ) {
        // Chat Header Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCard)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD2EBD4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SupportAgent, "Bot Icon", tint = BentoGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "FinGuard AI Copilot",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Active Sandbox Advisor",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoBluePrimary
                    )
                }
            }

            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier.testTag("clear_chat_button")
            ) {
                Icon(Icons.Default.DeleteSweep, "Clear history", tint = BentoTextMuted)
            }
        }

        // Messages list ledger
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (chatHistory.isEmpty()) {
                // Empty message welcoming box
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Welcome stars",
                        tint = BentoBluePrimary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Consult Your Financial Intelligence Companion",
                        style = MaterialTheme.typography.titleMedium,
                        color = BentoTextNavy,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ask questions regarding budget efficiency, cash flow risks, UPI phishing scan limits, or investment decisions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatHistory) { msg ->
                        ChatBubble(messageText = msg.text, isUser = msg.isUser)
                    }

                    if (chatLoading) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = BentoBluePrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("FinGuard is reasoning...", color = BentoTextMuted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Floated prompt shortcuts chip bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, BentoLavenderLight, BentoLavenderLight)
                        )
                    )
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    shortcuts.forEach { phrase ->
                        AssistChip(
                            onClick = { viewModel.sendChatMessage(phrase) },
                            label = { Text(phrase, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = BentoTextDark,
                                containerColor = BrightWhite
                            ),
                            border = BorderStroke(1.dp, BentoBorderLight),
                            modifier = Modifier.testTag("shortcut_${phrase.take(8)}")
                        )
                    }
                }
            }
        }

        // Bottom dialog input field
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BrightWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            border = BorderStroke(1.dp, BentoBorderLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .navigationBarsPadding() // Support edge-to-edge system bars
                    .imePadding(),           // Lift up keyboard gracefully
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    value = userMessageText,
                    onValueChange = { userMessageText = it },
                    placeholder = { Text("Ask FinGuard...", color = BentoTextMuted) },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .testTag("chat_input_text_field"),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = BentoTextDark,
                        unfocusedTextColor = BentoTextDark,
                        focusedLabelColor = BentoBluePrimary,
                        cursorColor = BentoBluePrimary,
                        focusedContainerColor = Color(0xFFF1F3F7),
                        unfocusedContainerColor = Color(0xFFF1F3F7)
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        val trimmed = userMessageText.trim()
                        if (trimmed.isNotEmpty()) {
                            viewModel.sendChatMessage(trimmed)
                            userMessageText = ""
                        }
                    },
                    modifier = Modifier
                        .background(BentoBluePrimary, CircleShape)
                        .testTag("send_chat_msg_btn"),
                    enabled = userMessageText.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Submit Message",
                        tint = BrightWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(messageText: String, isUser: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) BentoBluePrimary else BrightWhite
                ),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 2.dp,
                    bottomEnd = if (isUser) 2.dp else 16.dp
                ),
                border = if (isUser) null else BorderStroke(1.dp, BentoBorderLight)
            ) {
                Text(
                    text = messageText,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isUser) BrightWhite else BentoTextDark,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )
            }
            Text(
                text = if (isUser) "You" else "FinGuard AI Advisor",
                style = MaterialTheme.typography.bodySmall.copy(color = BentoTextMuted, fontSize = 9.sp),
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}
