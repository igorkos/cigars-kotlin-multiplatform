package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import dev.icerock.moko.resources.desc.StringDesc


class CigarHistoryScreenViewModel(val cigar: Cigar) : HistoryScreenViewModel() {
    override val repository: HistoryRepository = database.getRepository(RepositoryType.CigarHistory, cigar.rowid)

    init {
        name = cigar.name
    }
    override fun entityName(history: History): String {
        return repository.humidorName(history.humidorId)
    }
}