package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.Repository
import kotlinx.coroutines.runBlocking

class SqlDelightDatabase : DatabaseInterface {
    private val database = CigarsDatabase(SqlDelightDatabaseDriverFactory().createDriver())
    override fun <R : Repository<*>> getRepository(type: RepositoryType, args: Any?): R {
        return when (type) {
            RepositoryType.Cigars -> SqlDelightCigarsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.Humidors -> SqlDelightHumidorsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.Favorites -> SqlDelightFavoriteCigarsRepository(database.cigarsDatabaseQueries) as R
            RepositoryType.CigarImages -> SqlDelightCigarImagesRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R

            RepositoryType.HumidorImages -> SqlDelightHumidorImagesRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R

            RepositoryType.CigarHumidors -> SqlDelightCigarHumidorsRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R

            RepositoryType.HumidorCigars -> SqlDelightHumidorCigarsRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R

            RepositoryType.CigarHistory -> SqlDelightCigarHistoryRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R

            RepositoryType.HumidorHistory -> SqlDelightHumidorHistoryRepository(
                args as Long,
                database.cigarsDatabaseQueries
            ) as R
        }
    }


    override fun reset() {
        runBlocking {
            database.cigarsDatabaseQueries.removeAllCigars()
            database.cigarsDatabaseQueries.removeAllHumidors()
        }
    }

    override fun numberOfEntriesIn(type: RepositoryType): Long {
        return getRepository<Repository<*>>(type).count()
    }
}