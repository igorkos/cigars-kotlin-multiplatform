package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.DatabaseDriverFactory

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SqlDelightDatabaseDriverFactory : DatabaseDriverFactory<SqlDriver> {
    actual override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CigarsDatabase.Schema.synchronous(), "cigars.db")
    }
}