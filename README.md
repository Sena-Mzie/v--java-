# VUKA Safety Intelligence Platform
### Native Android Application — Java

> **See the Risk. Know the Journey. Get Home.**

VUKA is a safety intelligence platform built for the e-hailing industry. It operates as an independent safety layer alongside platforms such as Uber, Bolt, and inDrive — providing driver verification, trip risk assessment, real-time journey monitoring, emergency assistance, and community incident reporting.

---

## Table of Contents

1. [What VUKA Is (and Is Not)](#what-vuka-is-and-is-not)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Getting Started](#getting-started)
5. [Demo Credentials](#demo-credentials)
6. [Application Flows](#application-flows)
7. [Architecture](#architecture)
8. [Design System](#design-system)
9. [Security and Safety Implementation](#security-and-safety-implementation)
10. [Simulated Features](#simulated-features)
11. [External Integrations Required](#external-integrations-required)
12. [Build and Test Results](#build-and-test-results)
13. [Known Limitations](#known-limitations)

---

## What VUKA Is (and Is Not)

**VUKA is a safety platform.** It is not a ride-booking or e-hailing application.

VUKA does not provide:
- Ride booking
- Driver matching or dispatch
- Fare calculation or payment processing
- Transport marketplace functionality

VUKA does provide:
- Driver identity and vehicle verification
- Trip risk assessment and route safety intelligence
- Real-time journey monitoring with route deviation detection
- Emergency SOS with trusted contact notification
- Duress PIN for silent escalation under threat
- Community incident reporting
- Administrative moderation and audit tools

---

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java (sole application language) |
| Platform | Native Android |
| Minimum SDK | API 24 (Android 7.0) |
| Target SDK | API 34 (Android 14) |
| Build System | Gradle 9.3.1 |
| Android Gradle Plugin | 8.7.0 |
| UI Framework | XML layouts + Android Material Components 1.11.0 |
| Navigation | Fragment-based with centralised `NavigationManager` |
| State Management | Java ViewModel pattern + `MockDataRepository` |
| Local Persistence | SharedPreferences (session) + Room (dependency ready) |
| Image Loading | Glide 4.16.0 |
| Testing | JUnit 4 + AndroidX Instrumentation |

---

## Project Structure

```
v (java)/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/vuka/app/
│           │   ├── activities/          # SplashActivity, MainActivity, AdminActivity,
│           │   │                        # TripMonitorActivity, TrustedContactLiveActivity
│           │   ├── adapters/            # RecyclerView adapters for all list screens
│           │   ├── fragments/
│           │   │   ├── admin/           # AdminDashboardFragment
│           │   │   ├── auth/            # LoginFragment, OtpVerifyFragment
│           │   │   ├── contacts/        # TrustedContactsFragment, TrustedContactLiveFragment
│           │   │   ├── dashboard/       # DriverDashboardFragment, PassengerDashboardFragment
│           │   │   ├── driver/          # DriverRegistrationFragment, DriverVerificationFragment
│           │   │   ├── emergency/       # SosEmergencyFragment, DuressPinFragment
│           │   │   ├── incidents/       # ReportIncidentFragment, ReportHistoryFragment
│           │   │   ├── onboarding/      # PermissionsFragment, RoleSelectionFragment
│           │   │   ├── passenger/       # PassengerProfileFragment
│           │   │   ├── profile/         # ProfileFragment
│           │   │   └── trips/           # CreateTripFragment, LoadingAnalysisFragment,
│           │   │                        # TripSafetyProfileFragment, ActiveMonitoringFragment,
│           │   │                        # RouteDeviationFragment, SafeArrivalFragment,
│           │   │                        # TripHistoryFragment
│           │   ├── models/              # User, DriverProfile, TrustedContact, Trip,
│           │   │                        # TripSafetyProfile, RiskFactor, IncidentReport,
│           │   │                        # Alert, AuditLog
│           │   ├── navigation/          # NavigationManager
│           │   ├── repositories/        # MockDataRepository (central data store)
│           │   ├── utils/               # ValidationUtils, RiskColorUtils, TimeUtils,
│           │   │                        # MapSimulationView, SosHoldButton
│           │   └── VukaApplication.java # Application class and SharedPreferences keys
│           │
│           └── res/
│               ├── color/               # Color state lists (bottom nav selector)
│               ├── drawable/            # Backgrounds, shapes, button states, icons
│               ├── layout/              # 31 XML layout files
│               ├── mipmap-*/            # Launcher icons (all densities + anydpi-v26)
│               ├── values/              # colors.xml, strings.xml, themes.xml, dimens.xml
│               └── xml/                 # file_paths.xml (FileProvider)
│
├── gradle/wrapper/                      # Gradle 9.3.1 wrapper
├── gradlew.bat                          # Windows Gradle wrapper script
├── build.gradle                         # Root build config
├── settings.gradle                      # Module includes
└── gradle.properties                    # Gradle JVM and AndroidX flags
```

---

## Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK 17 or higher** (JDK 25 confirmed working)
- **Android SDK** with platform API 34 installed
- **Build Tools** 34.0.0 or later
- An Android emulator or physical device running API 24+

### Opening the Project

1. Launch Android Studio.
2. Select **File → Open**.
3. Navigate to `c:\Users\FMTALI-16\Desktop\v (java)` and click **OK**.
4. Allow Gradle sync to complete. All dependencies download from Maven Central and Google.

### Building from the Command Line

```bat
cd "c:\Users\FMTALI-16\Desktop\v (java)"

REM Clean build
gradlew.bat clean

REM Build debug APK
gradlew.bat assembleDebug

REM Run unit tests
gradlew.bat test

REM Build and test in one pass
gradlew.bat clean assembleDebug test
```

The debug APK is written to:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Running on a Device or Emulator

1. In Android Studio, select your target device from the device dropdown.
2. Click **Run** (▶) or press `Shift+F10`.
3. The app launches at the VUKA splash screen.

---

## Demo Credentials

| Credential | Value | Purpose |
|---|---|---|
| OTP code | `123456` | Used on the OTP verification screen for all users |
| Normal PIN | `1234` | Safety check-in — confirms safe, continues trip |
| Duress PIN | `9999` | Visually identical to normal check-in; triggers silent simulated escalation |
| Admin phone | `+27800000001` | Enter on the login screen to access the admin dashboard |

> **Note:** No real SMS is sent. The OTP is displayed on-screen as a demonstration hint. No real emergency services are contacted at any point.

---

## Application Flows

All 17 UX/UI flows from the product specification are implemented.

| # | Flow | Entry Point |
|---|---|---|
| 1 | First-time onboarding — splash, permissions, role selection | Automatic on first launch |
| 2 | Authentication — SA phone number, OTP verification | Login screen |
| 3 | Driver profile creation — personal details, vehicle, documents | After OTP as a driver |
| 4 | Passenger profile creation — name, optional photo, contacts intro | After OTP as a passenger |
| 5 | Trusted contact management — add, edit, remove, consent simulation | Contacts tab / dashboard shortcut |
| 6 | Home dashboards — role-specific, dynamic data, persistent SOS | After login |
| 7 | Create trip — pickup, destination, date/time, 4-step analysis animation | Dashboard CTA |
| 8 | Trip safety profile — risk scores, risk factors, safer route selection | After analysis |
| 9 | Driver verification — QR scan simulation, verified/mismatch states | Dashboard shortcut |
| 10 | Active trip monitoring — map simulation, driver info, ETA countdown | After profile → Start Trip |
| 11 | Route deviation — alert, I'm OK / Expected Detour / I Need Help | Auto-triggered after 15s in monitoring (demo) |
| 12 | Duress PIN — numpad check-in with normal and silent duress paths | Safety Check-In button |
| 13 | SOS emergency — 2-second hold, progress ring, simulated notifications | SOS button on any main screen |
| 14 | Incident reporting — type chips, location, photo, description, submit | Reports tab / dashboard card |
| 15 | Safe arrival — confirmation prompt, trip summary, contact notification | Auto on ETA = 0 or manual |
| 16 | Trusted contact live view — traveller info, map simulation, driver details | TrustedContactLiveActivity |
| 17 | Admin dashboard — KPI cards, report moderation, driver verification queue, audit logs | Admin login |

---

## Architecture

VUKA uses a layered architecture with clear separation between presentation, data, and utilities.

### Presentation Layer
- **Activities** own navigation boundaries and lifecycle.
- **Fragments** are the primary screens. Each fragment inflates its XML layout and wires click listeners in `onViewCreated`.
- **Adapters** drive all RecyclerView lists. Each adapter holds a typed list and exposes callback interfaces for user actions.
- **Custom Views** (`SosHoldButton`, `MapSimulationView`) encapsulate specialised interaction logic.

### Data Layer
- **`MockDataRepository`** is a singleton that provides stateful in-memory data for the entire demo session. It seeds realistic demo records on first access and exposes typed methods for every data operation.
- **Models** are plain Java objects with no Android dependencies — fully unit testable.
- **`VukaApplication`** holds the `SharedPreferences` reference and convenience accessors for session state (login status, user role, name, phone).

### Navigation
- **`NavigationManager`** wraps `FragmentManager` and provides `navigateTo`, `navigateToRoot`, and `switchTab` — a single consistent place for all screen transitions.
- Each major feature uses a separate fragment sub-package. The admin and trip monitor flows have their own activities to maintain clean separation.

### Service Interfaces
Every external integration point (authentication, SMS, notifications, risk intelligence, trip tracking) is designed so the `MockDataRepository` can be replaced by a Retrofit or other HTTP client implementation without touching any fragment or adapter code.

---

## Design System

The visual design follows the VUKA reference image: dark navy background, gold/amber accent, card-based layouts with subtle borders.

### Colour Palette

| Token | Hex | Usage |
|---|---|---|
| `vuka_navy_dark` | `#0A0E27` | App background |
| `vuka_navy_card` | `#151C47` | Card surfaces |
| `vuka_gold` | `#F5A623` | Primary accent, CTAs, highlights |
| `risk_low` | `#00C853` | Low risk indicators |
| `risk_moderate` | `#FFD600` | Moderate risk |
| `risk_elevated` | `#FF6D00` | Elevated risk |
| `risk_high` | `#D50000` | High risk |
| `sos_red` | `#FF1744` | Emergency / SOS states |
| `vuka_text_primary` | `#FFFFFF` | Primary body text |
| `vuka_text_secondary` | `#B0B8D4` | Secondary / hint text |

### Risk Status Communication
Risk level is always communicated with **both colour and text label** — never colour alone — to meet accessibility requirements.

### Reusable Style Tokens
All buttons, cards, inputs, risk badges, dialogs, and status labels are defined in `res/values/themes.xml` and referenced consistently across all layouts.

---

## Security and Safety Implementation

### SOS Button (`SosHoldButton`)
- Requires a continuous 2-second press to activate, preventing accidental triggers.
- Displays a radial progress ring during the hold.
- Triggers device vibration on activation.
- An accessible alternative button is provided on the same screen for users who cannot perform a press-and-hold gesture.

### Duress PIN (`DuressPinFragment`)
- Two PINs are configured: a normal PIN (`1234`) and a duress PIN (`9999`).
- Both paths produce an identical visible response ("Check-in confirmed — monitoring continues").
- The duress path triggers `MockDataRepository.activateDuress()` silently in the background.
- **Entered digits are never stored, logged, or written to any output stream.** The PIN string is compared in memory and immediately cleared after comparison.
- In a production system, PINs must be hashed using a secure algorithm (e.g. Argon2 or bcrypt) before any persistence.

### Permissions
- Each permission is requested individually with a clear explanation before the system prompt appears.
- The app functions in a limited mode if permissions are denied — the Skip option on the permissions screen is fully supported.
- Permissions are not assumed to be granted. Each feature checks the relevant permission before use.

### Role-Based Access
- The admin dashboard is only reachable via `AdminActivity`, which checks `VukaApplication.isAdmin()` on creation and redirects if the check fails.
- Driver-only and passenger-only screens are only navigated to from role-appropriate dashboards.

### Data Handling
- No sensitive credentials, API keys, or PINs are hardcoded in production paths.
- Demo credentials are only present in demo code paths and are clearly labelled.
- No personal information is written to Android logs.

---

## Simulated Features

The following features are simulated for demonstration. Each is clearly labelled in the application UI.

| Feature | Simulation | In-App Label |
|---|---|---|
| OTP delivery | Code displayed on screen | "Demo OTP: 123456" |
| GPS location | `MapSimulationView` canvas animation | "MAP SIMULATION — Not real GPS" |
| SMS to trusted contacts | Toast message | "DEMO: simulated — not sent" |
| Push notifications | In-app status update only | "DEMO: Notification simulated" |
| Emergency escalation | UI state change only | "DEMO: Notification simulated" |
| QR scanning | Demo button triggers verified state | "DEMO: Simulated verification" |
| Document upload | Toast confirmation | "DEMO: Upload simulated" |
| Risk intelligence | Randomly seeded illustrative data | "ILLUSTRATIVE DATA — Not verified crime intelligence" |
| Duress escalation | `activateDuress()` sets an in-memory flag | Silent — no real authority contact |
| Contact consent SMS | Toast notification | Demo notice in add contact dialog |
| Biometric authentication | UI scaffolding only | Not wired |
| Trusted contact browser view | Native Android screen (not a web app) | "DEMO ACCESS — Simulated trusted contact view" |

---

## External Integrations Required

The following integrations are defined as service interfaces in the codebase but require real API credentials and configuration before they carry live data.

### 1. Maps and Location
Replace `MapSimulationView` with a real map provider.

**Recommended:** Google Maps Android SDK
```groovy
// app/build.gradle
implementation 'com.google.android.gms:play-services-maps:18.2.0'
```
Set your API key in `AndroidManifest.xml`:
```xml
<meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_KEY_HERE"/>
```

### 2. SMS / Emergency Notifications
**Recommended:** Twilio Programmable SMS or Firebase Cloud Messaging

Replace the Toast-based simulation in `SosEmergencyFragment` and `TrustedContactsFragment` with the appropriate SDK calls behind the existing service interface pattern.

### 3. QR Scanning
**Recommended:** ZXing Android Embedded
```groovy
implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
```
Replace the demo scan button in `DriverVerificationFragment` with a real `IntentIntegrator` camera scan.

### 4. Authentication
**Recommended:** Firebase Authentication (phone number + OTP)
```groovy
implementation 'com.google.firebase:firebase-auth:22.3.1'
```
Replace `OtpVerifyFragment`'s mock verification with `PhoneAuthProvider` and a real SMS code flow.

### 5. Backend API
Replace `MockDataRepository` with Retrofit service implementations:
```groovy
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```
All model classes are plain Java and serialise cleanly with Gson.

### 6. Local Database (Room)
The Room dependency is already included. To activate persistence across restarts:
1. Create `@Entity`-annotated versions of the model classes.
2. Define `@Dao` interfaces for each entity.
3. Build an `@Database` class.
4. Inject the DAOs into `MockDataRepository` and replace the in-memory lists.

### 7. Trusted Contact Web View (Browser Module)
The in-app `TrustedContactLiveFragment` demonstrates the data and layout. A full browser-accessible experience requires a separate Java web module:

**Recommended stack:** Spring Boot + Thymeleaf templates
- Expose a REST endpoint `/trip/{shareToken}` returning trip, driver, and location data.
- Render a server-side HTML page with responsive CSS.
- Share the same model classes as the Android app via a common Java library module.

This is documented as a **separate outstanding deliverable**.

---

## Build and Test Results

### Build

```
BUILD SUCCESSFUL in 1m 52s
63 actionable tasks: 63 executed
```

**APK:** `app/build/outputs/apk/debug/app-debug.apk` — 6.78 MB

### Unit Tests

```
Test suite:  com.vuka.app.VukaUnitTest
Tests:       12
Failures:    0
Skipped:     0
Duration:    1.730s
Success:     100%
```

**Tests cover:**
- South African phone number validation (valid formats, invalid formats, null)
- South African ID number validation
- Vehicle registration validation
- Phone number normalisation to E.164
- Risk level label and icon generation
- User and contact initials calculation
- Trip status state machine transitions
- `MockDataRepository` seeding and data integrity
- Trusted contact add and remove operations
- SOS activation and cancellation state transitions

### Notes on Build Warnings
Two non-blocking warnings appear in every build:

1. **Java source/target version 8 deprecated** — JDK 25 warns that `-source 8 -target 8` will be removed in a future release. No impact on the current build. Resolve by updating `compileOptions` to `VERSION_17` when moving to production.

2. **Gradle 10 deprecation** — Gradle 9.x warns that certain features used by AGP 8.7.0 will be removed in Gradle 10. No impact on the current build.

---

## Known Limitations

| Limitation | Detail |
|---|---|
| No data persistence across restarts | `MockDataRepository` is in-memory. Room DAOs are not yet implemented. Trip history and contacts reset on app kill. |
| No real GPS | `MapSimulationView` draws a fixed animated canvas. It does not reflect the device's actual location. |
| No real OTP | The OTP `123456` is hardcoded. Firebase Auth integration is required for production. |
| No real emergency escalation | SOS triggers UI state changes and simulated notifications only. No external service is contacted. |
| Trusted contact web view is native only | The browser-based trusted contact experience described in the specification is implemented as a native Android screen. The Spring Boot web module is a separate deliverable. |
| Biometric authentication scaffolded only | The biometric settings toggle is present in the Security screen but is not wired to `BiometricPrompt`. |
| Lint task unstable on JDK 25 + Gradle 9.3.1 | The `lint` Gradle task fails to stop its service cleanly under JDK 25. The app builds and all unit tests pass. Use Android Studio's built-in lint inspector as the workaround. |
| Instrumented tests require a device | `VukaInstrumentedTest` requires a connected emulator or device running API 24+. No emulator is configured in this environment. |

---

## Resetting Demo Data

From the **Profile** screen, tap **Reset Demo Data** at the bottom. This clears all in-memory state and SharedPreferences and returns the app to its initial seeded state.

---

## Licence

This project is a demonstration application produced for the VUKA Safety Intelligence Platform MVP. All safety data, risk scores, and incident records are illustrative and do not represent verified crime intelligence or real-world conditions.

No production deployment, real emergency service integration, or actual user data processing should occur without appropriate security review, data protection compliance assessment, and service agreements with external providers.
