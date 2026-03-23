import SwiftUI
import AVKit

struct PlayerView: View {
    @ObservedObject var authViewModel: AuthViewModel
    @StateObject var viewModel: PlayerViewModel

    var body: some View {
        ZStack {
            Color.black
                .ignoresSafeArea()

            if let player = viewModel.player {
                VerticalVideoPlayer(player: player)
                    .ignoresSafeArea()
            } else if viewModel.state.isLoading {
                ProgressView()
                    .tint(.white)
            }

            VStack {
                Text(viewModel.state.title)
                    .font(.title3.weight(.semibold))
                    .foregroundColor(.white)
                    .shadow(radius: 4)
                    .padding(.top, 16)

                Spacer()

                if let errorMessage = viewModel.state.errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .padding(.bottom, 8)
                }

                Button("Logout") {
                    authViewModel.logout()
                }
                .buttonStyle(.borderedProminent)
                .padding(.bottom, 24)
            }
            .padding(.horizontal, 16)
        }
        .navigationBarBackButtonHidden(true)
        .task {
            viewModel.load()
        }
        .onAppear {
            viewModel.resumePlaybackIfNeeded()
        }
        .onDisappear {
            viewModel.pausePlayback()
        }
    }
}

private struct VerticalVideoPlayer: UIViewControllerRepresentable {
    let player: AVPlayer

    func makeUIViewController(context: Context) -> AVPlayerViewController {
        let controller = AVPlayerViewController()
        controller.player = player
        controller.videoGravity = .resizeAspectFill
        controller.showsPlaybackControls = true
        controller.entersFullScreenWhenPlaybackBegins = false
        controller.exitsFullScreenWhenPlaybackEnds = false
        return controller
    }

    func updateUIViewController(_ uiViewController: AVPlayerViewController, context: Context) {
        uiViewController.player = player
        uiViewController.videoGravity = .resizeAspectFill
    }
}
