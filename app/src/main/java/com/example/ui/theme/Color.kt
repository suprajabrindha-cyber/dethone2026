package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Bento Grid Design System Color Palette
// ==========================================
val BentoCanvasBG = Color(0xFFF7F9FC)      // Soft light grey-blue canvas background
val BentoTextNavy = Color(0xFF001D35)      // Deep brand navy for primary headers
val BentoTextDark = Color(0xFF1A1C1E)      // Charcoal dark for body text
val BentoTextMuted = Color(0xFF5C5E67)     // Muted gray for subtitles

// Cards Surface Colors
val BentoCardWhite = Color(0xFFFFFFFF)     // Dominant grid card background
val BentoBorderLight = Color(0xFFDEE2EB)   // Subtle light gray boundary divider

// Bento Cells Brand Accents
val BentoBluePrimary = Color(0xFF0061A4)   // Main interactive brand elements
val BentoBlueLight = Color(0xFFD6E3FF)     // "Safety Buffer" light blue background
val BentoBlueBorder = Color(0xFFBAC7E3)    // Blue frame accent

val BentoLavenderLight = Color(0xFFE1E2EC) // "Car Goal" grey-lavender background
val BentoLavenderBorder = Color(0xFFC5C6D0) // Lavender frame accent

// High-Contrast Dark Card (Scam Shield UI Contrast Block)
val BentoDarkCardBG = Color(0xFF1A1C1E)    // Deep slate block contrast background
val BentoDarkCardBorder = Color(0xFF2F3033)// Dark border outline
val BentoDarkCardText = Color(0xFFFFFFFF)  // White card text
val BentoAlertRed = Color(0xFF690005)      // Warning critical alert text
val BentoAlertRedLight = Color(0xFFFFB4AB) // Alert red card accent background
val BentoGreen = Color(0xFF006D3B)         // Green safety indicators (+₹2k progress)

// ==========================================
// Compatibility Constants Mapping
// ==========================================
val CyberEmerald = BentoGreen
val ShieldBlue = BentoBluePrimary
val WarningAmber = Color(0xFFB06000)       // Adjusted for legibility on light backgrounds
val AlertRed = Color(0xFFBA1A1A)           // Adjusted for legibility on light backgrounds

val DarkNavyBG = BentoCanvasBG            // Matches canvas base background
val SurfaceCard = BentoCardWhite          // Matches clean white cards
val SurfaceCardElevated = BentoBlueLight  // Matches helper components
val AccentGlow = Color(0x050061A4)

val LightSlateBG = BentoCanvasBG
val LightSurface = BentoCardWhite
val MutedSlate = BentoTextMuted
val BrightWhite = Color(0xFFFFFFFF)
