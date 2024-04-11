package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc


class MainScreenViewModel : ActionsViewModel<MainScreenViewModel.MainScreenActions>() {

    var isTabsVisible: Boolean = true
        set(value) {
            field = value
            Log.debug("Setting tabs visible $value")
            sendEvent(MainScreenActions.TabsVisibility(value))
        }

    var isDrawerVisible: Boolean = false
        set(value) {
            field = value
            sendEvent(MainScreenActions.OpenDrawer(value))
        }

    sealed interface MainScreenActions {
        data class OpenDrawer(val isVisible: Boolean = false) : MainScreenActions
        data class TabsVisibility(val isVisible: Boolean) : MainScreenActions
        data class ShowError(val error: StringDesc) : MainScreenActions
    }
}