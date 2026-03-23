# KMP RickRoll Template

Template для MVP-проекта с кроссплатформенными data/domain слоями и общим DI в `shared`.

## Что есть

- Авторизация (`login/register`) через локальную БД (SQLDelight)
- Экран плеера с RickRoll MP4
- Вертикальный full-screen плеер на iOS и Android (поведение как в short-video лентах)
- Нативные контролы плеера на iOS/Android + аппаратные кнопки громкости для media stream
- Базовая MVI-обертка для Android presentation-слоя (`state` + `event` + `effect`)

RickRoll URL:
`https://github.com/ShatteredDisk/rickroll/raw/refs/heads/main/rickroll.mp4`

## Архитектура

### Shared

- `shared:core`:
  - `RequestResult<T>` и `RequestErrorCode`
- `shared:auth_domain`:
  - auth-контракты (`AuthRepository`), модели, интеракторы
  - iOS wrapper `AuthService`
- `shared:auth_data`:
  - реализация `AuthRepository`, локальная БД, hasher
- `shared:player_domain`:
  - player-контракты (`MediaRepository`), модели, интеракторы
  - iOS wrapper `PlayerService`
- `shared:player_data`:
  - реализация `MediaRepository`
- `shared`:
  - umbrella-модуль для `SharedKit`
  - общий Koin (`initKoin`, `initKoinAndroid`, `initKoinIos`)

### Android

- Jetpack Compose + ViewModel
- Koin граф собирается в shared, Android добавляет только presentation-модуль
- ViewModel работают через интеракторы
- `MviViewModel` в `androidApp/src/main/kotlin/com/rickroll/template/android/presentation/mvi/MviViewModel.kt`
  - `state: StateFlow<State>`
  - `onEvent(event)`
  - `effect: SharedFlow<Effect>` для одноразовых side effects

### iOS

- SwiftUI + MVVM
- ViewModel работают через iOS wrappers `AuthService` / `PlayerService`
- `PlayerView` использует full-screen вертикальный рендер (`aspectFill`) и системные контролы `AVPlayerViewController`

## Структура

```text
androidApp/
iosApp/
shared/
  core/
  auth_domain/
  auth_data/
  player_domain/
  player_data/
  (umbrella)
```
