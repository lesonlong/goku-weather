# Weather Forecast App

The simple app is to demonstrate how the modern Enterprise Android App Architecture should be.

Clean Architecture (Mr Uncle Bob)

- The Application is organized base on the Clean Architecture with 3 modules: Domain, Presentation, Data:
    - Domain: Entities, Use Cases (Enterprise business and rules)
    - Data: Network, Database
    - Presentation: UI, Framework, Devices
    - Interfaces that help communicate between 3 modules above

![CleanArchitecture](https://user-images.githubusercontent.com/1311092/139828298-de3fd21c-52e9-4258-9a8f-3cfe5aa1af94.jpeg)

What I have achieved:

- The Clean Architecture being applied. I tried to achieve the clean architecture with the way that
  simple as much as possible, and not break any clean architecture principles
- The clean way to work together between 3 modules: Domain, Presentation, Data. The Dependency
  Inversion principle is applied, all 3 modules only depend on interfaces
- Full Kotlin language
- MVVM (Android Architecture Components) for Presentation module
- Apply LiveData mechanism
- Full required app features
- Full UnitTests (for all 3 modules: Domain, Presentation, Data)
- Input City Field min-length error handling (TextBox)
- App Exception handling (Meaningful error messages, the retry button when the app got an error)
- App Data Caching (by Room Database), and clear old cached data with the invalid weather date
- App Resilience Networking (use the app without the network (data cache) and more...)
- RateLimiter class helper to prevent the app from generating a bunch of API requests
- Secure Android app from:
    - SSL Certificate Pinning
    - Rooted device
    - Decompile APK (Proguard config for Release build. Shrink, obfuscate, and optimize the app)
    - Data transmission via the network (data traffic via https with system CA for Release build base on https://developer.android.com/training/articles/security-config)
- Accessibility for Disability Supports:
    - Talkback: Use a screen reader
    - Scaling Text: Display size and font-size: To change the size of items on your screen, adjust
      the display size or font size
- Principles being applied: SOLID, KISS, DRY, specially Dependency Inversion between Domain module
  and Presentation/Data module (High-Module vs Low-Module)
- Factory pattern, Dependency Injection pattern
- Config Checkstyle with Ktlint (run command line: ./gradlew ktlintCheck)
- Simplifying Project with a Folder-by-Feature structure

Libraries:

- Android Architecture Components (MVVM)
- AndroidX
- Android KTX
- Android Lifecycle-aware Components
- ViewModel
- Dagger 2: Android Injector + Custom Scope
- Inject dependencies into ViewModel without boilerplate factory
- LiveData
- Room database
- Kotlin Flow
- Kotlin Coroutines
- Kotlin CoroutineScope (viewModelScope)
- Retrofit 2, Okhttp3
- Single source of truth (by Room Database)
- Material Design
- Android Navigation
- Data Binding 2
- Gson
- Timber
- Ktlint
- ConstraintLayout
- Junit 4
- Mockito for UnitTests
- Kotlin Coroutines Test (kotlinx-coroutines-test)

The source code run on Android Studio Arctic Fox (2020.3.1)
