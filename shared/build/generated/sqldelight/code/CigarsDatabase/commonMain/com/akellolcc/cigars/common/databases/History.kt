package com.akellolcc.cigars.common.databases

import kotlin.Double
import kotlin.Long

public data class History(
  public val id: Long,
  public val count: Long?,
  public val date: Long?,
  public val left: Long?,
  public val price: Double?,
  public val type: Long?,
)
