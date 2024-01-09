package com.akellolcc.cigars.common.databases

import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.akellolcc.cigars.common.databases.shared.newInstance
import com.akellolcc.cigars.common.databases.shared.schema
import kotlin.Unit

public interface CigarsDatabase : SuspendingTransacter {
  public val cigarsDatabaseQueries: CigarsDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.AsyncValue<Unit>>
      get() = CigarsDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): CigarsDatabase =
        CigarsDatabase::class.newInstance(driver)
  }
}
