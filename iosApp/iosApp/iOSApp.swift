import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}

	init() {
           Log.companion.doInitLog()
           var driver = DatabaseDriverFactory()
           Database.companion.createInstance(databaseDriverFactory: driver)
        }
}