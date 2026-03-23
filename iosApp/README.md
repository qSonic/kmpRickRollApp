# iOS App (SwiftUI + MVVM)

`iosApp` содержит presentation-слой и использует `SharedKit.framework` из KMP.

Минимальная структура:
- `Sources/app/AppEntry.swift` стартует Koin из shared (`initKoinIos` через `KoinIos`)
- `Sources/app/RootView.swift` переключает `AuthView` и `PlayerView` по session state
- `Sources/feature/auth/*` и `Sources/feature/player/*` реализуют MVVM state
- shared wrappers `AuthService` и `PlayerService` инкапсулируют domain use cases

Плеер:
- full-screen вертикальный режим (`AVPlayerViewController` + `resizeAspectFill`)
- нативные контролы воспроизведения
- ускоренный старт видео и настройка `AVAudioSession` для media-громкости

Для запуска создай Xcode project и подключи `SharedKit` framework из модуля `:shared`.
