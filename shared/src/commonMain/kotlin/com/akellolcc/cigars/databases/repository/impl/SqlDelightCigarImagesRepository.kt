/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 10:22 PM
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

package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryFactory
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.impl.queries.imageFactory
import com.akellolcc.cigars.screens.search.SearchParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    queries: ImagesDatabaseQueries
) : SqlDelightImagesRepository(queries), CigarImagesRepository {
    override fun all(sorting: SearchParam<Boolean>?, filter: List<SearchParam<*>>?): Flow<List<CigarImage>> {
        return queries.cigarImages(cigarId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
    }

    companion object Factory : RepositoryFactory<SqlDelightCigarImagesRepository>() {
        override fun factory(data: Any?): SqlDelightCigarImagesRepository {
            val queries = SqlDelightDatabase.instance.database.imagesDatabaseQueries
            return SqlDelightCigarImagesRepository(data as Long, queries)
        }
    }

}
