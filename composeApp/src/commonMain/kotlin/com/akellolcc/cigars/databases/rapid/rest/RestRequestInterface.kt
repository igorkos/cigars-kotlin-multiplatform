/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/16/24, 11:24 AM
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

package com.akellolcc.cigars.databases.rapid.rest

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow

open class RestRequestInterface<T : Any, L : Any> : PagingSource<Int, L>() {

    override fun getRefreshKey(state: PagingState<Int, L>): Int {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, L> {
        throw IllegalStateException("An operation is not supported")
    }

    open fun paging(): Flow<PagingData<L>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 20,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { this },
            initialKey = 1
        ).flow
    }

    open fun flow(): Flow<T> {
        throw IllegalStateException("An operation is not supported")
    }

    open fun sync(): T {
        throw IllegalStateException("An operation is not supported")
    }

}