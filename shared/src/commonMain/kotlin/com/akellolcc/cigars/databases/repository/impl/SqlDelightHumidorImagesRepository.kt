package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightHumidorImagesRepository(
    private val humidorId: Long,
    queries: CigarsDatabaseQueries
) : SqlDelightImagesRepository(queries) {

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<CigarImage>> {
        return queries.humidorImages(humidorId, ::imageFactory).asFlow()
            .mapToList(Dispatchers.IO)
    }

    override fun add(entity: CigarImage): ObservableWrapper<CigarImage> {
        return super.add(entity).map {
            //queries.addImageToHumidor(humidorId, it)
            it
        }.wrap()
    }

}
