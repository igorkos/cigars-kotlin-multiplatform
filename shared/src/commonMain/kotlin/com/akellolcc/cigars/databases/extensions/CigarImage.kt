package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
@Stable
@Serializable
class CigarImage(
    override var rowid: Long,
    val image: String? = null,
    var bytes: ByteArray,
    val notes: String? = null,
    val type: Long? = null,
    val cigarId: Long? = null,
    val humidorId: Long? = null
) : BaseEntity()