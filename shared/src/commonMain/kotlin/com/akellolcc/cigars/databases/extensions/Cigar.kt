package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.CigarsTable
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
public data class Image( public val file: String?,
                         public val type: Long?)

@Serializable
class Cigar : DBObject<CigarsTable> {
    public val id: Long
        get() { return this.dbObject?.id ?: -1 }
    public val name: String
        get() { return this.dbObject?.name ?: "" }
    public val brand: String?
        get() { return this.dbObject?.brand}
    public val country: String?
        get() { return this.dbObject?.country}
    public val date: Long?
        get() { return this.dbObject?.date}
    public val cigar: String?
        get() { return this.dbObject?.cigar}
    public val wrapper: String?
        get() { return this.dbObject?.wrapper}
    public val binder: String?
        get() { return this.dbObject?.binder}
    public val gauge: Double?
        get() { return this.dbObject?.gauge}
    public val length: String?
        get() { return this.dbObject?.length}
    public val strength: Long?
        get() { return this.dbObject?.strength}
    public val rating: Long
        get() { return this.dbObject?.rating ?: 0 }
    public val myrating: Long
        get() { return this.dbObject?.myrating ?: 0 }
    public val notes: String?
        get() { return this.dbObject?.notes}
    public val filler: String?
        get() { return this.dbObject?.filler}
    public val link: String?
        get() { return this.dbObject?.link}
    public val shopping: Boolean
        get() { return this.dbObject?.shopping ?: false}
    public val favorites: Boolean
        get() { return this.dbObject?.favorites ?: false}

    constructor(dbID: Long) : super() {
        this.dbObject = this.dbQuery.cigar(dbID).executeAsOne()
    }

    constructor(cigar: CigarsTable) : super() {
        this.dbObject = cigar
    }

    constructor( name: String,
                brand: String,
                country: String,
                count: Long,
                price: Double,
                cigar: String? = null,
                wrapper: String? = null,
                binder: String? = null,
                gauge: Double = 0.0,
                length: String? = null,
                strength: Long = 0,
                rating: Long = 0,
                myrating: Long = 0,
                notes: String? = null,
                filler: String? = null,
                link: String? = null,
                shopping: Boolean = false,
                favorites: Boolean? = false,
                 date: Long = Clock.System.now().toEpochMilliseconds()) : super() {
        runDbQuery {
            //Add humidor
            this.dbQuery.addCigar(name, brand, country, date, cigar, wrapper, binder, gauge, length, strength, rating, myrating, notes, filler, link, shopping, favorites)
            val cigarID = this.dbQuery.lastInsertRowId().executeAsOne()
            //Add History item to humidor
            val history = this.addHistory(count, count, price, 1)
            this.dbQuery.addHistoryToCigar(cigarID, history.id)
            this.dbObject = this.dbQuery.cigar(cigarID).executeAsOne();
            return@runDbQuery null
        }
    }

    override fun addHistory(count: Long?, left: Long?, price: Double?,
                            type: Long?, date: Long?): History {
        return runBlocking {
            val history = super.addHistory(count, left, price, type, date)
            this@Cigar.dbQuery.addHistoryToCigar(this@Cigar.id, history.id)
            return@runBlocking history
        }
    }

}