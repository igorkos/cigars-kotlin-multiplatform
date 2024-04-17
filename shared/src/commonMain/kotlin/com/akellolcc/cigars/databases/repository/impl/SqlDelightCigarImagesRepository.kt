package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.impl.queries.imageFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    queries: ImagesDatabaseQueries
) : SqlDelightImagesRepository(queries) {
    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<CigarImage>> {
        return queries.cigarImages(cigarId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
            .asObservable().wrap()
    }

}
