package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.databases.CigarsTable
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
class Cigar : DBObject<CigarsTable> {
     val id: Long
        get() { return this.dbObject?.rowid ?: -1 }
     val name: String
        get() { return this.dbObject?.name ?: "" }
     val brand: String?
        get() { return this.dbObject?.brand}
     val country: String?
        get() { return this.dbObject?.country}
     val date: Long?
        get() { return this.dbObject?.date}
     val cigar: String?
        get() { return this.dbObject?.cigar}
     val wrapper: String?
        get() { return this.dbObject?.wrapper}
     val binder: String?
        get() { return this.dbObject?.binder}
     val gauge: Double?
        get() { return this.dbObject?.gauge}
     val length: String?
        get() { return this.dbObject?.length}
     val strength: Long?
        get() { return this.dbObject?.strength}
     val rating: Long
        get() { return this.dbObject?.rating ?: 0 }
     val myrating: Long
        get() { return this.dbObject?.myrating ?: 0 }
     val notes: String?
        get() { return this.dbObject?.notes}
     val filler: String?
        get() { return this.dbObject?.filler}
     val link: String?
        get() { return this.dbObject?.link}
     val shopping: Boolean
        get() { return this.dbObject?.shopping ?: false}
     val favorites: Boolean
        get() { return this.dbObject?.favorites ?: false}

    val images: List<CigarImage>
        get() {
            return dbQuery.cigarImages(this.id).executeAsList().map {
                CigarImage(it)
            }
        }

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
            val cigarID = this.dbQuery.transactionWithResult {
                dbQuery.addCigar(
                    name,
                    brand,
                    country,
                    date,
                    cigar,
                    wrapper,
                    binder,
                    gauge,
                    length,
                    strength,
                    rating,
                    myrating,
                    notes,
                    filler,
                    link,
                    shopping,
                    favorites
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
            //Add History item to humidor
          //  val history = this.addHistory(count, count, price, 1)
          //  this.dbQuery.addHistoryToCigar(cigarID, history.id)
            this.dbObject = this.dbQuery.cigar(cigarID).executeAsOne();
            return@runDbQuery this
        }
    }

    override fun addHistory(count: Long?, left: Long?, price: Double?,
                            type: Long?, date: Long?): History {
        return runBlocking {
            val history = super.addHistory(count, left, price, type, date)
            dbQuery.addHistoryToCigar(this@Cigar.id, history.id)
            return@runBlocking history
        }
    }

    fun addImage(image: SharedImage): CigarImage {
        return runBlocking {
            val cImage = CigarImage(image.toByteArray()!!, 0)
            dbQuery.addImageToCigar(this@Cigar.id, cImage.id)
            return@runBlocking cImage
        }
    }
}