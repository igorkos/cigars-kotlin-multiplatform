/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/17/24, 1:07 AM
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

package com.akellolcc.cigars.databases.repository.impl.queries

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.databases.extensions.emptyHumidor

fun historyFactory(
    rowid: Long,
    count: Long,
    date: Long,
    left: Long,
    price: Double?,
    type: Long,
    cigarId: Long,
    humidorTo: Long,
    humidorFrom: Long?
): History {
    return History(
        rowid,
        count,
        date,
        left,
        price,
        HistoryType.fromLong(type),
        cigarId,
        humidorTo,
        humidorFrom
    )
}

fun imageFactory(
    rowid: Long,
    image: String?,
    data_: ByteArray,
    notes: String?,
    type: Long?,
    cigarId: Long?,
    humidorId: Long?
): CigarImage {
    return CigarImage(
        rowid = rowid,
        image = image,
        bytes = data_,
        notes = notes,
        type = type,
        cigarId = cigarId,
        humidorId = humidorId
    )
}

fun cigarFactory(
    rowid: Long,
    name: String,
    brand: String?,
    country: String?,
    date: Long?,
    cigar: String,
    wrapper: String,
    binder: String,
    gauge: Long,
    length: String,
    strength: Long,
    rating: Long?,
    myrating: Long?,
    notes: String?,
    filler: String,
    link: String?,
    count: Long,
    shopping: Boolean,
    favorites: Boolean,
    price: Double?,
): Cigar {
    return Cigar(
        rowid,
        name,
        brand,
        country,
        date,
        cigar,
        wrapper,
        binder,
        gauge,
        length,
        CigarStrength.fromLong(strength),
        rating,
        myrating,
        notes,
        filler,
        link,
        count,
        shopping,
        favorites,
        price
    )
}

fun humidorFactory(
    rowid: Long,
    name: String,
    brand: String,
    holds: Long,
    count: Long,
    temperature: Long?,
    humidity: Double?,
    notes: String?,
    link: String?,
    price: Double?,
    autoOpen: Boolean?,
    sorting: Long?,
    type: Long,
): Humidor {
    return Humidor(
        rowid,
        name,
        brand,
        holds,
        count,
        temperature,
        humidity,
        notes,
        link,
        price,
        autoOpen == true,
        sorting,
        type,
    )
}

fun humidorCigarFactory(
    count: Long,
    humidor: Long,
    cigar: Long,
): HumidorCigar {
    return HumidorCigar(
        count,
        emptyHumidor.copy(rowid = humidor),
        emptyCigar.copy(rowid = cigar),
    )
}
