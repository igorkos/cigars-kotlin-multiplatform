/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/16/24, 6:35 PM
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
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.impl.queries.imageFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class SqlDelightCigarImagesRepository(
    private val cigarId: Long,
    queries: ImagesDatabaseQueries
) : SqlDelightImagesRepository(queries) {
    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<CigarImage>> {
        return queries.cigarImages(cigarId, ::imageFactory).asFlow().mapToList(Dispatchers.IO)
            .asObservable().wrap()
    }

}
