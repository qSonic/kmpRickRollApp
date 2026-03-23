import SwiftUI
import SharedKit

@main
struct RickRollIosApp: App {
    init() {
        KoinIos().doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
