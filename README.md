# Yūgen (幽玄) — Modern Anime Tracker & Explorer

[![Android API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat-square)](https://developer.android.com/about/dashboards)
[![Target API](https://img.shields.io/badge/Target-API%2036-blue.svg?style=flat-square)](https://developer.android.com/about/versions/16)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-purple.svg?style=flat-square)](https://kotlinlang.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg?style=flat-square)](https://developer.android.com/topic/architecture)

**Yūgen** (com.yugen.animeapp) is a production-ready, feature-rich Android application designed for discovering, tracking, and managing anime series. Built completely from scratch using modern Android development practices, the application showcases a highly decoupled, maintainable, and scalable architecture optimized for performance and type safety.

The application leverages the public **Jikan API v4** (the unofficial MyAnimeList wrapper) to fetch real-time anime data and utilizes a robust local caching system to provide an offline-first capability for key tracking interactions.

---

## 🚀 Key Features

* **Dynamic Discovery (Home):** Curated rows featuring Top Airing, Top Upcoming, and critically acclaimed Award-Winning anime, alongside genre-segmented carousels.
* **Deep-Dive Exploration (Details):** Full synopsis, scheduling metadata, genre mappings, and contextual recommendations for every series.
* **Advanced Search & Local History:** Real-time query filtering with automated local caching of recent search history for rapid retrieval.
* **Personal Anime Library & Tracking:** Robust local database management handling user watch states (`Watching`, `Completed`, `On Hold`, `Dropped`, `Plan to Watch`).
* **Favorites Subsystem:** Separate reactive indexing for lightning-fast toggling of core user favorites.
* **Gamified User Profiles:** Dynamic user avatar milestone system scaling automatically based on tracking thresholds (e.g., unlocked at 10 and 30 library entries), a custom profile management layout (max 24 chars), and localized theme controls.
* **Delightful First Run:** Polished onboarding user experience preventing UI friction for first-time installations.

---

## 🛠️ Technology Stack & Tooling

| Layer / Component | Tooling / Libraries Used | Purpose & Implementation |
| :--- | :--- | :--- |
| **Language** | Kotlin | Primary language leveraging modern syntactical features, coroutines, and type-safe properties. |
| **UI Framework** | Jetpack Compose + Material 3 | 100% declarative, component-driven reactive UI styling adhering to the latest Material Design patterns. |
| **Dependency Injection** | Hilt (Dagger 2 Core) | Compile-time safe dependency injection tree maximizing component isolation and testability. |
| **Asynchronous & Flows** | Kotlin Coroutines + StateFlow | Unidirectional reactive state management and thread-safe background execution loops. |
| **Networking** | Retrofit + OkHttp + kotlinx.serialization | REST communication configured with polymorphic JSON serialization and localized logging interceptors. |
| **Local Storage** | Room Database | SQLite abstraction layer providing type-safe querying and reactive data streams via Kotlin Flows. |
| **Preferences Storage** | Preferences DataStore | Modern, asynchronous key-value storage resolving thread-blocking vulnerabilities found in standard SharedPreferences. |
| **Image Pipeline** | Coil (Compose Extension) | Asynchronous memory/disk image caching and loading optimized for Jetpack Compose UI compositions. |
| **Pagination Engine** | Paging 3 (Jetpack) | Optimized chunk-loading configuration utilizing `PagingSource` for frictionless list streams over web endpoints. |
| **Navigation Wrapper** | Jetpack Compose Navigation | Modern, completely compile-time type-safe view-routing using `@Serializable` structural arguments. |
| **Build Configuration** | Gradle (Kotlin DSL) + KSP | Strict configuration-as-code scripting utilizing the Kotlin Symbol Processing (KSP) engine for fast code generation. |
| **Debugging / Analysis** | Chucker | In-app HTTP inspector providing immediate network telemetry overlay inside debug configurations. |

---

## 📐 Architecture Overview

Yūgen strictly implements **Clean Architecture** patterns distributed across three distinct layers, ensuring complete separation of concerns and high testability. Data flow strictly adheres to **Unidirectional Data Flow (UDF)** combined with the **MVVM (Model-View-ViewModel)** UI presentation pattern.

```
                    ┌────────────────────────┐
                    │       ui / Layer       │  (Jetpack Compose UI)
                    └───────────┬────────────┘
                                │ Observes UIState
                                ▼
                    ┌────────────────────────┐
                    │    viewmodel / Layer   │  (Exposes StateFlow)
                    └───────────┬────────────┘
                                │ Invokes Repositories
                                ▼
                    ┌────────────────────────┐
                    │     domain / Layer     │  (Pure Business Logic / Interfaces)
                    └───────────┬────────────┘
                                │ Implemented By
                                ▼
                    ┌────────────────────────┐
                    │      data / Layer      │  (Retrofit APIs, Room DAOs, DataStore)
                    └────────────────────────┘
```

### 1. `domain/` — Core Enterprise & Business Rules
This is a **pure Kotlin layer** completely decoupled from any Android platform dependencies. It acts as the single source of truth for application capability.
* **Models:** Definitively declares enterprise structural entities (`Anime`, `AnimeDetails`, `AnimeGenre`, `WatchStatus`, `ThemePreference`).
* **Repository Interfaces:** Outlines structural blueprints for data interaction, ensuring the domain layer remains agnostic of infrastructure data choices (`AnimeRepository`, `JikanRepository`, `LibraryRepository`, `UserPreferencesRepository`).

### 2. `data/` — Infrastructure & Data Sourcing
Manages data access strategies, API consumption, routing operations, and persistent local caching infrastructure.
* **`remote/`:** Encapsulates the network boundary, configuring `JikanApiService` (Retrofit), structural API network objects (DTOs), and custom `AnimePagingSource` endpoints handling scalable pagination loops.
* **`local/`:** Encapsulates persistent device states using `YugenDatabase` (Room), structural SQLite schema wrappers (Entities), custom Key-Value engines (DataStore), and targeted reactive data interaction interfaces:
    * `AnimeDao` & `AnimeGenreDao` — Aggregating metadata lookup pipelines.
    * `FavouriteAnimeDao` — Facilitating independent user-favorite lists.
    * `LibraryDao` — Driving CRUD pipelines mapped to specific watch states.
    * `SearchHistoryDao` — Logging localized textual parameters.
* **`repository/`:** Concrete implementations of domain interfaces (`JikanRepositoryImpl`, `AnimeRepositoryImpl`, etc.), orchestrating network fallback behaviors, memory configurations, and storage orchestration.
* **`mapper/`:** Strictly decoupled layer translating remote network payloads (DTOs) or local Room schema components directly into pure domain entities, preventing structural leakage across components.

### 3. `ui/` — Presentation Layer (Declarative MVVM)
Constructed entirely using reactive Compose layouts utilizing custom states exposed through explicit architectural loops. The presentation layer is modularly grouped by feature domains:
* **Feature Modules:** `home/`, `search/`, `animedetails/`, `animelist/`, `library/`, `profile/`, `onboarding/`, `splash/`.
* **Feature Architecture Components:** Each feature scope is compartmentalized into three primary files:
    * `*Screen.kt`: Pure, immutable Composable UI code mapping declared layouts against discrete UI states.
    * `*ViewModel.kt`: Processes user events and interacts with Domain layers to update structural variables safely.
    * `*UiState.kt`: Explicit immutable data class configurations describing the exact layout state at any point in time.

### 4. `di/` — Dependency Injection Architecture
Hilt configuration modules that decouple initialization logic and build structural component trees cleanly:
* `NetworkModule`: Provisions thread-safe singletons for Retrofit, OkHttp, and serialization components.
* `DatabaseModule`: Orchestrates the initialization patterns for `YugenDatabase` and extracts target DAO interfaces.
* `DataStoreModule`: Provides isolated access pools for localized configuration stores.
* `RepositoryModule`: Utilizes clean `@Binds` annotations to wire abstract domain interfaces directly to underlying structural data classes.

---

## 🗺️ Screen Navigation & Routing Topography

Navigation rules utilize a type-safe Compose routing graph controlled through `@Serializable sealed interface Route` models, preventing traditional string runtime crashes during navigation steps.

```
[Splash Screen] ──────────► [Onboarding Screen] (First Launch Only)
                                  │
                                  ▼
                        ┌───────────────────┐
                        │   MainApp Graph   │
                        └─────────┬─────────┘
                                  │
         ┌────────────────────────┼────────────────────────┐
         ▼                        ▼                        ▼
  ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
  │  HomeGraph   │         │ LibraryGraph │         │ ProfileGraph │
  └──────┬───────┘         └──────┬───────┘         └──────────────┘
         ├─► Home                 ├─► Library
         ├─► Search               └─► AnimeDetails (Library View)
         ├─► AnimeList (Genres)
         └─► AnimeDetails (Home View)
```

---

## 🛠️ Code Quality Highlights & Engineering Practices

* **Offline-First Strategy Architecture:** The architecture is structured to support smooth visual experiences by utilizing localized cache tables as predictable state bridges.
* **Type-Safe Routing Architecture:** Avoids unsafe string parameters or primitive key bindings by validating deep screens via robust structural models enforced by compiler checks.
* **Granular Layer Decoupling:** Domain layers do not import any framework code, making them highly testable with standard JUnit engines without requiring mock device runtime setups.
* **Compile-Time Verification:** Maximizes utilization of `KSP` for type compilation checks, preventing unexpected application runtime faults.