/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:28 PM
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

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.sqldelight.queries.imageFactory
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightHumidorImagesRepository(
    private val humidorId: Long,
    queries: ImagesDatabaseQueries
) : SqlDelightImagesRepository(queries), HumidorImagesRepository {

    override fun all(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?, page: Int): Flow<List<CigarImage>> {
        return queries.humidorImages(humidorId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
    }

    companion object Factory : ObjectFactory<SqlDelightHumidorImagesRepository>() {
        override fun factory(data: Any?): SqlDelightHumidorImagesRepository {
            val queries = SqlDelightDatabase.instance.database.imagesDatabaseQueries
            return SqlDelightHumidorImagesRepository(data as Long, queries)
        }
    }
}
