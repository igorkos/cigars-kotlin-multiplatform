package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.demo.DemoCigar
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarHumidorRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarsRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.readTextFile
import com.akellolcc.cigars.utils.Pref
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json


class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = CigarsDatabase(databaseDriverFactory.createDriver())
    val dbQueries = database.cigarsDatabaseQueries

    companion object {
        private var instance : Database? = null

        fun  createInstance(databaseDriverFactory: DatabaseDriverFactory): Database {
            if (instance == null)
                instance = Database(databaseDriverFactory)
           if (Pref.isFirstStart) {
                Pref.isFirstStart = false
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
            val humidorDatabase = SqlDelightHumidorsRepository(dbQueries)
            val humidor = Humidor(-1, name = Localize.demo_humidor_name, brand = "Brand", holds = 50, humidity = 72.0, temperature = 71)
            humidorDatabase.add(humidor)
            Log.debug("Demo humidor: $humidor")

            val demoCigarsJson = readTextFile(AssetFiles.demo_cigars)

            demoCigarsJson?.let { json ->
                val cigarsDatabase = SqlDelightCigarsRepository(
                    getInstance().dbQueries)
                val cigars = Json.decodeFromString<List<DemoCigar>>(json)
                for( cigar in cigars) {
                    val dbCigar = Cigar(-1,cigar.name, cigar.brand,
                        cigar.country,
                        10,
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
                    cigarsDatabase.add(dbCigar)
                    val hcDatabase = SqlDelightCigarHumidorRepository(dbCigar.rowid, dbQueries)
                    hcDatabase.add(humidor, 10)
                }
            }
        }
    }

    fun reset() {
        runBlocking {
            dbQueries.removeAllCigars()
            dbQueries.removeAllHumidors()
        }
    }

}