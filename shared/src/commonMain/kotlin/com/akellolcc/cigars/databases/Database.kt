/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/25/24, 10:00 PM
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

package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightDatabase
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import com.akellolcc.cigars.utils.collectFirst
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

enum class DatabaseType {
    SqlDelight;

    companion object {
        fun getDatabase(type: DatabaseType): DatabaseInterface {
            return when (type) {
                SqlDelight -> SqlDelightDatabase.instance
            }
        }
    }
}

class Database : DatabaseInterface {
    private val database = DatabaseType.getDatabase(DatabaseType.SqlDelight)

    companion object {
        private var _instance: Database? = null
        val instance: Database
            get() {
                return createInstance()
            }

        private fun createInstance(): Database {
            if (_instance == null)
                _instance = Database()
            return _instance!!
        }

    }

    inline fun <reified R : Repository<*>> getRepository(
        repoKClass: KClass<out R>,
        args: Any? = null
    ): R {
        return createRepository(repoKClass, args)
    }

    override fun reset() {
        database.reset()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createDemoSet(): Flow<Boolean> {
        val demoHumidors =
            Json.decodeFromString<List<Humidor>>(readTextFile(AssetFiles.demo_humidors) ?: "")
        val demoCigars =
            Json.decodeFromString<List<Cigar>>(readTextFile(AssetFiles.demo_cigars) ?: "")
        val demoCigarsImages = Json.decodeFromString<List<CigarImage>>(
            readTextFile(AssetFiles.demo_cigars_images) ?: ""
        )
        val humidorDatabase: HumidorsRepository = getRepository(HumidorsRepository::class)
        val cigarsDatabase: CigarsRepository = getRepository(CigarsRepository::class)
        var imagesDatabase: ImagesRepository
        Log.debug("Create demo database")
        var count = 0
        return flow {
            demoHumidors.asFlow().flatMapConcat {
                humidorDatabase.add(it)
            }.flatMapConcat { h ->
                flow {
                    val images = demoCigarsImages.filter { it.humidorId == h.other }.map {
                        val image = it.copy().apply {
                            rowid = -1
                            humidorId = h.rowid
                            bytes = imageData(it.notes!!)!!
                            notes = null
                        }
                        image
                    }
                    imagesDatabase = getRepository(HumidorImagesRepository::class, h.rowid)
                    imagesDatabase.addAll(images).collect {
                        emit(h)
                    }
                }
            }.flatMapConcat { humidor ->
                val cigars = demoCigars.filter { it.other == humidor.other }.map {
                    it.rowid = -1
                    it
                }
                cigarsDatabase.addAll(cigars, humidor)
            }.flatMapConcat {
                it.asFlow()
            }.flatMapConcat { cigar ->
                val images = demoCigarsImages.filter { it.cigarId == cigar.myrating }.map {
                    val image = it.copy().apply {
                        rowid = -1
                        cigarId = cigar.rowid
                        bytes = imageData(it.notes!!)!!
                        notes = null
                    }
                    image
                }
                imagesDatabase = getRepository(CigarImagesRepository::class, cigar.rowid)
                imagesDatabase.addAll(images)
            }.collectFirst(demoCigars.size) {
                emit(true)
            }
        }
    }
}