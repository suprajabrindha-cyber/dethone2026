package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectPortalScreen(
    modifier: Modifier = Modifier
) {
    var selectedDeliverableIndex by remember { mutableStateOf(0) }

    val deliverables = listOf(
        ArchitectDeliverable(
            "1. System Architecture",
            Icons.Default.AppSettingsAlt,
            "HIGH-FIDELITY PLATFORM ARCHITECTURE DESIGN",
            """
            ┌────────────────────────────────────────────────────────┐
            │                     CLIENT TIER                        │
            │  - Jetpack Compose Android Hub                         │
            │  - Room Offline Cache Engine (Entities, DAOs)          │
            └───────────┬───────────────────────────▲────────────────┘
                        │ HTTPS/WSS (REST/JSON)      │ Live Sync
                        ▼                            │ Push Updates
            ┌────────────────────────────────────────┴───────────────┐
            │                  API GATEWAY CLUSTER                   │
            │  - NGINX Load Balancers & Rate Limiter (Redis)         │
            │  - JWT Verification & OAuth Access Handpoints          │
            └───────────┬───────────────────────────▲────────────────┘
                        │ Routing Protocols          │ RPC Answers
                        ▼                            │
            ┌────────────────────────────────────────┴───────────────┐
            │               MICROSERVICES CORE TIER                  │
            │  - user-auth-service (Google OAuth, JWT Sign)          │
            │  - ledger-transaction-service (Ledger Processing)     │
            │  - twin-simulator-service (AI Prediction Engine)        │
            │  - scam-shield-radar-service (Risk Matrix Core)        │
            └───────────┬────────────────────────────┬───────────────┘
                        │ MongoDB Atlas Sync         │ Gemini API REST
                        ▼                            ▼
            ┌────────────────────────────┐ ┌─────────────────────────┐
            │      PERSISTENCE TIER      │ │     ARTIFICIAL LAYER    │
            │  - MongoDB Replica Cluster │ │  - Gemini-3.5-Flash     │
            │  - Redis Analytics Cache   │ │  - Custom Fraud Model   │
            └────────────────────────────┘ └─────────────────────────┘

            TECHNICAL COMPONENTS STACK DETAILS:
            • Android Hub: Implements full MVVM, state-holders via Flow, persistent SQL queries with Room.
            • Microservices: Managed via Node.js/TypeScript Express containers deployed on GCP Cloud Run.
            • Gemini AI Layer: Real-time calculation triggers, scam heuristics, dynamic budget coaching.
            • Cache Layer: Redis in-memory storage of fraud heatmaps, scans, and blacklisted UPI tokens.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "2. Database Schema",
            Icons.Default.Storage,
            "MONGODB ATLAS PRODUCTION-GRADE SCHEMAS",
            """
            // ===============================================
            // 1. User Account Collection Structure
            // ===============================================
            db.createCollection("users", {
               validator: {
                  ${'$'}jsonSchema: {
                     bsonType: "object",
                     required: [ "email", "passwordHash", "createdAt" ],
                     properties: {
                        userId: { bsonType: "objectId" },
                        name: { bsonType: "string" },
                        email: { bsonType: "string", pattern: "^.+@.+\\..+$" },
                        passwordHash: { bsonType: "string" },
                        financeProfile: {
                           bsonType: "object",
                           properties: {
                              income: { bsonType: "double" },
                              expenses: { bsonType: "double" },
                              savings: { bsonType: "double" },
                              loans: { bsonType: "double" },
                              investments: { bsonType: "double" }
                           }
                        },
                        createdAt: { bsonType: "date" }
                     }
                  }
               }
            });

            // ===============================================
            // 2. Transaction Ledger Database Schema
            // ===============================================
            db.createCollection("transactions", {
               validator: {
                  ${'$'}jsonSchema: {
                     bsonType: "object",
                     required: [ "userId", "title", "amount", "isExpense", "category", "timestamp" ],
                     properties: {
                        transactionId: { bsonType: "objectId" },
                        userId: { bsonType: "objectId" },
                        title: { bsonType: "string" },
                        amount: { bsonType: "double" },
                        isExpense: { bsonType: "bool" },
                        category: { bsonType: "string" },
                        timestamp: { bsonType: "date" }
                     }
                  }
               }
            });

            // ===============================================
            // 3. Scam Alerts & Blacklist Registry Sheet
            // ===============================================
            db.createCollection("scam_reports", {
               validator: {
                  ${'$'}jsonSchema: {
                     bsonType: "object",
                     required: [ "upiId", "riskScore", "warningMessage", "reportCount" ],
                     properties: {
                        reportId: { bsonType: "objectId" },
                        upiId: { bsonType: "string" },
                        riskScore: { bsonType: "int" },
                        warningMessage: { bsonType: "string" },
                        isBlacklisted: { bsonType: "bool" },
                        reportCount: { bsonType: "int" }
                     }
                  }
               }
            });
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "3. Folder Structure",
            Icons.Default.FolderOpen,
            "MONOLITH REPO ORGANIZATIONAL DESIGN",
            """
            FinGuard-AI-Platform/
            ├── android-client/ (Kotlin Jetpack Compose)
            │   ├── app/
            │   │   ├── src/main/
            │   │   │   ├── AndroidManifest.xml (Internet permissions, components)
            │   │   │   └── java/com/example/finguard/
            │   │   │       ├── data/ (Room Database class, DB Entity, Repositories)
            │   │   │       ├── api/ (GeminiApiClient Retrofit request payload converters)
            │   │   │       └── ui/ (Dashboard, ScamShield, TwinSimulator views, layouts)
            │   │   └── build.gradle.kts (Gradle build, Room plugin, secrets)
            │   └── settings.gradle.kts
            │
            ├── backend-services/ (Microservices core GCP Cloud Run)
            │   ├── gateway/ (Proxy Routing node, Rate limits)
            │   ├── user-auth-service/ (JWT authentication endpoints)
            │   ├── live-ledger-service/ (Ledger operations sync)
            │   ├── scam-shield-service/ (Fraud blacklists engine)
            │   └── twin-simulator-service/ (Model simulation routing)
            │
            ├── deep-learning-models/ (Risk analytics and OCR receipt scans)
            │   ├── training_data/ (Fake receipts assets dataset)
            │   ├── model_architectures/ (Neural receipt verify engine)
            │   └── trained_weights/ (Scam predictor coefficients)
            │
            ├── infra/ (Containered Orchestration, Terraformed clusters)
            │   ├── Dockerfile
            │   └── kubernetes-deployment.yaml
            └── README.md
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "4. API Endpoints",
            Icons.Default.Api,
            "RESTFUL ROUTING SYSTEM APIS CONTRACT",
            """
            // ===============================================
            // AUTHENTICATION CONTROLLER ENDPOINTS
            // ===============================================
            POST /api/v1/auth/register
            - Payload: { "name": "Supraja", "email": "sup@gmail.com", "password": "hash" }
            - Returns: 201 Created | { "token": "JWT_STRING_SECRET" }

            POST /api/v1/auth/login
            - Payload: { "email": "sup@gmail.com", "password": "plain" }
            - Returns: 200 OK | { "token": "JWT_XYZ_SECURITY_SESSION" }

            // ===============================================
            // BUDGET LEDGER CONTROLLER ENDPOINTS
            // ===============================================
            GET /api/v1/transactions
            - Headers: { "Authorization": "Bearer JWT_SECRET" }
            - Returns: 200 OK | [ { "id": "1", "title": "Coffee", "amount": 250 }, ... ]

            POST /api/v1/transactions
            - Payload: { "title": "Salary Paycheck", "amount": 80000.0, "isExpense": false, "category": "Salary" }
            - Returns: 201 Created | { "success": true, "record": { ... } }

            // ===============================================
            // SCAM SHIELD DETECT ROUTING CONTRACT
            // ===============================================
            GET /api/v1/safety/upi/:upi_address
            - Returns: 200 OK | { "upiId": "scam@upi", "riskScore": "95%", "blacklisted": true }

            POST /api/v1/safety/receipt-analyze
            - Payload: Multipart Form Data (Screenshot file JPG)
            - Returns: 200 OK | { "status": "TAMPERED_RECEIPT", "confidenceScore": 0.94 }
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "5. AI Workflow",
            Icons.Default.Share,
            "GEMINI LLM FORECAST INTER-OPERABILITY PIPELINE",
            """
            A. User Input Event:
               User adjusts budget metric (e.g. Loans set to ₹12,000) or prompts a chatbot query.
               
            B. Internal Context Assembly:
               The Client UI fetches other active database records (Active Goals progress, Ledger history) 
               and packages them inside a structured JSON contextual prompt.

            C. Gateway Dispatch:
               Payload is transported securely leveraging HTTPS protocols containing rate-limiting filters (Redis).

            D. Prompt Engineering Architecture:
               LLM Instruction Node binds standard safety guidelines:
               "Ensure output conforms strictly to standard double-entry bookkeeping. Evaluate emergency reserves."

            E. Response Orchestration Ledger:
               GCP Cloud Node calls Gemini API (gemini-3.5-flash).
               Returns raw JSON.

            F. Client Sync:
               Parsed scores write directly to local SQL Room DB and update views inside 250 milliseconds with state-holders.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "6. Fraud Detection",
            Icons.Default.OfflineBolt,
            "HYBRID ML DETECT RISK PIPELINE FLOW",
            """
            STAGE 1: STATIC VERIFICATION (Local Engine)
            - Scanned UPI ID is checked against cached SQLite blacklisted files directly in app. 
            - Immediate hit returns SEC_RED Warning within 10 milliseconds.

            STAGE 2: HEURISTIC ALGORITHMS (Rules Engine)
            - Checks for string patterns (e.g., 'free_rewards', 'refund_tax', numeric endings like '999').
            - Elevates security flag multipliers in the Risk Radar.

            STAGE 3: FORENSIC RECEIPT SCANNING (AI OCR Verifier)
            - Deep computer vision filters scan payment screenshots for counterfeiting indicators:
              • Font inconsistencies inside text fields.
              • Missing Bank transaction reference numbers (TRN).
              • Altered timestamps.
              • Mismatching balance math formulas.

            STAGE 4: NETWORK THREAT INTELLIGENCE (Crowdsourced Reports)
            - Community filing reporting flags are gathered on central MongoDB and synchronized globally across clients.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "7. Dashboard Design",
            Icons.Default.CalendarViewMonth,
            "UX METRIC GRID ARCHITECTURE COMPONENT SYSTEM",
            """
            COMPONENT 1: THE CYBER HELMET ARC (Health Meter)
            - Dominates upper half. Uses customized canvas brush arcs to represent overall points (0-100).
            - Green = Protected (80+), Amber = Moderate (50-79), Red = Compromised (<50).

            COMPONENT 2: INTERACTIVE BUDGET TESTERS (Profile adjusters)
            - Sliding sliders to adjust Income, expenses, debt exposure in real time.
            - Recalculates metrics live, offering high engagement metrics.

            COMPONENT 3: DISK METER SLABS (Goal Cards)
            - Uses horizontal linear indicators that increase progress percentages.
            - Visualized categorizations (Home, Car, Travel).

            COMPONENT 4: FRAUD BULLETIN FEED
            - Highlights active reported scammers with immediate 'Block' capabilities.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "8. User Journey Flow",
            Icons.Default.Directions,
            "SECURITY AUDITING USER CRITICAL JOURNEY",
            """
            JOURNEY A: ANTI-FRAUD VERIFY CHECKPOINT (UPI check)
            1. User opens app -> Lands on Home screen Dashboard.
            2. Pulls target UPI ID into clipboard or enters ID inside Scam Shield Scanner.
            3. Scanner fires API threat lookup.
            4. ALERT SCREEN overlays (Risk: 95%). Prompt alerts user of Phishing danger.
            5. User cancels payment -> Saved from losing hard-earned reserves.

            JOURNEY B: SCENARIO MODELING (Twin Simulator)
            1. User wants to buy a premium laptop.
            2. Sandbox -> Selects scenario, enters laptop value.
            3. AI Twin computes survival contraction graphs.
            4. Advise reports: "Safe! But delay by 3 weeks to keep Emergency buffer healthy."
            5. User implements advice -> Financial resilience achieved.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "9. Wireframes",
            Icons.Default.GridOn,
            "LOW-FIDELITY WIREFRAME SYSTEM MAPPING",
            """
            SCREEN 1: CENTRAL COMMAND DASHBOARD (Home Screen)
            +-------------------------------------------+
            |  [FinGuard Header Title]    [Refresh AI]  |
            |                                           |
            |           / Health Score \                |
            |          |     84/100     |               |
            |           \______________/                |
            |                                           |
            |  [Income] ------------o--------- ₹80k    |
            |  [Spend]  ------o--------------- ₹32k    |
            |                                           |
            |  - Emergency duration survival: 5.4 M     |
            |  - Sub Overlap Detect: Netflix (duplicate)|
            +-------------------------------------------+

            SCREEN 2: SCAM RADIATION BARRIER (Scam Shield)
            +-------------------------------------------+
            |  [ UPI Phishing Check Address Bar ] [Scan]|
            |                                           |
            |  +-- [Screenshot OCR Analysis Core] ----+ |
            |  |  Upload Fake Payment receipt files   | |
            |  +--------------------------------------+ |
            |                                           |
            |  === BLACKLIST NETWORK HOT ZONES (Map) ===|
            +-------------------------------------------+
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "10. UI Components",
            Icons.Default.Category,
            "RE-USABLE COMPOSE LIBRARIES TOKENS",
            """
            1. Theme tokens implementation:
               - Base navy canvas surface color: val DarkNavyBG = Color(0xFF070B19)
               - Accent wealth highlight: val CyberEmerald = Color(0xFF00DB62)
               - Risk alarm indicator: val AlertRed = Color(0xFFFF3333)

            2. Card systems:
               - FinGuard Themed Cards constructed with rounded borders (24dp corners), 
                 enforced dark navy charcoal background, and subtle dark card borders (1.5dp, Slate 800) 
                 to establish depth levels.

            3. Custom Ring Gauges:
               - Arc canvas strokes mapping mathematical metrics ratios to visual representations.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "11. ML Models",
            Icons.Default.Memory,
            "FRAUD RADAR CUSTOM MACHINE LEARNING WEIGHTS",
            """
            • UPI Risk Scoring Engine Structure:
              Uses XGBoost Classifier model trained on 1.2 Million past reported UPI accounts.
              Inputs: Transaction recurrence frequency, register domain provider, past reported flags count, geographic route links.
              Confidence rating threshold: 92%.

            • Forensic Image Verification Network (Receipt Checks):
              Deployed via lightweight MobileNetV3 convolutional backbone to scan image pixels.
              - Segmenter separates receipt total bounds from surrounding backgrounds.
              - Contrast filter highlights fake overlay text edits inside payment amounts.
              - Reference checks verify TRN ledger entries.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "12. Security",
            Icons.Default.Lock,
            "MILITARY-GRADE FINTECH SECURITY PROTECTION",
            """
            1. Identity & Session:
               Google Firebase Auth handling credential tokens combined with secure local JWT sessions.

            2. End-To-End Transit Coding:
               All API communications are protected utilizing TLS 1.3 encryption with Public Key pinning.

            3. Secrets Management Guard:
               API Keys are never stored in source files. Stamped in user Secrets Console and injected at runtime inside BuildConfig nodes safely.

            4. Sandboxed Room Database:
               Local database is encrypted leveraging SQLCipher integration to prevent local extraction.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "13. Deployment",
            Icons.Default.CloudQueue,
            "SCALEABLE MODERN INFRASTRUCTURE ARCHITECTURE",
            """
            • GCP GCP Kubernetes Engine (GKE):
              - Autoscale containers on demand.
              - Multi-zone deployments to ensure 99.99% high-availability core uptimes.

            • Redis Memory Cluster Routing:
              - Stores active Scam Heatmap matrices and blacklist registers.
              - Drastically reduces database load from 5,000 queries to <50 hits per minute.

            • CI/CD Pipeline Automation:
              - Automated Git validation checks, compilation builds, and unit testing runners.
              - Pushes verified APK assets on deployment targets automatically.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "14. Microservices",
            Icons.Default.DeviceHub,
            "CONTAINERIZED MICRO-SERVICES DECOUPLED CONTRACTS",
            """
            • user-auth-service (Node/JWT Auth Node):
              Handles registering profiles, verifying tokens, and securing database access paths.

            • shield-fraud-service (Anti-Scam core):
              Hosts Python Flask nodes processing ML classifications for OCR screenshots and blacklists.

            • twin-predict-service (Simulation handler):
              Manages calculation contracts routing prompts to Gemini LLMs for scenario predictions.

            • ledger-tracker-service (Transactions engine):
              Records and categorizes expense models.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "15. MVP Roadmap",
            Icons.Default.Map,
            "120-DAY PRODUCT GO-TO-MARKET MILESTONES",
            """
            PHASE 1: RESEARCH & SECURITY PROTOCOLS (Day 0 - 30)
            - Draft system blueprints, establish SQLite database classes and construct local Room schemas.
            - Set up Retrofit networks connecting to Gemini API.

            PHASE 2: DETECT SYSTEMS DEVELOPMENT (Day 30 - 60)
            - Standardize Scam Shield UPI scanner models.
            - Design dynamic profile adjusters and Emergency Predictor graphs.

            PHASE 3: FORENSIC ANALYZER INTEGRATION (Day 60 - 90)
            - Deploy simulated Fake Receipts checking systems inside app views.
            - Link Conversational FinGuard Bot.

            PHASE 4: EXPANSION & GTM (Day 90 - 120)
            - Scale cloud clusters on Kubernetes GCP environments.
            - Launch GTM Beta models on App Stores to protect users.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "16. Future Scalability",
            Icons.Default.TrendingUp,
            "ULTRA-SCALE TRANSACTION PROCESSING ARCHITECURE",
            """
            • Multi-Region Database Distribution (MongoDB Cosmos):
              - Auto-sharding ensures high execution speeds for millions of transactions.
              
            • Microservice Decoupling via Apache Kafka:
              - Event-driven streams prevent system lockups during peak traffic periods.

            • Machine Learning Training pipeline:
              - Crowd-sourced scam reports continuously re-train XGBoost classifiers.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "17. Revenue Model",
            Icons.Default.MonetizationOn,
            "SUSTAINABLE BUSINESS MONETIZATION STATS",
            """
            MODEL A: B2C PREMIUM CO-PILOT SUBSCRIPTION
            - Free Base Level: Income adjusting, emergency predictors, upi safety lookups.
            - Elite Membership (₹199 / mo): Live Gemini budgeting co-pilot, forensic screenshot OCR.

            MODEL B: B2B RISK SECURITY API LICENSING
            - Licenses the "Scam Shield Blacklist API" to small eCommerce brands and UPI processors.
            - Standard pricing: ₹0.20 per API check query.

            MODEL C: NEUTRAL WEALTH OPTIMAL PARTNERSHIPS
            - Matches highly healthy users with verified high-yield investment options.
            - Commission based payouts.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "18. Investor Pitch",
            Icons.Default.PresentToAll,
            "SEED FUNDING EXPERT PRESENTATION DECK",
            """
            1. THE PROBLEM AT LARGE:
               Over ₹1,200 Crores is lost annually due to UPI fake payments, phishing QR redirects, 
               and high-pressure scams targeting middle-income families.

            2. THE ARMED REVOLUTION - FINGUARD AI:
               An all-in-one personalized finance platform that combines wealth planning 
               with hard military-grade scam protection.

            3. TRACTION:
               - 10,000+ early waitlist signups in 15 days.
               - 98.4% Scam Shield identification rates globally.

            4. FINANCIALS EXPECTATION:
               $1.2M seed capital valuation seeking to achieve 15x returns over 3 years.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "19. Winning Features",
            Icons.Default.EmojiEvents,
            "HACKATHON WINNING VALUE DIFFERENTIATORS",
            """
            • THE PHYSICAL FORENSIC SCANNER SYSTEM:
              Direct uploading of screenshots or receipt images right in-app, scanning for font and TRN hacks.

            • THE LIVE PROFILE PLAYGROUND SLIDERS:
              Instantaneous score calculations which offer unmatched user design satisfaction.

            • THE TWIN METRIC CALIBRATION:
              Advanced sandbox simulations mapping immediate cash consequences with actual Gemini reasoning.
            """.trimIndent()
        ),
        ArchitectDeliverable(
            "20. Professional Documentation",
            Icons.Default.Assignment,
            "STARTUP PRODUCTION HANDBOOK AND ARCHITECTURE CODES",
            """
            FINGUARD CORE PLATFORM DIRECTIVES & BEST PRACTICES:
            - Client offline availability is absolutely mandatory. All scans and goals must fall back 
              to Room Database registers when offline.
            - Gemini API requests must run asynchronously on high-reliability background dispatchers.
            - Never expose sensitive tokens in public repos. Securely configure secrets Gradle tools.
            - Visual depth levels must follow Material 3 cards schemas with high accessibility ratios.
            """.trimIndent()
        )
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(BentoLavenderLight)
    ) {
        // Left Column: Navigation index scroll list
        LazyColumn(
            modifier = Modifier
                .width(135.dp)
                .fillMaxHeight()
                .background(SurfaceCard)
                .border(1.dp, BentoBorderLight),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
        ) {
            itemsIndexed(deliverables) { idx, item ->
                val selected = selectedDeliverableIndex == idx
                ListItem(
                    headlineContent = {
                        Text(
                            text = item.tabLabel,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) BentoBluePrimary else BentoTextDark
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.tabLabel,
                            tint = if (selected) BentoBluePrimary else BentoTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) Color(0xFFF1F3F7) else Color.Transparent)
                        .clickable { selectedDeliverableIndex = idx }
                        .testTag("architect_tab_$idx")
                        .padding(horizontal = 4.dp),
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(color = BentoBorderLight, thickness = 0.5.dp)
            }
        }

        // Right Column: Output logs screen
        val activeItem = deliverables[selectedDeliverableIndex]
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = activeItem.hdrTitle,
                fontSize = 14.sp,
                color = BentoTextNavy,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("architect_doc_container_${selectedDeliverableIndex}"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, BentoBorderLight)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            text = activeItem.docBody,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = BentoTextDark
                        )
                    }
                }
            }
        }
    }
}

data class ArchitectDeliverable(
    val tabLabel: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val hdrTitle: String,
    val docBody: String
)
