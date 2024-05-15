/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 8:41 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.databases.sqldelight

import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.sqldelight.queries.ImagesTableQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class SqlDelightImagesRepository(
    protected val queries: ImagesDatabaseQueries
) : SQLDelightBaseRepository<CigarImage>(ImagesTableQueries(queries)), ImagesRepository {

    override fun doUpsert(entity: CigarImage, add: Boolean): Flow<CigarImage> {
        return flow {
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
            emit(entity)
        }
    }
}
