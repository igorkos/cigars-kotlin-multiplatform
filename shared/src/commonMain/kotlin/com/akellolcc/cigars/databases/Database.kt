/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/21/24, 11:34 AM
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

package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.rapid.RapidDatabase
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.sqldelight.SqlDelightDatabase
import com.akellolcc.cigars.databases.test.DemoTestSets
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import com.akellolcc.cigars.utils.collectFirst
import dev.icerock.moko.resources.FileResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

enum class DatabaseType {
    SqlDelight,
    Rapid;

    companion object {
        fun getDatabase(type: DatabaseType, inMemory: Boolean): DatabaseInterface {
            return when (type) {
                SqlDelight -> SqlDelightDatabase.createInstance(inMemory)
                Rapid -> RapidDatabase.createInstance(inMemory)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> loadDemoSet(resource: FileResource, inMemory: Boolean): List<T> {
    if (inMemory) {
        return when (resource) {
            AssetFiles.demo_humidors -> Json.decodeFromString<List<Humidor>>(DemoTestSets.humidorsSet) as List<T>
            AssetFiles.demo_cigars -> Json.decodeFromString<List<Cigar>>(DemoTestSets.cigarsSet) as List<T>
            else -> emptyList()
        }
    } else {
        val jsonString = readTextFile(resource) ?: ""
        return when (resource) {
            AssetFiles.demo_humidors -> Json.decodeFromString<List<Humidor>>(jsonString) as List<T>
            AssetFiles.demo_cigars -> Json.decodeFromString<List<Cigar>>(jsonString) as List<T>
            AssetFiles.demo_cigars_images -> Json.decodeFromString<List<CigarImage>>(
                jsonString
            ) as List<T>

            else -> emptyList()
        }
    }
}

class Database(override val inMemory: Boolean) : DatabaseInterface {

    private val sqlDelightDatabase: DatabaseInterface = DatabaseType.getDatabase(DatabaseType.SqlDelight, inMemory)
    private val rapidDatabase: DatabaseInterface = DatabaseType.getDatabase(DatabaseType.Rapid, inMemory)

    companion object {
        private var _instance: Database? = null
        val instance: Database
            get() {
                if (_instance == null)
                    throw IllegalStateException("Database not initialized")
                return _instance!!
            }

        fun createInstance(inMemory: Boolean): Database {
            if (_instance == null)
                _instance = Database(inMemory)
            return _instance!!
        }

    }

    override fun reset() {
        sqlDelightDatabase.reset()
        rapidDatabase.reset()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createDemoSet(): Flow<Boolean> {
        val demoHumidors: List<Humidor> = loadDemoSet(AssetFiles.demo_humidors, inMemory)
        val demoCigars: List<Cigar> = loadDemoSet(AssetFiles.demo_cigars, inMemory)
        val demoCigarsImages: List<CigarImage> = loadDemoSet(AssetFiles.demo_cigars_images, inMemory)
        val humidorDatabase: HumidorsRepository = createRepository(HumidorsRepository::class)
        val cigarsDatabase: CigarsRepository = createRepository(CigarsRepository::class)
        var imagesDatabase: ImagesRepository
        Log.debug("Create demo database")

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
                    imagesDatabase = createRepository(HumidorImagesRepository::class, h.rowid)
                    imagesDatabase.addAll(images).collect {
                        emit(h)
                    }
                }
            }.flatMapConcat { humidor ->
                val cigars = demoCigars.filter { it.other == humidor.other }.map {
                    it.rowid = -1
                    it
                }
                /*var multiply = listOf<Cigar>()
                for (i in 0..10) {
                    multiply = multiply + cigars
                }
                multiply = multiply.map {
                    it.copy(rowid = -1)
                }*/
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
                imagesDatabase = createRepository(CigarImagesRepository::class, cigar.rowid)
                imagesDatabase.addAll(images)
            }.collectFirst(demoCigars.size) {
                emit(true)
            }
        }
    }

    fun logDatabase() {
        Log.debug("===========Humidors===========")
        createRepository(HumidorsRepository::class).allSync().map {
            Log.debug(it.toString())
        }
        Log.debug("===========Cigars===========")
        createRepository(CigarsRepository::class).allSync().map {
            Log.debug(it.toString())
        }
        Log.debug("===========Humidor Cigar===========")
        createRepository(CigarHumidorsRepository::class, -1L).allSync().map {
            Log.debug(it.toString())
        }
        Log.debug("===========History===========")
        createRepository(CigarHistoryRepository::class, -1L).allSync().map {
            Log.debug(it.toString())
        }
    }
}