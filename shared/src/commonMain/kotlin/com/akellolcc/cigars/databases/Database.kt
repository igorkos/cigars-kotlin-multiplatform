package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightDatabase
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.imageData
import com.akellolcc.cigars.theme.readTextFile
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.observable.wrap
import kotlinx.serialization.json.Json

enum class DatabaseType {
    SqlDelight;

    companion object {
        fun getDatabase(type: DatabaseType): DatabaseInterface {
            return when (type) {
                SqlDelight -> SqlDelightDatabase()
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

    override fun <R> getRepository(type: RepositoryType, args: Any?): R {
        return database.getRepository(type, args)
    }

    override fun reset() {
        database.reset()
    }

    override fun numberOfEntriesIn(type: RepositoryType): Long {
        return database.numberOfEntriesIn(type)
    }

    fun createDemoSet(): ObservableWrapper<Any> {
        val demoHumidors =
            Json.decodeFromString<List<Humidor>>(readTextFile(AssetFiles.demo_humidors) ?: "")
        var humidor: Humidor = demoHumidors[0]
        val demoCigars =
            Json.decodeFromString<List<Cigar>>(readTextFile(AssetFiles.demo_cigars) ?: "")
        val demoCigarsImages = Json.decodeFromString<List<CigarImage>>(
            readTextFile(AssetFiles.demo_cigars_images) ?: ""
        )
        val humidorDatabase: HumidorsRepository = getRepository(RepositoryType.Humidors)
        val cigarsDatabase: CigarsRepository = getRepository(RepositoryType.Cigars)
        val imagesDatabase: ImagesRepository =
            getRepository(RepositoryType.CigarImages, humidor.rowid)
        Log.debug("Added demo database")
        return humidorDatabase.add(demoHumidors[1]).flatMap {
                humidorDatabase.add(demoHumidors[0])
            }.flatMapIterable { h ->
                Log.debug("Added demo Humidor ${h.rowid}")
                humidor = h
                demoCigarsImages.filter { it.humidorId == 0L }
            }.flatMap { image ->
                Log.debug("Added image to Humidor ${image.rowid}")
                image.humidorId = humidor.rowid
                image.bytes = imageData(image.notes!!)!!
                imagesDatabase.add(image)
            }.take(1).flatMapIterable {
                Log.debug("Add cigars")
                demoCigars
            }.flatMap { cigar ->
                Log.debug("Add demo Cigar ${cigar.rowid}")
                cigarsDatabase.add(cigar, humidor)
            }.flatMap { cigar ->
                val image = demoCigarsImages.first {
                    it.rowid == cigar.myrating
                }
                Log.debug("Add image $image to Cigar ${cigar.rowid} : ${cigar.myrating}")
                image.rowid = -1
                image.cigarId = cigar.rowid
                try {
                    image.bytes = imageData(image.notes!!)!!
                } catch (e: Exception) {
                    Log.error("Get image data failed $e")
                }
                imagesDatabase.add(image)
            }.wrap()
    }
}