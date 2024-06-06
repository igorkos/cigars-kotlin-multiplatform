/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 11:00 AM
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
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.sqldelight.queries.CIGAR_ID
import com.akellolcc.cigars.utils.ObjectFactory

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    queries: ImagesDatabaseQueries
) : SqlDelightImagesRepository(queries, Pair(CIGAR_ID, cigarId)), CigarImagesRepository {

    companion object Factory : ObjectFactory<SqlDelightCigarImagesRepository>() {
        override fun factory(data: Any?): SqlDelightCigarImagesRepository {
            val queries = SqlDelightDatabase.instance.database.imagesDatabaseQueries
            return SqlDelightCigarImagesRepository(data as Long, queries)
        }
    }

}
