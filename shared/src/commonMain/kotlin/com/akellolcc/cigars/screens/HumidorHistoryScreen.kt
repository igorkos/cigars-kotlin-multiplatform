package com.akellolcc.cigars.screens

import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.mvvm.HistoryScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorHistoryScreenViewModel
import com.akellolcc.cigars.navigation.NavRoute
import kotlin.jvm.Transient

class HumidorHistoryScreen(route: NavRoute) : HistoryScreen(route) {

    @Transient
    override var viewModel =
        HumidorHistoryScreenViewModel(route.data as Humidor) as HistoryScreenViewModel
}
