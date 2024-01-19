package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.Database
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock

open class DBObject<T>() {
    protected var dbObject: T? = null;
    protected val dbQuery = Database.getInstance().dbQueries

    protected fun runDbQuery( block: suspend (DBObject<T>) -> DBObject<T>?) : DBObject<T>? {
        return runBlocking {
            return@runBlocking block(this@DBObject)
        }
    }

    protected open fun addHistory(count: Long?, left: Long?, price: Double?,
                   type: Long? = 1,date: Long? = Clock.System.now().toEpochMilliseconds()
    ): History {
        return runBlocking {
            this@DBObject.dbQuery.addHistory(count, date, left, price, type)
            val historyID = this@DBObject.dbQuery.lastInsertRowId().executeAsOne()
            return@runBlocking History(historyID)
        }
    }

    override fun toString(): String {
        return dbObject?.toString() ?: super.toString()
    }
}