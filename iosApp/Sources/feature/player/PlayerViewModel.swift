import Foundation
import AVKit
import SharedKit
internal import Combine

struct PlayerScreenState {
    var title: String = "RickRoll Player"
    var mediaUrl: String = ""
    var isLoading: Bool = true
    var errorMessage: String?
}

@MainActor
final class PlayerViewModel: ObservableObject {
    @Published var state = PlayerScreenState()
    @Published var player: AVPlayer?

    private let playerService = PlayerService()
    private var hasLoaded = false

    func load() {
        guard !hasLoaded else {
            resumePlaybackIfNeeded()
            return
        }

        state.errorMessage = nil

        if let media = playerService.getRickRollMediaOrNull() {
            state.title = media.title
            state.mediaUrl = media.url
            state.isLoading = false

            if let url = URL(string: media.url) {
                configureAudioSession()

                let playerItem = AVPlayerItem(url: url)
                playerItem.preferredForwardBufferDuration = 1

                let newPlayer = AVPlayer(playerItem: playerItem)
                newPlayer.automaticallyWaitsToMinimizeStalling = false

                player = newPlayer
                hasLoaded = true
                newPlayer.playImmediately(atRate: 1.0)
            } else {
                state.errorMessage = "Некорректный URL видео"
            }
        } else {
            state.isLoading = false
            state.errorMessage = "Не удалось загрузить видео"
        }
    }

    func resumePlaybackIfNeeded() {
        guard let currentPlayer = player else { return }

        currentPlayer.playImmediately(atRate: 1.0)
    }

    func pausePlayback() {
        player?.pause()
    }

    private func configureAudioSession() {
        do {
            let session = AVAudioSession.sharedInstance()
            try session.setCategory(.playback, mode: .moviePlayback, options: [])
            try session.setActive(true)
        } catch {
            state.errorMessage = "Не удалось настроить аудио"
        }
    }
}
