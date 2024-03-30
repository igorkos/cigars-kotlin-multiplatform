package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.ImagesTable
import kotlinx.serialization.Serializable

@Serializable
class CigarImage : DBObject<ImagesTable> {

    public val id: Long
        get() { return this.dbObject?.rowid ?: -1 }
    public val bytes: ByteArray?
        get() { return this.dbObject?.data_ }
    public val image: String
        get() { return this.dbObject?.image ?: "" }
    public val notes: String
        get() { return this.dbObject?.notes ?: "" }
    public val type: Long
        get() { return this.dbObject?.type ?: 0 }
    public val cigarId: Long
        get() { return this.dbObject?.cigarId ?: -1 }
    public val humidorId: Long
        get() { return this.dbObject?.humidorId ?: -1 }
    constructor(dbID: Long) : super() {
        this.dbObject = this.dbQuery.image(dbID).executeAsOne()
    }

    constructor(image: ImagesTable) : super() {
        this.dbObject = image
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
            this.dbObject = this.dbQuery.image(imageID).executeAsOne()
            return@runDbQuery this
        }
    }
}