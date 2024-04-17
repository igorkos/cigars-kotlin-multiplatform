package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.Repository
import kotlinx.coroutines.runBlocking

class SqlDelightDatabase : DatabaseInterface {
    private val database = CigarsDatabase(SqlDelightDatabaseDriverFactory().createDriver())

    @Suppress("UNCHECKED_CAST")
    override fun <R> getRepository(type: RepositoryType, args: Any?): R {
        return when (type) {
            RepositoryType.Cigars -> (SqlDelightCigarsRepository(database.cigarsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.Humidors -> (SqlDelightHumidorsRepository(database.humidorsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.Favorites -> (SqlDelightFavoriteCigarsRepository(database.cigarsDatabaseQueries) as? R)
                ?: error("Invalid repository type")

            RepositoryType.CigarImages -> (SqlDelightCigarImagesRepository(
                args as Long,
                database.imagesDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorImages -> (SqlDelightHumidorImagesRepository(
                args as Long,
                database.imagesDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.CigarHumidors -> (SqlDelightCigarHumidorsRepository(
                args as Long,
                database.humidorCigarsDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorCigars -> (SqlDelightHumidorCigarsRepository(
                args as Long,
                database.humidorCigarsDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.CigarHistory -> (SqlDelightCigarHistoryRepository(
                args as Long,
                database.historyDatabaseQueries
            ) as? R) ?: error("Invalid repository type")

            RepositoryType.HumidorHistory -> (SqlDelightHumidorHistoryRepository(
                args as Long,
                database.historyDatabaseQueries
            ) as? R) ?: error("Invalid repository type")
        }
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

    override fun numberOfEntriesIn(type: RepositoryType): Long {
        return getRepository<Repository<*>>(type).count()
    }
}