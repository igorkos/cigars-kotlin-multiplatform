package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
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

    override fun add(entity: CigarImage, callback: (suspend (Long) -> Unit)?) {
        super.add(entity) {
            queries.addImageToHumidor(humidorId, it)
        }
    }

}
