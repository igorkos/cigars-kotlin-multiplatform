package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.repository.impl.queries.ImagesTableQueries

abstract class SqlDelightImagesRepository(
    protected val queries: ImagesDatabaseQueries
) : BaseRepository<CigarImage>(ImagesTableQueries(queries)), ImagesRepository {

    override suspend fun doUpsert(entity: CigarImage, add: Boolean) {
        if (add) {
            queries.add(
                entity.bytes,
                entity.type,
                entity.image,
                entity.notes,
                entity.cigarId,
                entity.humidorId
            )
        } else {
            queries.update(
                entity.bytes,
                entity.type,
                entity.image,
                entity.notes,
                entity.rowid
            )
        }
    }
}
