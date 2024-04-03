package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


class CigarsScreenViewModel : ActionsViewModel<CigarsScreenViewModel.CigarsAction>() {
    private val database: SqlDelightCigarsRepository = SqlDelightCigarsRepository(Database.getInstance().dbQueries)
    var loading by mutableStateOf(false)
    fun cigarSelected( cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    @Composable
    fun asState() : State<List<Cigar>> {
        return database.observeAll().collectAsState(listOf())
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}