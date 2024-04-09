package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository


class FavoritesScreenViewModel : CigarsScreenViewModel() {
    override val repository: FavoriteCigarsRepository = database.getRepository(RepositoryType.Favorites)
}