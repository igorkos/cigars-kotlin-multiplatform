import SwiftUI
import shared
import FirebaseCore
import FirebaseAnalytics

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      Platform_iosKt.doInitFirebase()
      Log.companion.doInitLog(){event in
          Analytics.logEvent(event.event.name, parameters: event.params)
      }
    return true
  }
}
@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
