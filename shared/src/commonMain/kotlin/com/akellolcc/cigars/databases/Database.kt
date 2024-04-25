/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/24/24, 1:33 PM
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
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightDatabase
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    fun createDemoSet(): Flow<Any> {
        val demoHumidors =
            Json.decodeFromString<List<Humidor>>(readTextFile(AssetFiles.demo_humidors) ?: "")
        var humidor: Humidor = demoHumidors[0]
        val demoCigars =
            Json.decodeFromString<List<Cigar>>(readTextFile(AssetFiles.demo_cigars) ?: "")
        val demoCigarsImages = Json.decodeFromString<List<CigarImage>>(
            readTextFile(AssetFiles.demo_cigars_images) ?: ""
        )
        val humidorDatabase: HumidorsRepository = getRepository(HumidorsRepository::class)
        val cigarsDatabase: CigarsRepository = getRepository(CigarsRepository::class)
        val imagesDatabase: ImagesRepository =
            getRepository(ImagesRepository::class, humidor.rowid)
        Log.debug("Added demo database")
        return demoHumidors.asFlow().flatMapConcat {
            humidorDatabase.add(it)
        }.flatMapLatest { h ->
            Log.debug("Added demo Humidor ${h.rowid}")
            humidor = h
            demoCigarsImages.filter { it.humidorId == 0L }.asFlow()
        }.flatMapConcat { image ->
            Log.debug("Added image to Humidor ${image.rowid}")
            image.humidorId = humidor.rowid
            image.bytes = imageData(image.notes!!)!!
            imagesDatabase.add(image)
        }.flatMapLatest {
            Log.debug("Add cigars")
            demoCigars.asFlow()
        }.flatMapConcat { cigar ->
            Log.debug("Add demo Cigar ${cigar.rowid}")
            cigarsDatabase.add(cigar, humidor)
        }.flatMapConcat { cigar ->
            demoCigarsImages.filter {
                it.rowid == cigar.myrating
            }.map {
                it.rowid = -1
                it.cigarId = cigar.rowid
                it
            }.asFlow()
        }.flatMapConcat { image ->
            Log.debug("Add image $image to Cigar ${image.cigarId}")
            try {
                image.bytes = imageData(image.notes!!)!!
            } catch (e: Exception) {
                Log.error("Get image data failed $e")
            }
            imagesDatabase.add(image)
        }.flatMapLatest {
            flowOf(true)
        }
    }
}