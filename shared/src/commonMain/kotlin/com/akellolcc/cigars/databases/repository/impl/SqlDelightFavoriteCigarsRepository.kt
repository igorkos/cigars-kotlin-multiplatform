package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.cigarFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers

class SqlDelightFavoriteCigarsRepository(queries: CigarsDatabaseQueries) :
    SqlDelightCigarsRepository(queries), FavoriteCigarsRepository {
    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<Cigar>> {
        return queries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)
            .asObservable().wrap()
    }
}
