import SwiftUI

struct RootView: View {
    @StateObject private var authViewModel = AuthViewModel()
    @StateObject private var playerViewModel = PlayerViewModel()
    @State private var path: [AppRoute] = []

    var body: some View {
        NavigationStack(path: $path) {
            AuthView(viewModel: authViewModel)
                .navigationDestination(for: AppRoute.self) { route in
                    switch route {
                    case .player:
                        PlayerView(
                            authViewModel: authViewModel,
                            viewModel: playerViewModel
                        )
                    }
                }
                .onChange(of: authViewModel.state.isAuthorized) { isAuthorized in
                    if isAuthorized {
                        if path != [.player] {
                            path = [.player]
                        }
                    } else {
                        path.removeAll()
                    }
                }
                .task {
                    authViewModel.loadSession()
                    if authViewModel.state.isAuthorized {
                        path = [.player]
                    } else {
                        path.removeAll()
                    }
                }
        }
    }
}
