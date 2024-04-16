package com.akellolcc.cigars.theme

import Strings
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.vanniktech.locale.Locales
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k

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

    val button_cancel = Strings.button_cancel.toString()
    val button_save = Strings.button_save.toString()

    val cigar_details_name = Strings.cigar_details_name.toString()
    val cigar_details_company = Strings.cigar_details_company.toString()
    val cigar_details_country = Strings.cigar_details_country.toString()

    val cigar_details_cigars = Strings.cigar_details_cigars.toString()
    val cigar_details_shape = Strings.cigar_details_shape.toString()
    val cigar_details_length = Strings.cigar_details_length.toString()
    val cigar_details_gauge = Strings.cigar_details_gauge.toString()

    val cigar_details_tobacco = Strings.cigar_details_tobacco.toString()
    val cigar_details_wrapper = Strings.cigar_details_wrapper.toString()
    val cigar_details_binder = Strings.cigar_details_binder.toString()
    val cigar_details_filler = Strings.cigar_details_filler.toString()
    val cigar_details_strength = Strings.cigar_details_strength.toString()

    val cigar_details_ratings = Strings.cigar_details_ratings.toString()
    val cigar_details_rating = Strings.cigar_details_rating.toString()
    val cigar_details_myrating = Strings.cigar_details_myrating.toString()
    val cigar_details_humidors = Strings.cigar_details_humidors.toString()
    val cigar_details_link = Strings.cigar_details_link.toString()
    val cigar_details_count_dialog = Strings.cigar_details_count_dialog.toString()
    val cigar_details_count_dialog_price = Strings.cigar_details_count_dialog_price.toString()
    val cigar_details_rating_dialog = Strings.cigar_details_rating_dialog.toString()
    val cigar_details_move_dialog = Strings.cigar_details_move_dialog.toString()
    val cigar_details_move_from = Strings.cigar_details_move_from.toString()
    val cigar_details_move_to = Strings.cigar_details_move_to.toString()
    val cigar_details_move_select = Strings.cigar_details_move_select.toString()
    val cigar_details_count = Strings.cigar_details_count.toString()

    val cigar_shape_corona = Strings.cigar_shape_corona.toString()
    val cigar_shape_petit_corona = Strings.cigar_shape_petit_corona.toString()
    val cigar_shape_churchill = Strings.cigar_shape_churchill.toString()
    val cigar_shape_robusto = Strings.cigar_shape_robusto.toString()
    val cigar_shape_corona_gorda = Strings.cigar_shape_corona_gorda.toString()
    val cigar_shape_double_corona = Strings.cigar_shape_double_corona.toString()
    val cigar_shape_panetela = Strings.cigar_shape_panetela.toString()
    val cigar_shape_lonsdale = Strings.cigar_shape_lonsdale.toString()
    val cigar_shape_grande = Strings.cigar_shape_grande.toString()
    val cigar_shape_pyramid = Strings.cigar_shape_pyramid.toString()
    val cigar_shape_belicoso = Strings.cigar_shape_belicoso.toString()
    val cigar_shape_torpedo = Strings.cigar_shape_torpedo.toString()
    val cigar_shape_perfecto = Strings.cigar_shape_perfecto.toString()
    val cigar_shape_culebra = Strings.cigar_shape_culebra.toString()
    val cigar_shape_diadema = Strings.cigar_shape_diadema.toString()
    val cigar_shape_toro_grande = Strings.cigar_shape_toro_grande.toString()
    val cigar_shape_figurado = Strings.cigar_shape_figurado.toString()
    val cigar_shape_gordo = Strings.cigar_shape_gordo.toString()
    val cigar_shape_toro = Strings.cigar_shape_toro.toString()
    val cigar_shape_compana = Strings.cigar_shape_compana.toString()
    val cigar_shape_bullet = Strings.cigar_shape_bullet.toString()
    val cigar_shape_pettit_figurado = Strings.cigar_shape_pettit_figurado.toString()
    val cigar_shape_pettit = Strings.cigar_shape_pettit.toString()
    val cigar_shape_cigarillo = Strings.cigar_shape_cigarillo.toString()

    val cigar_strength_mild = Strings.cigar_strength_mild.toString()
    val cigar_strength_mild_medium = Strings.cigar_strength_mild_medium.toString()
    val cigar_strength_medium = Strings.cigar_strength_medium.toString()
    val cigar_strength_medium_full = Strings.cigar_strength_medium_full.toString()
    val cigar_strength_full = Strings.cigar_strength_full.toString()

    val demo_humidor_name = Strings.demo_humidor_name.toString()

    val humidor_details_holds = Strings.humidor_details_holds.toString()
    val humidor_details_count = Strings.humidor_details_count.toString()
    val humidor_details_temperature = Strings.humidor_details_temperature.toString()
    val humidor_details_humidity = Strings.humidor_details_humidity.toString()
    val humidor_details_cigars = Strings.humidor_details_cigars.toString()
    val humidor_details_humidor = Strings.humidor_details_humidor.toString()

    val menu_item_camera: String = Strings.menu_item_camera.toString()
    val menu_item_gallery: String = Strings.menu_item_gallery.toString()

    val history_type_addition = Strings.history_type_addition.toString()
    val history_type_deletion = Strings.history_type_deletion.toString()
    val history_type_move = Strings.history_type_move.toString()

    fun humidor_cigars(v: Long, v1: Long): String {
        return Strings.humidor_cigars.createLocalizedString(v, v1).toString()
    }

    fun cigar_details_total(v: Long): String {
        return Strings.cigar_details_total.createLocalizedString(v).toString()
    }

    fun cigar_list_total(v: Long): String {
        return if (v == 1L) {
            Strings.cigar_details_count_value_one.createLocalizedString(v)
                .toString()
        } else {
            Strings.cigar_details_count_value_more.createLocalizedString(v)
                .toString()
        }
    }

    fun history_transaction_desc(v: HistoryType, v1: Long): String {
        return if (v1 == 0L || v1 > 1) {
            Strings.history_transaction_desc_more.createLocalizedString(v.toString(), v1)
                .toString()
        } else {
            Strings.history_transaction_desc_one.createLocalizedString(v.toString(), v1)
                .toString()
        }
    }

    fun history_transaction_price(v: Double): String {
        return Strings.history_transaction_price.createLocalizedString(v).toString()
    }

    fun history_humidor_added(v: HistoryType): String {
        return Strings.history_humidor_added.createLocalizedString(v.toString()).toString()
    }

}
