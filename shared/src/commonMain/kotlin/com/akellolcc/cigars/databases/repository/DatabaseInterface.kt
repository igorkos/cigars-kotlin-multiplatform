package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.RepositoryType

interface DatabaseInterface {

    fun <R: Repository<*>>getRepository(type: RepositoryType, args: Any? = null): R
    fun createDemoSet()

    fun reset()
}