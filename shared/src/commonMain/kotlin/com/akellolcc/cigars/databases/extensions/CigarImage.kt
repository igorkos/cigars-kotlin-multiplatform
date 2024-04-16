package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
class CigarImage(
    override var rowid: Long,
    val image: String? = null,
    var bytes: ByteArray,
    var notes: String? = null,
    var type: Long? = null,
    var cigarId: Long? = null,
    var humidorId: Long? = null
) : BaseEntity()