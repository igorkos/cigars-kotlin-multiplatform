package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import com.akellolcc.cigars.databases.HumidorsTable
import kotlinx.serialization.Serializable

var emptyHumidor = Humidor(-1, "", "", 0, 0)
@Stable
@Serializable
class Humidor(
    override var rowid: Long,
    var name: String,
    var brand: String,
    var holds: Long,
    var count: Long,
    var temperature: Long? = null,
    var humidity: Double? = null,
    var notes: String? = null,
    var link: String? = null,
    var price: Double? = null,
    var autoOpen: Boolean = false,
    var sorting: Long? = null,
    var type: Long = 0,
) : BaseEntity() {

    constructor(humidor: HumidorsTable) : this(
        humidor.rowid,
        humidor.name,
        humidor.brand,
        humidor.holds,
        humidor.count,
        humidor.temperature,
        humidor.humidity,
        humidor.notes,
        humidor.link,
        humidor.price,
        humidor.autoOpen == true,
        humidor.sorting,
        humidor.type
    )
}

