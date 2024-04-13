package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar


interface CigarHumidorRepository : Repository<HumidorCigar>{
    fun add(entity: Humidor, count: Long)
    fun find(cigar: Cigar, humidor: Humidor): HumidorCigar?
}
