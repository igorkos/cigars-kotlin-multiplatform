package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.CigarsTable
import com.akellolcc.cigars.theme.Localize

var emptyCigar =
    Cigar(-1, "", "", "", 0, "", "", "", 0, "", CigarStrength.Mild, 0, 0, "", "", "", false, false)

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
    var shopping: Boolean,
    var favorites: Boolean
) : BaseEntity(rowid) {

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
        cigar.shopping,
        cigar.favorites
    )
}

enum class CigarShapes {
    Corona,
    PetitCorona,
    Churchill,
    Robusto,
    CoronaGorda,
    DoubleCorona,
    Panetela,
    Lonsdale,
    Grande,
    Pyramid,
    Belicoso,
    Torpedo,
    Perfecto,
    Culebra,
    Diadema;

    companion object {
        fun enumValues(): Array<Pair<CigarShapes, String>> {
            return CigarShapes.entries.map {
                it to localized(it)
            }.toTypedArray()
        }

        fun localized(value: CigarShapes): String {
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
                Pyramid -> Localize.cigar_shape_pyramid
                Belicoso -> Localize.cigar_shape_belicoso
                Torpedo -> Localize.cigar_shape_torpedo
                Perfecto -> Localize.cigar_shape_perfecto
                Culebra -> Localize.cigar_shape_culebra
                Diadema -> Localize.cigar_shape_diadema
            }
        }
    }
}

enum class CigarStrength {
    Mild,
    MildToMedium,
    Medium,
    MediumToFull,
    Full;

    companion object {
        fun enumValues(): Array<Pair<CigarStrength, String>> {
            return CigarStrength.entries.map {
                it to localized(it)
            }.toTypedArray()
        }

        fun localized(value: CigarStrength): String {
            return when (value) {
                Mild -> Localize.cigar_strength_mild
                MildToMedium -> Localize.cigar_strength_mild_medium
                Medium -> Localize.cigar_strength_medium
                MediumToFull -> Localize.cigar_strength_medium_full
                Full -> Localize.cigar_strength_full
            }
        }

        inline fun fromLong(value: Long): CigarStrength = when (value) {
            0L -> Mild
            1L -> MildToMedium
            2L -> Medium
            3L -> MediumToFull
            4L -> Full
            else -> Mild
        }

        inline fun toLong(value: CigarStrength): Long = when (value) {
            Mild -> 0
            MildToMedium -> 1
            Medium -> 2
            MediumToFull -> 3
            Full -> 4
        }
    }
}

