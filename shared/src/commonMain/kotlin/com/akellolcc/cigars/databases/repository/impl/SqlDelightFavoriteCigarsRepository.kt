package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightFavoriteCigarsRepository(queries: CigarsDatabaseQueries) :
    SqlDelightCigarsRepository(queries), FavoriteCigarsRepository {
    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<Cigar>> {
        return queries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)
    }

    override fun doDelete(id: Long) {}

}
