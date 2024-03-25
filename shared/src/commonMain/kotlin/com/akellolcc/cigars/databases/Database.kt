package com.akellolcc.cigars.databases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import com.akellolcc.cigars.databases.demo.DemoCigar
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.readTextFile
import com.akellolcc.cigars.utils.Pref
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*


class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = CigarsDatabase(databaseDriverFactory.createDriver())
    val dbQueries = database.cigarsDatabaseQueries

    companion object {
        private var instance : Database? = null

        fun  createInstance(databaseDriverFactory: DatabaseDriverFactory): Database {
            if (instance == null)
                instance = Database(databaseDriverFactory)
           if (Pref.isFirstStart) {
                //instance?.reset()
                instance?.createDemoSet()
            }
            return instance!!
        }

        fun getInstance(): Database {
            if (instance == null) throw IllegalStateException()
            return instance!!
        }
    }

    fun createDemoSet() {
        runBlocking {
            val humidor = Humidor(Localize.demo_humidor_name, "Brand", 50, 72, 71.0)
            Log.debug("Demo humidor: $humidor")

            val demoCigarsJson = readTextFile(AssetFiles.demo_cigars)

            demoCigarsJson?.let { json ->
                val cigars = Json.decodeFromString<List<DemoCigar>>(json)
                for( cigar in cigars) {
                    val dbCigar = Cigar(cigar.name, cigar.brand,
                        cigar.country,
                        10,
                        15.0,
                        cigar.cigar,
                        cigar.wrapper,
                        cigar.binder,
                        cigar.gauge,
                        cigar.length,
                        cigar.strength,
                        cigar.rating,
                        cigar.myrating,
                        cigar.notes,
                        cigar.filler,
                        cigar.link,
                        cigar.shopping,
                        cigar.favorites)
                    humidor.addCigar(dbCigar, 10)
                }
            }
        }
    }
    fun humidors() : List<Humidor> {
        return runBlocking {
            return@runBlocking dbQueries.allHumidors().executeAsList().map {
                Humidor(it)
            }
        }
    }

    fun cigars() : List<Cigar> {
        return dbQueries.allCigars().executeAsList().map {
                Cigar(it)
            }
    }

    fun reset() {
        runBlocking {
            dbQueries.removeAllCigars()
            dbQueries.removeAllHumidors()
        }
    }

}