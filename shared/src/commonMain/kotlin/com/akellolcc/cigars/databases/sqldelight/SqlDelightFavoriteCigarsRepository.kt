/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:12 PM
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

import androidx.paging.PagingData
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.sqldelight.queries.FAVORITES_ID
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.Flow

class SqlDelightFavoriteCigarsRepository(queries: CigarsDatabaseQueries) :
    SqlDelightCigarsRepository(queries), FavoriteCigarsRepository {

    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): List<Cigar> {
        val filterFav = FiltersList((filter ?: emptyList()) + FilterParameter(FAVORITES_ID, true))
        return super.allSync(sorting, filterFav)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): Flow<List<Cigar>> {
        val filterFav = FiltersList((filter ?: emptyList()) + FilterParameter(FAVORITES_ID, true))
        return super.all(sorting, filterFav)
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
    ): Flow<PagingData<Cigar>> {
        val filterFav = FiltersList((filter ?: emptyList()) + FilterParameter(FAVORITES_ID, true))
        return super.paging(sorting, filterFav, Pair(FAVORITES_ID, true))
    }

    override fun count(): Long {
        return super.count(Pair(FAVORITES_ID, true))
    }

    companion object Factory : ObjectFactory<SqlDelightFavoriteCigarsRepository>() {
        override fun factory(data: Any?): SqlDelightFavoriteCigarsRepository {
            val queries = SqlDelightDatabase.instance.database.cigarsDatabaseQueries
            return SqlDelightFavoriteCigarsRepository(queries)
        }
    }
}
