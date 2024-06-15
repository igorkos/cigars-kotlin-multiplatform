/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 3:00 PM
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
import androidx.paging.PagingData
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.databases.models.CigarImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseDetailsScreenViewModel<T : BaseEntity>(protected var entity: T) : DatabaseViewModel<T>() {
    protected var disposable: List<Job> = mutableListOf()

    var editing by mutableStateOf(entity.rowid < 0)
    var images by mutableStateOf<Flow<PagingData<CigarImage>>?>(null)
    private var observing by mutableStateOf(false)

    /**
     * Observing cigar state
     */
    fun observeObject() {
        if (entity.rowid >= 0 && !observing) {
            observe()
            observing = true
        }
    }

    protected abstract fun observe()

    /**
     * Editing cigar
     */
    abstract fun verifyFields(): Boolean

    abstract fun onSaveEdit()

    fun onCancelEdit() {
        if (entity.rowid >= 0 && !observing) {
            observeObject()
        } else {
            onBackPress()
        }
        editing = false
    }

    /**
     * Events
     */

    fun historyOpen() {
        sendEvent(DetailsAction.OpenHistory(entity))
    }

    fun showImages(selected: Int) {
        sendEvent(DetailsAction.ShowImages(entity, selected))
    }

    override fun onDispose() {
        super.onDispose()
        if (disposable.isNotEmpty()) {
            disposable.forEach {
                it.cancel()
            }
            disposable = mutableListOf()
        }
    }

    sealed interface DetailsAction : CommonAction {
        data class OpenHistory(val entity: BaseEntity) : DetailsAction
        data class ShowImages(val entity: BaseEntity, val selected: Int) : DetailsAction
    }
}
