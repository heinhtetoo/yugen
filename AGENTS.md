# AGENTS.md — Guide for AI coding agents

Purpose: give a compact, actionable orientation so an AI coding agent can be immediately productive in this repository.

Checklist for the agent
- Read core entry points listed below (App class, DI modules, repositories, navigation).
- Understand the three-layer architecture (data ⇆ domain ⇆ ui) and Flow/Paging propagation.
- Know where secrets/config come from (local.properties -> BuildConfig).
- Use Gradle wrapper to generate code before edits (KSP/Hilt/Room).

Big picture (what matters)
- This is an Android Jetpack Compose app (package: `com.yugen.animeapp`).
- Key layers and directories:
  - `core/` — shared utils and theme (look at `Constants.kt` for feature flags and system instructions).
  - `data/` — local (Room DAOs & entities), remote (Retrofit service + paging sources), mappers, repositories.
      - Example: `AnimeRepositoryImpl.kt` implements remote fetch -> map -> write to Room and exposes Flows/PagingData.
  - `domain/` — pure-domain models and repository interfaces.
  - `ui/` — Compose screens, `navigation/` contains sealed `Route` (`Route.kt`) and `YugenNavGraph.kt`.
  - `di/` — Hilt modules (NetworkModule, DatabaseModule, RepositoryModule, DataStoreModule).

Data & control flows (how code moves data)
- Network (Retrofit) → DTOs in `data/remote/model` → mapper functions in `data/mapper` → Room entities → DAOs expose `Flow<List<...>>` → ViewModels collect Flows and expose UI state.
  - Paging paths use `AnimePagingSource` + `Pager` to produce `Flow<PagingData<Anime>>` (see `AnimeRepositoryImpl.getPagedAnimeListByGenreId`).
- Chat/Generative AI: streaming responses from `ChatRepositoryImpl.sendMessage()` return Flow<String> of partial chunks; agent code that integrates should collect the Flow and update UI incrementally.

Service boundaries & integrations
- Jikan API (anime data): base URL in `core/utils/Constants.kt` (`JIKAN_BASE_URL`) and Retrofit interface `JikanApiService`.
- Generative AI (Gemini): the project uses Google Generative AI client (`com.google.ai.client.generativeai`) with model `GENERATIVE_MODEL_NAME` from `Constants.kt`. API key is wired into BuildConfig (see below).
- Local persistence: Room DB `YugenDatabase` + DAOs (`AnimeDao`, `AnimeGenreDao`, `ChatDao`, `LibraryDao`, `SearchHistoryDao`).
- Preferences: Android DataStore via `DataStoreModule` and `UserPreferencesDataStore`.
- Network debugging: Chucker is enabled (`NetworkModule` adds `ChuckerInterceptor`).

Secrets and build-time config
- `app/build.gradle.kts` reads local properties into BuildConfig:
  - BuildConfig field: `GEMINI_API_KEY` <- `STAGING_GEMINI_API_KEY` from `local.properties`.
  - Example entry in `local.properties` (do NOT commit secrets):
    STAGING_GEMINI_API_KEY="<your-api-key>"
- Always run the Gradle build (see below) so generated BuildConfig / Hilt / Room classes are present.

Developer workflows (commands)
- Generate code + build (recommended first step):
  - ./gradlew clean assembleDebug
- Install on a connected device/emulator and launch:
  - ./gradlew installDebug
  - adb shell am start -n com.yugen.animeapp/.MainActivity
- Run unit tests (JVM):
  - ./gradlew test
- Run instrumented/android tests (requires device/emulator):
  - ./gradlew connectedAndroidTest
- Build release/bundle artifacts:
  - ./gradlew assembleRelease
  - AAB/APKs are under `app/build/outputs/` (or `app/release/` for artifacts already produced in this repo snapshot).

Project-specific conventions & patterns (concrete)
- Package layering: prefer `data`, `domain`, `ui`, `di`, `core`. Use repository interfaces in `domain/repository` and implementations in `data/repository` wired via `RepositoryModule`.
- Mapping centralization: all conversions between remote/local/domain appear in `data/mapper/*` (e.g., `AnimeRemoteMapper.kt`, `AnimeLocalMapper.kt`). Edit mappers when adding fields rather than changing call sites.
- Flow-first APIs: DAOs and repositories expose Kotlin `Flow` and Compose ViewModels/Composables collect Flows directly. Expect reactive streaming rather than synchronous returns.
- Paging: use `Pager` + custom `AnimePagingSource`. To debug paged loading, inspect `AnimePagingSource.load`.
- Chat streaming: `ChatRepositoryImpl.sendMessage()` returns a Flow that emits partial text; UI collects and appends. Search `ChatViewModel` and `ChatScreen` for usage.
- System-generated genre IDs: negative genre IDs are used for synthetic sections (`TOP_AIRING` -> `-1`, `TOP_UPCOMING` -> `-2`) — see `Constants.kt` and `DefaultHomeSectionType`.
- Error handling style: repositories generally catch exceptions, return empty lists or emit error UI states; follow existing lightweight try/catch patterns.

Where to look first (file pointers)
- App wiring: `YugenApplication.kt`, `MainActivity.kt`, `MyApp.kt`
- DI & infra: `app/src/main/java/com/yugen/animeapp/di/*` (NetworkModule, DatabaseModule, RepositoryModule)
- Repositories: `app/src/main/java/com/yugen/animeapp/data/repository/*` (AnimeRepositoryImpl, ChatRepositoryImpl)
- Remote API: `app/src/main/java/com/yugen/animeapp/data/remote/api/JikanApiService.kt`, `AnimePagingSource.kt`
- Mapping: `app/src/main/java/com/yugen/animeapp/data/mapper/*`
- UI & navigation: `app/src/main/java/com/yugen/animeapp/ui/navigation/*`, `ui/screen/*`
- Constants & system instructions: `app/src/main/java/com/yugen/animeapp/core/utils/Constants.kt`

Quick examples an agent may need to produce
- Streaming Chat handler (how to collect):
  - collect `chatRepository.sendMessage("hi")` Flow and append each emitted chunk to the UI buffer; at completion, repository inserts the full assistant message into DB.
- Adding a new network field:
  - Update DTO in `data/remote/model`, update mapper in `data/mapper`, update local entity if persisted (`data/local/entities`), update DAO migration if required, then run `./gradlew assembleDebug` to run KSP/Room migrations and compile.

Notes & gotchas
- Many classes are generated (Hilt, Room, BuildConfig). Always build before relying on generated symbols.
- API key is read from `local.properties` (not environment). If CI uses env vars, adapt `app/build.gradle.kts` to read env or inject via Gradle properties.
- The project uses Kotlinx serialization + Retrofit converter (not Moshi/Gson for API mapping).

If you make changes that modify DB schemas or Hilt bindings:
- Update DAOs/entities and increment Room migrations or set fallback behavior intentionally.
- Run `./gradlew clean assembleDebug` to validate generated code and Hilt components.

---
This file intentionally short and pragmatic. If you want, I can extend it with a small troubleshooting checklist (build failures, common stack traces) or add example snippets showing how ViewModels subscribe to the Flow/Paging streams.
