package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.repository.impl.SqlDelightDatabase


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

interface DatabaseDriverFactory<T> {
    fun createDriver(): T
}
