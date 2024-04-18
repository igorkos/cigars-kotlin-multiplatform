package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository


class CigarHistoryScreenViewModel(val cigar: Cigar) : HistoryScreenViewModel() {
    override val repository: HistoryRepository =
        database.getRepository(RepositoryType.CigarHistory, cigar.rowid)

    override fun entitySelected(entity: History) {
    }

    init {
        name = cigar.name
    }

    override fun entityName(id: History): String {
        return repository.humidorName(id.humidorFrom)
    }
}