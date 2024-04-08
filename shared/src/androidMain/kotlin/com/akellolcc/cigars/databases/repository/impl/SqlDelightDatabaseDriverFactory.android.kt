package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.repository.DatabaseDriverFactory
import com.akellolcc.cigars.utils.appContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SqlDelightDatabaseDriverFactory : DatabaseDriverFactory<SqlDriver> {
    actual override fun createDriver(): SqlDriver {
        if (appContext == null) throw Exception("appContext is null")
        return AndroidSqliteDriver(CigarsDatabase.Schema.synchronous(), appContext!!, "cigars.db")
    }
}