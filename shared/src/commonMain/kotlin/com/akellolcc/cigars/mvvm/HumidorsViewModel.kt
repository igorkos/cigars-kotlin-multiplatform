package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorsRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorsViewModel : BaseListViewModel<Humidor, HumidorsViewModel.Action>() {
    override val database: SqlDelightHumidorsRepository = SqlDelightHumidorsRepository()

    @Composable
    override fun asState(): State<List<Humidor>> {
        return database.observeAll().collectAsState(listOf())
    }

    fun humidorSelected(humidor: Humidor) {
        sendEvent(Action.RouteToHumidor(humidor))
    }

    sealed interface Action {
        data class RouteToHumidor(val humidor: Humidor) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}