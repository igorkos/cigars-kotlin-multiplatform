package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.badoo.reaktive.observable.ObservableWrapper

interface CigarsRepository : Repository<Cigar> {
    fun add(cigar: Cigar, humidor: Humidor): ObservableWrapper<Cigar>

    override fun update(entity: Cigar): ObservableWrapper<Cigar>

    fun updateFavorite(value: Boolean, cigar: Cigar): ObservableWrapper<Cigar>

    fun updateRating(value: Long, cigar: Cigar): ObservableWrapper<Cigar>
}
