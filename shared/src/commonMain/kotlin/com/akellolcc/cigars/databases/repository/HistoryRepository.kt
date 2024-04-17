package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.History

interface HistoryRepository : Repository<History> {
    fun humidorName(id: Long): String

    fun cigarName(id: Long): String
}
