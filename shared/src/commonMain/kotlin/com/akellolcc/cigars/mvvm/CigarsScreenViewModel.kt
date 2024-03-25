package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Cigar
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
import kotlinx.coroutines.launch


class CigarsScreenViewModel : ViewModel() {

    private val database: Database = Database.getInstance();

    private val _cigars: CMutableStateFlow<List<Cigar>> = MutableStateFlow(listOf<Cigar>()).cMutableStateFlow()
    val cigars: StateFlow<List<Cigar>> = _cigars

    private val _actions = Channel<Action>(Channel.BUFFERED)
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    fun fetchCigars() {
        _cigars.value = database.cigars()
    }

    fun cigarSelected( cigar: Cigar) {
        viewModelScope.launch {
            _actions.send(Action.RouteToCigar(cigar))
        }
    }

    sealed interface Action {
        data class RouteToCigar(val cigar: Cigar) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}