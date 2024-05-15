/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:34 PM
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

package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.Humidor
import kotlinx.coroutines.flow.Flow

interface CigarsRepository : Repository<Cigar> {
    fun add(cigar: Cigar, humidor: Humidor): Flow<Cigar>

    fun addAll(cigars: List<Cigar>, humidor: Humidor): Flow<List<Cigar>>

    override fun update(entity: Cigar): Flow<Cigar>

    fun updateFavorite(value: Boolean, cigar: Cigar): Flow<Boolean>

    fun updateRating(value: Long, cigar: Cigar): Flow<Long>
}

interface CigarsSearchRepository : CigarsRepository