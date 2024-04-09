package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.Humidor

interface HistoryRepository : Repository<History> {
    suspend fun cigar(id: Long): Cigar?
    suspend fun humidor(id: Long): Humidor?

    fun humidorName(id: Long): String

    fun cigarName(id: Long): String
}
