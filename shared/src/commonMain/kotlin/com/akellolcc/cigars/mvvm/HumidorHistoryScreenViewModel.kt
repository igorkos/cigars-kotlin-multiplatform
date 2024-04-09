package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HistoryRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorHistoryScreenViewModel(val humidor: Humidor) : HistoryScreenViewModel() {
    override val repository: HistoryRepository = database.getRepository(RepositoryType.HumidorHistory, humidor.rowid)
    init {
        name = humidor.name
    }
    override fun entityName(history: History): String {
        if (history.cigarId < 0) return humidor.name
        return repository.cigarName(history.cigarId)
    }
}