package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.HumidorsTable
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock


class Humidor : DBObject<HumidorsTable>{
    public val id: Long
        get() { return this.dbObject?.rowid ?: -1 }
    public val name: String
        get() { return this.dbObject?.name ?: "" }
    public val brand: String?
        get() { return this.dbObject?.brand }
    public val holds: Long
        get() { return this.dbObject?.holds ?: 0 }
    public val count: Long
        get() { return this.dbObject?.count ?: 0 }
    public val humidity: Double?
        get() { return this.dbObject?.humidity}
    public val temperature: Long?
        get() { return this.dbObject?.temperature}
    public val notes: String?
        get() { return this.dbObject?.notes }
    public val link: String?
        get() { return this.dbObject?.link }
    public val autoOpen: Boolean
        get() { return this.dbObject?.autoOpen ?: false }
    public val sorting: Long
        get() { return this.dbObject?.sorting ?: 0 }
    public val type: Long
        get() { return this.dbObject?.type ?: 0 }

    constructor(dbID: Long) : super() {
        this.dbObject = this.dbQuery.humidor(dbID).executeAsOne()
    }

    constructor(humidor: HumidorsTable) : super() {
        this.dbObject = humidor
    }

    constructor(name: String,
        brand: String?,
        holds: Long,
        temperature: Long = 72,
        humidity: Double = 71.0,
        notes: String? = null,
        link: String? = null,
        autoOpen: Boolean = false,
        sorting: Long = 0,
        type: Long = 0) : super() {
        runDbQuery {
            //Add humidor
            Log.debug("Added Demo Humidor")
            val humidorID = this.dbQuery.transactionWithResult {
                dbQuery.addHumidor(
                    name,
                    brand,
                    holds,
                    0,
                    temperature,
                    humidity,
                    notes,
                    link,
                    autoOpen,
                    sorting,
                    type
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
            Log.debug("Added Humidor id $humidorID")
            //Add History item to humidor
            val historyID = this.dbQuery.transactionWithResult {
                dbQuery.addHistory(0, Clock.System.now().toEpochMilliseconds(), 1, 150.0, 0)
                dbQuery.lastInsertRowId().executeAsOne()
            }
            Log.debug("Added history to id $historyID")
            this.dbQuery.addHistoryToHumidor(humidorID, historyID)
            Log.debug("Added history to humidor")
            this.dbObject = this.dbQuery.humidor(humidorID).executeAsOne()
            Log.debug("Humidor $this.dbObject")
            return@runDbQuery this
        }
    }

    fun addCigar(cigar: Cigar, count: Long) {
        runDbQuery {
           this.dbQuery.addCigarToHumidor(this.id, cigar.id)
            this.dbQuery.addHistory(count, Clock.System.now().toEpochMilliseconds(), count, 150.0, 1)
            val historyID = this.dbQuery.lastInsertRowId().executeAsOne()
            this.dbQuery.addHistoryToHumidor(this.id, historyID)
            return@runDbQuery null
        }
    }

    fun addHistory(cigar: History) {
        runDbQuery {
            this.dbQuery.addCigarToHumidor(this.id, cigar.id)
            val historyID = this.dbQuery.transactionWithResult {
                dbQuery.addHistory(
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    150.0,
                    1
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
            this.dbQuery.addHistoryToHumidor(this.id, historyID)
            return@runDbQuery null
        }
    }

    override fun addHistory(count: Long?, left: Long?, price: Double?,
                            type: Long?, date: Long?): History {
        return runBlocking {
            val history = super.addHistory(count, left, price, type, date)
            this@Humidor.dbQuery.addHistoryToHumidor(this@Humidor.id, history.id)
            return@runBlocking history
        }
    }

    fun update() {

    }

}

