package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.repository.DatabaseInterface
import com.akellolcc.cigars.databases.repository.DatabaseType
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.utils.Pref

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

    override fun createDemoSet() {
        database.createDemoSet()
    }

    override fun reset() {
        database.reset()
    }
}