package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.db.SqlDriver
import com.akellolcc.cigars.databases.repository.DatabaseDriverFactory

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SqlDelightDatabaseDriverFactory() : DatabaseDriverFactory<SqlDriver> {
    override fun createDriver(): SqlDriver
}

