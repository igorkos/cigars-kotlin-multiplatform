/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/9/24, 1:52 PM
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

package com.akellolcc.cigars.databases.sqldelight.queries

import com.akellolcc.cigars.databases.CigarsTable
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.databases.models.HistoryType
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository

fun historyFactory(
    rowid: Long,
    count: Long,
    date: Long,
    left: Long,
    price: Double?,
    type: Long,
    cigarId: Long?,
    humidorTo: Long,
    humidorFrom: Long
): History {
    val humidorsRepository = createRepository(HumidorsRepository::class)
    val cigarRepository = createRepository(CigarsRepository::class)
    val humTo = humidorsRepository.getSync(humidorTo)
    val humFrom = humidorsRepository.getSync(humidorFrom)
    val cigar = cigarId?.let { cigarRepository.getSync(cigarId) }
    return History(
        rowid,
        count,
        date,
        left,
        price,
        HistoryType.fromLong(type),
        cigar,
        humTo,
        humFrom
    )
}

fun imageFactory(
    rowid: Long,
    image: String?,
    data: ByteArray,
    notes: String?,
    type: Long?,
    cigarId: Long?,
    humidorId: Long?
): CigarImage {
    return CigarImage(
        rowid = rowid,
        image = image,
        bytes = data,
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
    other: Long?,
    relations: String?
): Cigar {
    return Cigar(
        CigarsTable(
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
            strength,
            rating,
            myrating,
            notes,
            filler,
            link,
            count,
            shopping,
            favorites,
            price,
            other,
            relations
        )
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
    other: Long?
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
        other
    )
}

fun humidorCigarFactory(
    count: Long,
    humidorID: Long,
    cigarID: Long,
): HumidorCigar {
    val humidorsRepository = createRepository(HumidorsRepository::class)
    val cigarRepository = createRepository(CigarsRepository::class)
    val humidor = humidorsRepository.getSync(humidorID)
    val cigar = cigarRepository.getSync(cigarID)
    return HumidorCigar(
        count,
        humidor,
        cigar
    )
}
