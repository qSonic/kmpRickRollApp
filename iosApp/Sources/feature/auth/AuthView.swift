import SwiftUI

struct AuthView: View {
    @ObservedObject var viewModel: AuthViewModel

    var body: some View {
        VStack(spacing: 16) {
            Text("RickRoll Auth")
                .font(.title2)

            Picker("Mode", selection: Binding(
                get: { viewModel.state.isLoginMode },
                set: { viewModel.switchMode(isLogin: $0) }
            )) {
                Text("Login").tag(true)
                Text("Register").tag(false)
            }
            .pickerStyle(.segmented)

            TextField("Логин", text: Binding(
                get: { viewModel.state.login },
                set: { viewModel.state.login = $0 }
            ))
            .textFieldStyle(.roundedBorder)

            SecureField("Пароль", text: Binding(
                get: { viewModel.state.password },
                set: { viewModel.state.password = $0 }
            ))
            .textFieldStyle(.roundedBorder)

            if let errorMessage = viewModel.state.errorMessage {
                Text(errorMessage)
                    .foregroundColor(.red)
                    .font(.footnote)
            }

            Button(viewModel.state.isLoginMode ? "Войти" : "Зарегистрироваться") {
                viewModel.submit()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.state.isLoading)
        }
        .padding(24)
    }
}
