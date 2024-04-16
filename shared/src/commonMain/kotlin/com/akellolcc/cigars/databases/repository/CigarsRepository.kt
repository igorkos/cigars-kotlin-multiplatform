package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.badoo.reaktive.observable.ObservableWrapper

interface CigarsRepository : Repository<Cigar> {
    fun add(cigar: Cigar, humidor: Humidor): ObservableWrapper<Cigar>
}
