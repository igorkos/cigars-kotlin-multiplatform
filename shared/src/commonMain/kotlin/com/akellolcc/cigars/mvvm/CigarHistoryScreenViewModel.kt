package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import dev.icerock.moko.resources.desc.StringDesc


class CigarHistoryScreenViewModel(val cigar: Cigar) :
    BaseListViewModel<History, CigarHistoryScreenViewModel.CigarsAction>() {
    override val repository: HistoryRepository = database.getRepository(RepositoryType.CigarHistory)

    fun humidorName(id: Long): String {
        return repository.humidorName(id)
    }

    sealed interface CigarsAction {
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}