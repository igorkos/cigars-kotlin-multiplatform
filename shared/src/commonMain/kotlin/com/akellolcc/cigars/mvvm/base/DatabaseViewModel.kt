/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 1:44 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.mvvm.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.models.BaseEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

open class DatabaseViewModel<T : BaseEntity> : ActionsViewModel() {
    protected val database: Database = Database.instance
    var loading by mutableStateOf(false)

    protected fun <Q> executeQuery(query: Flow<Q>, onCompletion: ((Q) -> Unit)? = null) {
        screenModelScope.launch {
            loading = true
            query.cancellable().collect {
                loading = false
                onCompletion?.invoke(it)
                this.cancel()
            }
        }
    }

    protected fun <Q> execute(query: Flow<Q>, onCompletion: ((Q) -> Unit)? = null): Job {
        return screenModelScope.launch {
            query.cancellable().collect {
                onCompletion?.invoke(it)
            }
        }
    }
}