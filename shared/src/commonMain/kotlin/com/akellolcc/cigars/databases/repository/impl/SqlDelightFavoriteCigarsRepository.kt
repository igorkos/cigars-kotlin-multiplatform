/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 8:54 PM
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
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.cigarFactory
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightFavoriteCigarsRepository(queries: CigarsDatabaseQueries) :
    SqlDelightCigarsRepository(queries), FavoriteCigarsRepository {
    override fun all(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?): Flow<List<Cigar>> {
        return queries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.IO)
    }

    override fun count(): Long {
        return queries.favoriteCount().executeAsOne()
    }

    companion object Factory : ObjectFactory<SqlDelightFavoriteCigarsRepository>() {
        override fun factory(data: Any?): SqlDelightFavoriteCigarsRepository {
            val queries = SqlDelightDatabase.instance.database.cigarsDatabaseQueries
            return SqlDelightFavoriteCigarsRepository(queries)
        }
    }
}
