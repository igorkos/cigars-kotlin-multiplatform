package com.akellolcc.cigars.databases

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = CigarsDatabase(databaseDriverFactory.createDriver())
    private val dbQueries = database.cigarsDatabaseQueries

    companion object {
        private var instance : Database? = null

        fun  createInstance(databaseDriverFactory: DatabaseDriverFactory): Database {
            if (instance == null)
                instance = Database(databaseDriverFactory)
            return instance!!
        }

        fun getInstance(): Database {
            if (instance == null) throw IllegalStateException()
            return instance!!
        }
    }


}