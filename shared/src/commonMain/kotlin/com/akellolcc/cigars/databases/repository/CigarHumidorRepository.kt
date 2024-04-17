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

    fun updateCount(entity: HumidorCigar, count: Long, price: Double? = null, historyType: HistoryType? = null): ObservableWrapper<HumidorCigar>

    fun moveCigar(from: HumidorCigar, to: Humidor, count: Long): ObservableWrapper<Boolean>
}
