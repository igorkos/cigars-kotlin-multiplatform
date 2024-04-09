package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightDatabase
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import com.akellolcc.cigars.utils.Pref
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

enum class DatabaseType {
    SqlDelight;
    companion object {
        fun getDatabase(type: DatabaseType): DatabaseInterface {
            return when (type) {
                DatabaseType.SqlDelight -> SqlDelightDatabase()
            }
        }
    }
}

enum class RepositoryType {
    Cigars,
    Humidors,
    Favorites,
    CigarImages,
    HumidorImages,
    CigarHumidors,
    HumidorCigars,
    CigarHistory,
    HumidorHistory
}
class Database() : DatabaseInterface {
    private val database = DatabaseType.getDatabase(DatabaseType.SqlDelight)

    companion object {
        private var instance: Database? = null

        private fun createInstance(): Database {
            if (instance == null)
                instance = Database()
            if (Pref.isFirstStart) {
                Pref.isFirstStart = false
                instance?.createDemoSet()
            }
            return instance!!
        }

        fun getInstance(): Database {
            if (instance == null) createInstance()
            return instance!!
        }
    }

    override fun <R : Repository<*>> getRepository(type: RepositoryType, args: Any?): R {
        return database.getRepository(type, args)
    }

    override fun reset() {
        database.reset()
    }

    private fun createDemoSet() {
        runBlocking {
            var humidor: Humidor
            readTextFile(AssetFiles.demo_humidors)?.let { hjson ->
                val humidorDatabase: HumidorsRepository = getRepository(RepositoryType.Humidors)
                humidor = Json.decodeFromString<List<Humidor>>(hjson).first()
                humidorDatabase.add(humidor, null)
                readTextFile(AssetFiles.demo_cigars)?.let { cjson ->
                    val cigarsDatabase: CigarsRepository = getRepository(RepositoryType.Cigars)
                    val cigars = Json.decodeFromString<List<Cigar>>(cjson)
                    for (cigar in cigars) {
                        cigarsDatabase.add(cigar, null)
                        val hcDatabase: CigarHumidorRepository = getRepository(RepositoryType.CigarHumidors, cigar.rowid)
                        hcDatabase.add(humidor, 10)
                        val hisDatabase: HistoryRepository = getRepository(RepositoryType.CigarHistory, cigar.rowid)
                        hisDatabase.add(
                            History(
                                -1,
                                cigar.count,
                                Clock.System.now().toEpochMilliseconds(),
                                cigar.count,
                                cigar.price,
                                HistoryType.Addition,
                                cigar.rowid,
                                humidor.rowid
                            ),
                            null
                        )
                    }
                    readTextFile(AssetFiles.demo_cigars_images)?.let { json ->
                        val imagesDatabase: ImagesRepository = getRepository(RepositoryType.CigarImages, humidor.rowid)
                        val images = Json.decodeFromString<List<CigarImage>>(json)
                        for (image in images) {
                            imageData(image.notes!!)?.let {
                                image.bytes = it
                                imagesDatabase.addOrUpdate(image)
                            }
                        }
                    }
                }
            }
        }
    }
}