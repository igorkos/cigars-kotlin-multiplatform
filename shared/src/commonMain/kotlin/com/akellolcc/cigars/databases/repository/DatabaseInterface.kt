package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.RepositoryType

interface DatabaseInterface {

    fun <R> getRepository(type: RepositoryType, args: Any? = null): R

    fun reset()

    fun numberOfEntriesIn(type: RepositoryType): Long

}