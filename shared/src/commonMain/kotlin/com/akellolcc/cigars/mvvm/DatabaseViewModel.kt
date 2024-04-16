package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.BaseEntity

open class DatabaseViewModel<T : BaseEntity, A> : ActionsViewModel<A>() {
    protected val database: Database = Database.instance
    var loading by mutableStateOf(false)
}