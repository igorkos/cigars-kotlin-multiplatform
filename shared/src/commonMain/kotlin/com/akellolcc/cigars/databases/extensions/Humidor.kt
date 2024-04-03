package com.akellolcc.cigars.databases.extensions


class Humidor(
    public override var rowid: Long,
    public val name: String? = null,
    public val brand: String? = null,
    public val holds: Long? = null,
    public val count: Long? = null,
    public val temperature: Long? = null,
    public val humidity: Double? = null,
    public val notes: String? = null,
    public val link: String? = null,
    public val autoOpen: Boolean? = null,
    public val sorting: Long? = null,
    public val type: Long? = null,
) : BaseEntity(rowid){

   /* constructor(dbID: Long) : super() {
        load(dbID)
    }

    constructor(humidor: HumidorsTable) : super() {
        load(humidor.rowid)
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
            load(humidorID)
            Log.debug("Humidor $this.dbObject")
            return@runDbQuery this
        }
    }

    fun addCigar(cigar: Cigar, count: Long) {
        runDbQuery {
           this.dbQuery.addCigarToHumidor(this.id, cigar.rowid, count)
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

    override fun query(id: Long): Flow<HumidorsTable> {
        return this.dbQuery.humidor(id).asFlow().mapToOne(Dispatchers.Default)
    }

    fun update() {

    }
*/
}

