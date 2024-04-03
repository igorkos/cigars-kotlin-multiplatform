package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    private val roomQueries: CigarsDatabaseQueries,
) : BaseRepository<CigarImage>(), ImagesRepository {

    fun allSync(): List<CigarImage> = roomQueries.cigarImages(cigarId, ::imageFactory).executeAsList()

    override fun observeAll(): Flow<List<CigarImage>> {
        return roomQueries.cigarImages(cigarId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
    }
    override fun doUpsert(entity: CigarImage) {
        CoroutineScope(Dispatchers.Main).launch {
            val imageID = roomQueries.transactionWithResult {
                roomQueries.addImage(entity.rowid,
                    entity.data_,
                    entity.type,
                    entity.image,
                    entity.notes)
                roomQueries.lastInsertRowId().executeAsOne()
            }
            roomQueries.addImageToCigar(cigarId, imageID)
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

    private fun imageFactory(
        rowid: Long,
        image: String?,
        data_: ByteArray,
        notes: String?,
        type: Long?,
        cigarId: Long?,
        humidorId: Long?
    ): CigarImage {
        return CigarImage(
            rowid = rowid,
            image = image,
        data_ = data_,
        notes = notes,
        type = type,
        cigarId = cigarId,
        humidorId = humidorId
        )
    }
}
