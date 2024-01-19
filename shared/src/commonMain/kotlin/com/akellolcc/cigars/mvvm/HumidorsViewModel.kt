package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Humidor
import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow


class HumidorsViewModel : ViewModel() {

    private val database: Database = Database.getInstance();

    private val _humidors: CMutableStateFlow<List<Humidor>> = MutableStateFlow(database.humidors()).cMutableStateFlow()
    val humidors: StateFlow<List<Humidor>> = _humidors

    private val _actions = Channel<Action>(Channel.BUFFERED)
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    sealed interface Action {
        object RouteToSuccess : Action
        data class ShowError(val error: StringDesc) : Action
    }
}