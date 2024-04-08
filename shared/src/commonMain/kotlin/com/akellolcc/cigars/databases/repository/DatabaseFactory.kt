package com.akellolcc.cigars.databases.repository

interface DatabaseDriverFactory<T> {
    fun createDriver(): T
}


