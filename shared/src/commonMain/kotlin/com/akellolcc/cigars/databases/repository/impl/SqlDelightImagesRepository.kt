package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class SqlDelightImagesRepository(
    queries: CigarsDatabaseQueries
) : BaseRepository<CigarImage>(queries), ImagesRepository {

    override suspend fun doUpsert(entity: CigarImage) {
        queries.addImage(
            entity.rowid,
            entity.bytes,
            entity.type,
            entity.image,
            entity.notes,
            entity.cigarId,
            entity.humidorId
            )
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.removeImage(id)
        }
    }

    override fun observe(id: Long): Flow<CigarImage> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<CigarImage?> {
        TODO("Not yet implemented")
    }


    override fun contains(id: Long): Boolean {
        return queries.imageExists(id).executeAsOne() != 0L
    }
}
