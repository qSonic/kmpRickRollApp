import Foundation
import SharedKit
internal import Combine

struct AuthScreenState {
    var isAuthorized: Bool = false
    var isLoginMode: Bool = true
    var login: String = ""
    var password: String = ""
    var isLoading: Bool = false
    var errorMessage: String?
}

@MainActor
final class AuthViewModel: ObservableObject {
    @Published var state = AuthScreenState()

    private let authService = AuthService()

    func loadSession() {
        state.isAuthorized = authService.isAuthorized()
    }

    func switchMode(isLogin: Bool) {
        state.isLoginMode = isLogin
        state.errorMessage = nil
    }

    func submit() {
        state.isLoading = true
        state.errorMessage = nil

        let errorCode = state.isLoginMode
            ? authService.login(login: state.login, password: state.password)
            : authService.register(login: state.login, password: state.password)

        state.isLoading = false
        if errorCode == nil {
            state.password = ""
            state.isAuthorized = true
        } else {
            state.errorMessage = Self.mapError(errorCode)
        }
    }

    func logout() {
        if authService.logout() {
            state.isAuthorized = false
        }
    }

    private static func mapError(_ error: RequestErrorCode?) -> String {
        switch error {
        case .emptyLogin:
            return "Введите логин"
        case .emptyPassword:
            return "Введите пароль"
        case .loginAlreadyExists:
            return "Пользователь уже существует"
        case .invalidCredentials:
            return "Неверный логин или пароль"
        default:
            return "Ошибка авторизации"
        }
    }
}
