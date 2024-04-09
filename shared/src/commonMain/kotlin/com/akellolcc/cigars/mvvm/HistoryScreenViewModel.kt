package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.History
import dev.icerock.moko.resources.desc.StringDesc


abstract class HistoryScreenViewModel :
    BaseListViewModel<History, HistoryScreenViewModel.CigarsAction>() {

    var name by mutableStateOf("")
    abstract fun entityName(id: History): String

    sealed interface CigarsAction {
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}