/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 2:59 PM
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
 */

package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryFactory
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.cigarFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers

class SqlDelightFavoriteCigarsRepository(queries: CigarsDatabaseQueries) :
    SqlDelightCigarsRepository(queries), FavoriteCigarsRepository {
    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<Cigar>> {
        return queries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)
            .asObservable().wrap()
    }

    companion object Factory : RepositoryFactory<SqlDelightFavoriteCigarsRepository>() {
        override fun factory(data: Any?): SqlDelightFavoriteCigarsRepository {
            val queries = SqlDelightDatabase.instance.database.cigarsDatabaseQueries
            return SqlDelightFavoriteCigarsRepository(queries)
        }
    }
}
