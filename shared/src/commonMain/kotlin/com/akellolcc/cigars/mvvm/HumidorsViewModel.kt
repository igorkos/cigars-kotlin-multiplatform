package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorsRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorsViewModel : ActionsViewModel<HumidorsViewModel.Action>()  {
    private val database: SqlDelightHumidorsRepository = SqlDelightHumidorsRepository(Database.getInstance().dbQueries)
    var loading by mutableStateOf(false)

    @Composable
    fun asState() : State<List<Humidor>> {
        return database.observeAll().collectAsState(listOf())
    }

    sealed interface Action {
        data class RouteToHumidor(val humidor: Humidor) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}