package com.akellolcc.cigars.screens

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.mvvm.CigarHistoryScreenViewModel
import com.akellolcc.cigars.mvvm.HistoryScreenViewModel
import com.akellolcc.cigars.navigation.NavRoute
import kotlin.jvm.Transient

class CigarHistoryScreen(route: NavRoute) : HistoryScreen(route) {

    @Transient
    override var viewModel = CigarHistoryScreenViewModel(route.data as Cigar) as HistoryScreenViewModel

}
