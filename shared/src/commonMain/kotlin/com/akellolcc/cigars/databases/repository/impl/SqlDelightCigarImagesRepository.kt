package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    queries: CigarsDatabaseQueries
) : SqlDelightImagesRepository(queries) {

    override fun observeAll(): Flow<List<CigarImage>> {
        return queries.cigarImages(cigarId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
    }

    override fun add(entity: CigarImage, callback: (suspend (Long) -> Unit)?) {
        super.add(entity) {
            queries.addImageToCigar(cigarId, it)
        }
    }
}
