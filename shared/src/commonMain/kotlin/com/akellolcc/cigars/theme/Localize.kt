package com.akellolcc.cigars.theme

import Strings
import androidx.compose.runtime.Composable
import com.akellolcc.cigars.MR
import com.vanniktech.locale.Locales
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

object Localize {
    init {
        val currentLocaleString = Locales.currentLocaleString()
        val i18n4kConfig = I18n4kConfigDefault()
        i18n4kConfig.locale = Locale(currentLocaleString)
        i18n4k = i18n4kConfig
    }

    val app_name: String = Strings.app_name.toString()
       

    val title_humidors: String = Strings.title_humidors.toString()

    val title_cigars: String = Strings.title_cigars.toString()

    val title_favorites: String = Strings.title_favorites.toString()

    val title_login: String = Strings.login.toString()

    val demo_humidor_name: String = Strings.demo_humidor_name.toString()

}
