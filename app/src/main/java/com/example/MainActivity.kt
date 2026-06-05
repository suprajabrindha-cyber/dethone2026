package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Mandatory Edge-to-Edge full render bleed support
        
        setContent {
            MyApplicationTheme {
                var currentTab by remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = SurfaceCard,
                            tonalElevation = 8.dp,
                            modifier = Modifier.testTag("app_navigation_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentTab == 0,
                                onClick = { currentTab = 0 },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard Hub Portal") },
                                label = { Text("Home") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BentoTextNavy,
                                    selectedTextColor = BentoTextNavy,
                                    indicatorColor = BentoBlueLight,
                                    unselectedIconColor = BentoTextMuted,
                                    unselectedTextColor = BentoTextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_dashboard")
                            )
                            NavigationBarItem(
                                selected = currentTab == 1,
                                onClick = { currentTab = 1 },
                                icon = { Icon(Icons.Default.Shield, contentDescription = "Scam Shield Radar") },
                                label = { Text("Shield") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BentoTextNavy,
                                    selectedTextColor = BentoTextNavy,
                                    indicatorColor = BentoBlueLight,
                                    unselectedIconColor = BentoTextMuted,
                                    unselectedTextColor = BentoTextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_scamshield")
                            )
                            NavigationBarItem(
                                selected = currentTab == 2,
                                onClick = { currentTab = 2 },
                                icon = { Icon(Icons.Default.Science, contentDescription = "AI Sandbox Twins") },
                                label = { Text("Twin Sandbox") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BentoTextNavy,
                                    selectedTextColor = BentoTextNavy,
                                    indicatorColor = BentoBlueLight,
                                    unselectedIconColor = BentoTextMuted,
                                    unselectedTextColor = BentoTextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_twin")
                            )
                            NavigationBarItem(
                                selected = currentTab == 3,
                                onClick = { currentTab = 3 },
                                icon = { Icon(Icons.Default.Forum, contentDescription = "Conversations AI chatbot") },
                                label = { Text("AI Advice") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BentoTextNavy,
                                    selectedTextColor = BentoTextNavy,
                                    indicatorColor = BentoBlueLight,
                                    unselectedIconColor = BentoTextMuted,
                                    unselectedTextColor = BentoTextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_chatbot")
                            )
                            NavigationBarItem(
                                selected = currentTab == 4,
                                onClick = { currentTab = 4 },
                                icon = { Icon(Icons.Default.Code, contentDescription = "Developer Specs Info Hub") },
                                label = { Text("Specs") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BentoTextNavy,
                                    selectedTextColor = BentoTextNavy,
                                    indicatorColor = BentoBlueLight,
                                    unselectedIconColor = BentoTextMuted,
                                    unselectedTextColor = BentoTextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_architect")
                            )
                        }
                    },
                    containerColor = DarkNavyBG
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkNavyBG)
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            0 -> DashboardScreen(viewModel = viewModel)
                            1 -> ScamShieldScreen(viewModel = viewModel)
                            2 -> TwinSimulatorScreen(viewModel = viewModel)
                            3 -> ChatbotScreen(viewModel = viewModel)
                            4 -> ArchitectPortalScreen()
                        }
                    }
                }
            }
        }
    }
}
