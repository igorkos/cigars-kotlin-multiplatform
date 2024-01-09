package com.akellolcc.cigars.common.databases

import kotlin.ByteArray
import kotlin.Long
import kotlin.String

public data class Images(
  public val id: Long,
  public val data_: ByteArray?,
  public val image: String?,
  public val notes: String?,
  public val type: Long?,
)
