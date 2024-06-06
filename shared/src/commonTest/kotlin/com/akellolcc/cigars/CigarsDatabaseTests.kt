/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/27/24, 1:48 PM
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

package com.akellolcc.cigars

import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorCigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.logging.Log
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoggingTests : Antilog() {
    override fun performLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?) {
        println("$priority $tag $message")
    }

}

class CigarsDatabaseTests {
    init {
        Log.initLog(LoggingTests()) { }
    }

    @Test
    fun testRepository() {
        val database = Database(true)
        assertNotNull(createRepository(CigarsRepository::class))
        assertNotNull(createRepository(HumidorsRepository::class))
        assertNotNull(createRepository(FavoriteCigarsRepository::class))
        assertNotNull(createRepository(CigarImagesRepository::class, -1L))
        assertNotNull(createRepository(HumidorImagesRepository::class, -1L))
        assertNotNull(createRepository(CigarHumidorsRepository::class, -1L))
        assertNotNull(createRepository(HumidorCigarsRepository::class, -1L))
        assertNotNull(createRepository(CigarHistoryRepository::class, -1L))
        assertNotNull(createRepository(HumidorHistoryRepository::class, -1L))
    }

    @Test
    fun testDemoSetup() {
        runBlocking {
            val database = Database(true)
            database.reset()
            database.createDemoSet().collect {
                //Check that demo set is created
                //Should be 2 cigars and 2 humidors and no favorite cigars
                val humidorsRepository = createRepository(HumidorsRepository::class)
                assertEquals(2, humidorsRepository.count())
                val cigarsRepository = createRepository(CigarsRepository::class)
                assertEquals(2, cigarsRepository.count())
                val favoriteCigarsRepository = createRepository(FavoriteCigarsRepository::class)
                assertEquals(0, favoriteCigarsRepository.count())

                //Check cigars each cigar has 1 humidor and 1 history and no images
                val cigars = cigarsRepository.allSync()
                assertNotNull(cigars)
                cigars.forEach {
                    val cigarImagesRepository = createRepository(CigarImagesRepository::class, it.rowid)
                    assertEquals(0, cigarImagesRepository.count())

                    val cigarHumidorsRepository = createRepository(CigarHumidorsRepository::class, it.rowid)
                    assertEquals(1, cigarHumidorsRepository.count())

                    val cigarHistoryRepository = createRepository(CigarHistoryRepository::class, it.rowid)
                    assertEquals(1, cigarHistoryRepository.count())
                }
                //Check humidors:
                // -> first humidor has 2 cigar and 3 history and no images cigars count is 20
                // -> second humidor has 0 cigar and 1 history and no images cigars count is 0
                val humidors = humidorsRepository.allSync()
                assertNotNull(humidors)
                for (humidor in humidors) {
                    val humidorImagesRepository = createRepository(HumidorImagesRepository::class, humidor.rowid)
                    assertEquals(0, humidorImagesRepository.count())

                    val humidorCigarsRepository = createRepository(HumidorCigarsRepository::class, humidor.rowid)
                    if (humidor.name == "Second") {
                        assertEquals(0, humidorCigarsRepository.count())
                        assertEquals(0, humidor.count)
                    } else {
                        assertEquals(2, humidorCigarsRepository.count())
                        assertEquals(20, humidor.count)
                    }

                    val humidorHistoryRepository = createRepository(HumidorHistoryRepository::class, humidor.rowid)
                    if (humidor.name == "Second") {
                        assertEquals(1, humidorHistoryRepository.count())
                    } else {
                        assertEquals(3, humidorHistoryRepository.count())
                    }
                }
            }
        }
    }

    @Test
    fun moveCigarTest() {
        runBlocking {
            val database = Database(true)
            database.reset()
            database.createDemoSet().collect {
                database.logDatabase()
                val humidorsRepository = createRepository(HumidorsRepository::class)
                val cigarsRepository = createRepository(CigarsRepository::class)
                //Get first cigar
                val cigar = cigarsRepository.allSync().first()
                assertNotNull(cigar, "Get first cigar")
                //Get first humidor where cigar lives
                val cigarHumidorsRepository = createRepository(CigarHumidorsRepository::class, cigar.rowid)
                val entry = cigarHumidorsRepository.allSync().first()
                assertNotNull(entry, "Get first humidor where cigar lives")
                //Get second humidor
                var humidors = humidorsRepository.allSync()
                val humidorTo = humidors.first { it.name == "Second" }
                assertNotNull(humidorTo, "Get second humidor")

                //Move one cigar from first humidor to second
                cigarHumidorsRepository.moveCigar(entry, humidorTo, 1).single()

                database.logDatabase()
                //Check that cigar is moved
                //Get first humidor where cigar lives after move there should be 2 cigars
                var humidorCigarsRepository = createRepository(HumidorCigarsRepository::class, entry.humidor.rowid)
                assertEquals(2, humidorCigarsRepository.count(), "Check that cigar is moved from")
                //Get second humidor where cigar lives after move there should be 1 cigar
                humidorCigarsRepository = createRepository(HumidorCigarsRepository::class, humidorTo.rowid)
                assertEquals(1, humidorCigarsRepository.count(), "Check that cigar is moved to")

                //Check cigars count after move
                humidors = humidorsRepository.allSync()
                for (humidor in humidors) {
                    val humidorHistoryRepository = createRepository(HumidorHistoryRepository::class, humidor.rowid)
                    if (humidor.name == "Second") {
                        assertEquals(1, humidor.count, "Check cigars count after move in humidor to")
                        assertEquals(2, humidorHistoryRepository.count(), "Check cigars count after move in humidor to")
                    } else {
                        assertEquals(19, humidor.count, "Check cigars count after move in")
                        assertEquals(4, humidorHistoryRepository.count(), "Check cigars count after move in")
                    }
                }

                cigarHumidorsRepository.allSync().forEach {
                    if (it.humidor.name == "Second") {
                        assertEquals(1, it.count, "Check cigars count after move in humidor to")
                    } else {
                        if (it.cigar.rowid == cigar.rowid) {
                            assertEquals(9, it.count, "Check cigars count after move in")
                        } else {
                            assertEquals(10, it.count, "Check cigars count after move in")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun setCigarFavoriteTest() {
        runBlocking {
            val database = Database(true)
            database.reset()
            database.createDemoSet().collect {
                val cigarsRepository = createRepository(CigarsRepository::class)
                //Get first cigar
                var cigar = cigarsRepository.allSync().first()
                assertNotNull(cigar, "Get first cigar")

                cigarsRepository.updateFavorite(true, cigar).single()

                cigar = cigarsRepository.getSync(cigar.rowid)
                assertNotNull(cigar, "Get first cigar")
                assertEquals(true, cigar.favorites, "Check cigar favorite")


                val favoriteCigarsRepository = createRepository(FavoriteCigarsRepository::class)
                assertEquals(1, favoriteCigarsRepository.count(), "Check favorite cigars count")
            }
        }
    }

    @Test
    fun setCigarRatingTest() {
        runBlocking {
            val database = Database(true)
            database.reset()
            database.createDemoSet().collect {
                val cigarsRepository = createRepository(CigarsRepository::class)
                //Get first cigar
                var cigar = cigarsRepository.allSync().first()
                assertNotNull(cigar, "Get first cigar")

                cigarsRepository.updateRating(56, cigar).single()

                cigar = cigarsRepository.getSync(cigar.rowid)
                assertNotNull(cigar, "Get updated cigar")
                assertEquals(56, cigar.myrating, "Check cigar rating")
            }
        }
    }
}