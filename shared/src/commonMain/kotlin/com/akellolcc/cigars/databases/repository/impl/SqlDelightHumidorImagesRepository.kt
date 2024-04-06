package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightHumidorImagesRepository(
    private val humidorId: Long
) : BaseRepository<CigarImage>(), ImagesRepository {

    fun allSync(): List<CigarImage> =
        roomQueries.humidorImages(humidorId, ::imageFactory).executeAsList()

    override fun observeAll(): Flow<List<CigarImage>> {
        return roomQueries.humidorImages(humidorId, ::imageFactory).asFlow()
            .mapToList(Dispatchers.IO)
    }

    override fun doUpsert(entity: CigarImage) {
        CoroutineScope(Dispatchers.Main).launch {
            val imageID = roomQueries.transactionWithResult {
                roomQueries.addImage(
                    entity.rowid,
                    entity.data_,
                    entity.type,
                    entity.image,
                    entity.notes
                )
                roomQueries.lastInsertRowId().executeAsOne()
            }
            roomQueries.addImageToHumidor(humidorId, imageID)
        }
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.removeImage(id)
        }
    }

    override fun observe(id: Long): Flow<CigarImage> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<CigarImage?> {
        TODO("Not yet implemented")
    }


    override fun contains(id: Long): Boolean {
        return roomQueries.imageExists(id).executeAsOne() != 0L
    }

}
