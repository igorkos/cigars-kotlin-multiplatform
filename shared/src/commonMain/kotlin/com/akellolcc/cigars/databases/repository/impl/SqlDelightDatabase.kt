package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.RepositoryType
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
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.math.absoluteValue

class SqlDelightDatabase : DatabaseInterface {
    private val database = CigarsDatabase(SqlDelightDatabaseDriverFactory().createDriver())
    override fun <R: Repository<*>> getRepository(type: RepositoryType, args: Any?): R {
        return when (type) {
            RepositoryType.Cigars -> SqlDelightCigarsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.Humidors -> SqlDelightHumidorsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.Favorites -> SqlDelightFavoriteCigarsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.CigarImages -> SqlDelightCigarImagesRepository(args as Long, database.cigarsDatabaseQueries) as R
            RepositoryType.HumidorImages -> SqlDelightHumidorImagesRepository(args as Long, database.cigarsDatabaseQueries) as R
            RepositoryType.CigarHumidors -> SqlDelightCigarHumidorsRepository(args as Long, database.cigarsDatabaseQueries) as R
            RepositoryType.HumidorCigars -> SqlDelightHumidorCigarsRepository(args as Long, database.cigarsDatabaseQueries) as R
            RepositoryType.CigarHistory -> SqlDelightCigarHistoryRepository(args as Long, database.cigarsDatabaseQueries) as R
            RepositoryType.HumidorHistory -> SqlDelightHumidorHistoryRepository(args as Long, database.cigarsDatabaseQueries) as R
        }
    }

    override fun createDemoSet() {
        runBlocking {
            var humidor: Humidor
            readTextFile(AssetFiles.demo_humidors)?.let { hjson ->
                val humidorDatabase: HumidorsRepository = getRepository(RepositoryType.Humidors)
                humidor = Json.decodeFromString<List<Humidor>>(hjson).first()
                humidorDatabase.add(humidor)
                readTextFile(AssetFiles.demo_cigars)?.let { cjson ->
                    val cigarsDatabase: CigarsRepository = getRepository(RepositoryType.Cigars)
                    val cigars = Json.decodeFromString<List<Cigar>>(cjson)
                    for (cigar in cigars) {
                        cigarsDatabase.add(cigar)
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
                        )
                        )
                    }
                    readTextFile(AssetFiles.demo_cigars_images)?.let { json ->
                        val images = Json.decodeFromString<List<CigarImage>>(json)
                        for (image in images) {
                            imageData(image.notes!!)?.let {
                                database.cigarsDatabaseQueries.addImage(
                                    image.rowid,
                                    it,
                                    image.type,
                                    image.image,
                                    "",
                                    image.cigarId,
                                    image.humidorId
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun reset() {
        runBlocking {
            database.cigarsDatabaseQueries.removeAllCigars()
            database.cigarsDatabaseQueries.removeAllHumidors()
        }
    }
}