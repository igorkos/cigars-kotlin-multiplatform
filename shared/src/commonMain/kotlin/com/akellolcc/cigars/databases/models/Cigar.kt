/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/9/24, 2:29 PM
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

package com.akellolcc.cigars.databases.models

import androidx.compose.runtime.Stable
import com.akellolcc.cigars.databases.CigarsTable
import com.akellolcc.cigars.theme.Localize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


var emptyCigar =
    Cigar(
        -1,
        "",
        "",
        "",
        0,
        "",
        "",
        "",
        0,
        "",
        CigarStrength.Mild,
        0,
        0,
        "",
        "",
        "",
        0,
        shopping = false,
        favorites = false,
        price = 0.0,
        other = null
    )

@Stable
@Serializable
data class Relations(val source: String, val id: Long)

@Stable
@Serializable
data class Cigar(
    override var rowid: Long,
    var name: String,
    var brand: String?,
    var country: String?,
    var date: Long?,
    var cigar: String,
    var wrapper: String,
    var binder: String,
    var gauge: Long,
    var length: String,
    var strength: CigarStrength,
    var rating: Long?,
    var myrating: Long?,
    var notes: String?,
    var filler: String,
    var link: String?,
    var count: Long,
    var shopping: Boolean,
    var favorites: Boolean,
    var price: Double?,
    var other: Long? = null,
    var relations: List<Relations>? = null
) : BaseEntity() {

    constructor(cigar: CigarsTable) : this(
        cigar.rowid,
        cigar.name,
        cigar.brand,
        cigar.country,
        cigar.date,
        cigar.cigar,
        cigar.wrapper,
        cigar.binder,
        cigar.gauge,
        cigar.length,
        CigarStrength.fromLong(cigar.strength),
        cigar.rating,
        cigar.myrating,
        cigar.notes,
        cigar.filler,
        cigar.link,
        cigar.count,
        cigar.shopping,
        cigar.favorites,
        cigar.price,
        cigar.other
    ) {
        cigar.relations?.let {
            val rel = Json.decodeFromString<List<Relations>>(it)
            this.relations = rel
        }
    }
}

enum class CigarShapes {
    Belicoso,
    Bullet,
    Campana,
    Churchill,
    Cigarillo,
    Corona,
    CoronaGorda,
    Culebra,
    Diadema,
    DoubleCorona,
    Figurado,
    Gordo,
    Grande,
    Lonsdale,
    Panetela,
    Perfecto,
    PetitCorona,
    PetitFigurado,
    Petit,
    Pyramide,
    Robusto,
    Toro,
    ToroGrande,
    Torpedo,
    Other;

    companion object {
        fun enumValues(): List<Pair<CigarShapes, String>> {
            return CigarShapes.entries.map {
                it to localized(it)
            }
        }

        fun fromString(value: String): CigarShapes {
            return CigarShapes.entries.firstOrNull {
                it.name == value
            } ?: Other
        }

        private fun localized(value: CigarShapes): String {
            return when (value) {
                Corona -> Localize.cigar_shape_corona
                PetitCorona -> Localize.cigar_shape_petit_corona
                Churchill -> Localize.cigar_shape_churchill
                Robusto -> Localize.cigar_shape_robusto
                CoronaGorda -> Localize.cigar_shape_corona_gorda
                DoubleCorona -> Localize.cigar_shape_double_corona
                Panetela -> Localize.cigar_shape_panetela
                Lonsdale -> Localize.cigar_shape_lonsdale
                Grande -> Localize.cigar_shape_grande
                Pyramide -> Localize.cigar_shape_pyramid
                Belicoso -> Localize.cigar_shape_belicoso
                Torpedo -> Localize.cigar_shape_torpedo
                Perfecto -> Localize.cigar_shape_perfecto
                Culebra -> Localize.cigar_shape_culebra
                Diadema -> Localize.cigar_shape_diadema
                ToroGrande -> Localize.cigar_shape_toro_grande
                Figurado -> Localize.cigar_shape_figurado
                Gordo -> Localize.cigar_shape_gordo
                Toro -> Localize.cigar_shape_toro
                Campana -> Localize.cigar_shape_compana
                Bullet -> Localize.cigar_shape_bullet
                PetitFigurado -> Localize.cigar_shape_pettit_figurado
                Petit -> Localize.cigar_shape_pettit
                Cigarillo -> Localize.cigar_shape_cigarillo
                Other -> ""
            }
        }
    }
}

@Serializable
enum class CigarStrength {
    Mild,
    MildToMedium,
    Medium,
    MediumToFull,
    Full,
    Unknown;

    companion object {
        fun enumValues(): List<Pair<CigarStrength, String>> {
            return CigarStrength.entries.filter { it != Unknown }.map {
                it to localized(it)
            }
        }

        fun fromString(value: String?): CigarStrength? {
            if (value == null) return null
            return CigarStrength.entries.firstOrNull {
                it.name == value
            }
        }

        fun localized(value: CigarStrength): String {
            return when (value) {
                Mild -> Localize.cigar_strength_mild
                MildToMedium -> Localize.cigar_strength_mild_medium
                Medium -> Localize.cigar_strength_medium
                MediumToFull -> Localize.cigar_strength_medium_full
                Full -> Localize.cigar_strength_full
                Unknown -> ""
            }
        }

        fun fromLong(value: Long): CigarStrength = when (value) {
            0L -> Mild
            1L -> MildToMedium
            2L -> Medium
            3L -> MediumToFull
            4L -> Full
            else -> Unknown
        }

        fun toLong(value: CigarStrength): Long = when (value) {
            Mild -> 0
            MildToMedium -> 1
            Medium -> 2
            MediumToFull -> 3
            Full -> 4
            Unknown -> 5
        }
    }
}

@Serializable
enum class CigarSortingFields(val value: String) {
    Name("name"),

    Brand("brand"),
    Country("country"),

    //   Date("date"),
    Shape("cigar"),
    Gauge("gauge"),
    Length("length");
    // Strength("strength"),
    //  Rating("rating"),
    //  Myrating("myrating");

    companion object {
        fun enumValues(): Array<Pair<CigarSortingFields, String>> {
            return CigarSortingFields.entries.map {
                it to localized(it)
            }.toTypedArray()
        }

        private fun localized(value: CigarSortingFields): String {
            return when (value) {
                Name -> Localize.cigar_details_name
                Brand -> Localize.cigar_details_company
                Country -> Localize.cigar_details_country
                //    Date -> Localize.cigar_details_name
                Shape -> Localize.cigar_details_shape
                Gauge -> Localize.cigar_details_gauge
                Length -> Localize.cigar_details_length
                //    Strength -> Localize.cigar_details_strength
                //    Rating -> Localize.cigar_details_rating
                //    Myrating -> Localize.cigar_details_myrating
            }
        }
    }
}
