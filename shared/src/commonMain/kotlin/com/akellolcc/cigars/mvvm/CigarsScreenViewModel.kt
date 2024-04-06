package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


class CigarsScreenViewModel : BaseListViewModel<Cigar, CigarsScreenViewModel.CigarsAction>() {
    override val database: SqlDelightCigarsRepository = SqlDelightCigarsRepository()

    fun cigarSelected(cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    @Composable
    override fun asState(): State<List<Cigar>> {
        return database.observeAll().collectAsState(listOf())
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}