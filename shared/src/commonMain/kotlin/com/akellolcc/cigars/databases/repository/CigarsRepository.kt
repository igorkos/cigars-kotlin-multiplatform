/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 3:31 PM
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
 */

package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.badoo.reaktive.observable.ObservableWrapper

interface CigarsRepository : Repository<Cigar> {
    fun add(cigar: Cigar, humidor: Humidor): ObservableWrapper<Cigar>

    override fun update(entity: Cigar): ObservableWrapper<Cigar>

    fun updateFavorite(value: Boolean, cigar: Cigar): ObservableWrapper<Cigar>

    fun updateRating(value: Long, cigar: Cigar): ObservableWrapper<Cigar>

    fun numberOfEntries(): Long
}
