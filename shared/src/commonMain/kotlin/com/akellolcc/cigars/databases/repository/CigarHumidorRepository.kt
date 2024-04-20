/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.badoo.reaktive.observable.ObservableWrapper


interface CigarHumidorRepository : Repository<HumidorCigar> {
    fun add(cigar: Cigar, humidor: Humidor, count: Long): ObservableWrapper<HumidorCigar>

    fun find(cigar: Cigar, humidor: Humidor): HumidorCigar?

    fun remove(cigar: Cigar, from: Humidor): ObservableWrapper<Boolean>

    fun updateCount(
        entity: HumidorCigar,
        count: Long,
        price: Double? = null,
        historyType: HistoryType? = null,
        humidorTo: Humidor? = null
    ): ObservableWrapper<HumidorCigar>

    fun moveCigar(from: HumidorCigar, to: Humidor, count: Long): ObservableWrapper<Boolean>
}
