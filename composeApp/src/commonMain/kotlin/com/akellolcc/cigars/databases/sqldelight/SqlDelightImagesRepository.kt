/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:08 PM
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
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.sqldelight.queries.ImagesTableQueries
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.flow.Flow

abstract class SqlDelightImagesRepository(
    protected val queries: ImagesDatabaseQueries,
    protected val id: Pair<String, Long>
) : SQLDelightBaseRepository<CigarImage>(ImagesTableQueries(queries)), ImagesRepository {
    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): List<CigarImage> {
        return allSync(sorting, filter, id)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): Flow<List<CigarImage>> {
        return all(sorting, filter, id)
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): Flow<PagingData<CigarImage>> {
        return paging(sorting, filter, id)
    }

    override fun count(): Long {
        return count(id)
    }

}
