package com.akellolcc.cigars.databases.extensions

/*
class History: DBObject<HistoryTable> {
    public val id: Long
        get() { return this.entity.value?.rowid ?: -1 }
    public val count: Long
        get() { return this.entity.value?.count ?: 0 }
    public val date: Long
        get() { return this.entity.value?.date ?: -1 }
    public val left: Long
        get() { return this.entity.value?.left ?: 0 }
    public val price: Double
        get() { return this.entity.value?.price ?: 0.0 }
    public val type: Long
        get() { return this.entity.value?.type ?: 0 }
    public val cigarId: Long
        get() { return this.entity.value?.cigarId ?: -1 }
    public val humidorId: Long
        get() { return this.entity.value?.humidorId ?: -1 }

    constructor(dbID: Long) : super() {
        load(dbID)
    }

    constructor(history: HistoryTable) : super() {
        load(history.rowid)
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
            load(historyID)
            return@runDbQuery this
        }
    }

    override fun query(id: Long): Flow<HistoryTable> {
        return this.dbQuery.history(id).asFlow().mapToOne(Dispatchers.Default)
    }
}

 */