package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.badoo.reaktive.observable.ObservableWrapper


interface CigarHumidorRepository : Repository<HumidorCigar> {
    fun add(cigar: Cigar, humidor: Humidor, count: Long): ObservableWrapper<HumidorCigar>

    fun find(cigar: Cigar, humidor: Humidor): HumidorCigar?

    fun remove(cigar: Cigar, from: Humidor)

    fun updateCount(cigar: Cigar, where: Humidor, count: Long)
}
