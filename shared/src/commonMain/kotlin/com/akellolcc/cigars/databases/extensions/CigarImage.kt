package com.akellolcc.cigars.databases.extensions

class CigarImage(
    override val rowid: Long,
    val image: String? = null,
    val data_: ByteArray,
    val notes: String? = null,
    val type: Long? = null,
    val cigarId: Long? = null,
    val humidorId: Long? = null
) : BaseEntity(rowid) {


    /*constructor(dbID: Long) : super() {
        load(dbID)
    }

    constructor(image: ImagesTable) : super() {
        load(image.rowid)
    }

    constructor( data: ByteArray,
                 type: Long,
                 image: String? = null,
                 notes: String? = null
    ) : super() {
        runDbQuery {
            val imageID = this.dbQuery.transactionWithResult {
                dbQuery.addImage(data, type, image, notes)
                dbQuery.lastInsertRowId().executeAsOne()
            }
            load(imageID)
            return@runDbQuery this
        }
    }

    override fun query(id: Long): Flow<ImagesTable> {
        return this.dbQuery.image(id).asFlow().mapToOne(Dispatchers.Default)
    }*/
}