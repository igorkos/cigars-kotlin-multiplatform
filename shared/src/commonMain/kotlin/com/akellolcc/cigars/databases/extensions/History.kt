package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.HistoryTable
import kotlinx.datetime.Clock

class History: DBObject<HistoryTable> {
    public val id: Long
        get() { return this.dbObject?.rowid ?: -1 }
    public val count: Long
        get() { return this.dbObject?.count ?: 0 }
    public val date: Long
        get() { return this.dbObject?.date ?: -1 }
    public val left: Long
        get() { return this.dbObject?.left ?: 0 }
    public val price: Double
        get() { return this.dbObject?.price ?: 0.0 }
    public val type: Long
        get() { return this.dbObject?.type ?: 0 }
    public val cigarId: Long
        get() { return this.dbObject?.cigarId ?: -1 }
    public val humidorId: Long
        get() { return this.dbObject?.humidorId ?: -1 }

    constructor(dbID: Long) : super() {
        this.dbObject = this.dbQuery.history(dbID).executeAsOne()
    }

    constructor(history: HistoryTable) : super() {
        this.dbObject = history
    }


    constructor( count: Long,
                 price: Double,
                 type: Long,
                 left: Long? = null,
                 date: Long = Clock.System.now().toEpochMilliseconds()
                 ) : super() {
        runDbQuery {
            val historyID = this.dbQuery.transactionWithResult {
                dbQuery.addHistory(count, date, left, price, type)
                dbQuery.lastInsertRowId().executeAsOne()
            }
            this.dbObject = this.dbQuery.history(historyID).executeAsOne()
            return@runDbQuery this
        }
    }
}