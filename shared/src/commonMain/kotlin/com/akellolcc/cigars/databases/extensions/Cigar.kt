package com.akellolcc.cigars.databases.extensions

val emptyCigar = Cigar(0, "", "", "", 0, "", "", "", 0, "", 0, 0, 0, "", "", "", false, false)
data class Cigar(
    override var rowid: Long,
    val name: String,
    val brand: String?,
    val country: String?,
    val date: Long?,
    val cigar: String,
    val wrapper: String,
    val binder: String,
    val gauge: Long,
    val length: String,
    val strength: Long,
    val rating: Long?,
    val myrating: Long?,
    val notes: String?,
    val filler: String,
    val link: String?,
    val shopping: Boolean,
    var favorites: Boolean) : BaseEntity(rowid) {

  /*  constructor(dbID: Long) {
        load(dbID)
    }

    constructor(cigar: CigarsTable) : super() {
        load(cigar.rowid)
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
            load(cigarID)
            return@runDbQuery this
        }
    }

    override fun query(id: Long): Flow<CigarsTable> {
       return this.dbQuery.cigar(id).asFlow().mapToOne(Dispatchers.Default)
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

    fun setFavorite(favorite: Boolean) {
        runBlocking {
            dbQuery.setFavoriteCigar(favorite, id)
            entity.value = dbQuery.cigar(id).executeAsOne();
            Log.debug("Cigar setFavorite: $favorite ${entity.value}")
        }
    }*/
}