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

import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.Repository
import kotlinx.coroutines.runBlocking

class SqlDelightDatabase : DatabaseInterface {
    private val database = CigarsDatabase(SqlDelightDatabaseDriverFactory().createDriver())

    @Suppress("UNCHECKED_CAST")
    override fun <R> getRepository(type: RepositoryType, args: Any?): R {
        return when (type) {
            RepositoryType.Cigars -> (SqlDelightCigarsRepository(database.cigarsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.Humidors -> (SqlDelightHumidorsRepository(database.humidorsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.Favorites -> (SqlDelightFavoriteCigarsRepository(database.cigarsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.CigarImages -> (SqlDelightCigarImagesRepository(
                args as Long,
                database.imagesDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorImages -> (SqlDelightHumidorImagesRepository(
                args as Long,
                database.imagesDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.CigarHumidors -> (SqlDelightCigarHumidorsRepository(
                args as Long,
                database.humidorCigarsDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorCigars -> (SqlDelightHumidorCigarsRepository(
                args as Long,
                database.humidorCigarsDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.CigarHistory -> (SqlDelightCigarHistoryRepository(
                args as Long,
                database.historyDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorHistory -> (SqlDelightHumidorHistoryRepository(
                args as Long,
                database.historyDatabaseQueries
            ) as? R) ?: error("Invalid repository type")
        }
    }


    override fun reset() {
        runBlocking {
            database.cigarsDatabaseQueries.removeAll()
            database.humidorsDatabaseQueries.removeAll()
            database.imagesDatabaseQueries.removeAll()
            database.humidorCigarsDatabaseQueries.removeAll()
            database.historyDatabaseQueries.removeAll()
        }
    }

    override fun numberOfEntriesIn(type: RepositoryType): Long {
        return getRepository<Repository<*>>(type).count()
    }
}