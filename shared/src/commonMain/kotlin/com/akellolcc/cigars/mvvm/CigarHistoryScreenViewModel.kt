package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarHistoryRepository
import dev.icerock.moko.resources.desc.StringDesc


class CigarHistoryScreenViewModel(val cigar: Cigar) :
    BaseListViewModel<History, CigarHistoryScreenViewModel.CigarsAction>() {
    override val database: SqlDelightCigarHistoryRepository =
        SqlDelightCigarHistoryRepository(cigar.rowid)

    @Composable
    override fun asState(): State<List<History>> {
        return database.observeAll().collectAsState(listOf())
    }

    fun humidorName(id: Long): String {
        return database.humidorName(id)
    }

    sealed interface CigarsAction {
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}