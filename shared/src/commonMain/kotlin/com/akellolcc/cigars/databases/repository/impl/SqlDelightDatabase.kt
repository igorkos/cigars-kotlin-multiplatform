/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/25/24, 5:38 PM
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
import com.akellolcc.cigars.databases.RepositoryRegistry
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorCigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import kotlinx.coroutines.runBlocking

class SqlDelightDatabase : DatabaseInterface {
    internal val database = CigarsDatabase(SqlDelightDatabaseDriverFactory().createDriver())

    companion object {
        private var _instance: SqlDelightDatabase? = null
        val instance: SqlDelightDatabase
            get() {
                return createInstance()
            }

        private fun createInstance(): SqlDelightDatabase {
            if (_instance == null) {
                _instance = SqlDelightDatabase()
                _instance!!.register()
            }
            return _instance!!
        }

    }

    private fun register() {
        RepositoryRegistry.register(
            CigarsRepository::class,
            SqlDelightCigarsRepository.Factory
        )
        RepositoryRegistry.register(
            HumidorsRepository::class,
            SqlDelightHumidorsRepository.Factory
        )
        RepositoryRegistry.register(
            FavoriteCigarsRepository::class,
            SqlDelightFavoriteCigarsRepository.Factory
        )
        RepositoryRegistry.register(
            CigarImagesRepository::class,
            SqlDelightCigarImagesRepository.Factory
        )
        RepositoryRegistry.register(
            HumidorImagesRepository::class,
            SqlDelightHumidorImagesRepository.Factory
        )
        RepositoryRegistry.register(
            CigarHumidorsRepository::class,
            SqlDelightCigarHumidorsRepository.Factory
        )
        RepositoryRegistry.register(
            HumidorCigarsRepository::class,
            SqlDelightHumidorCigarsRepository.Factory
        )
        RepositoryRegistry.register(
            CigarHistoryRepository::class,
            SqlDelightCigarHistoryRepository.Factory
        )
        RepositoryRegistry.register(
            HumidorHistoryRepository::class,
            SqlDelightHumidorHistoryRepository.Factory
        )
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

}