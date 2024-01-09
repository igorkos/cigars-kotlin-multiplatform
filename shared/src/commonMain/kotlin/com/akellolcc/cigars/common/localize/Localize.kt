package com.akellolcc.cigars.common.localize

import com.vanniktech.locale.Locales
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k

object Localize {
    init {
        val currentLocaleString = Locales.currentLocaleString()
        val i18n4kConfig = I18n4kConfigDefault()
        i18n4k = i18n4kConfig
        i18n4kConfig.locale = Locale(currentLocaleString)
    }

    val app_name: String = Strings.app_name.toString()

    val title_humidors: String = Strings.title_humidors.toString()

    val title_cigars: String = Strings.title_cigars.toString()

    val title_favorites: String = Strings.title_favorites.toString()

    val account_id_description: String = Strings.account_id_description.toString()

    val action_settings: String = Strings.action_settings.toString()

    val album_add_dialog_place_holder: String =
        Strings.album_add_dialog_place_holder.toString()

    val album_add_dialog_title: String =
        Strings.album_add_dialog_title.toString()

    val album_delete_dialog_messge: String =
        Strings.album_delete_dialog_messge.toString()

    val album_delete_dialog_title: String =
        Strings.album_delete_dialog_title.toString()

    val app_developer: String = Strings.app_developer.toString()


    val auth_options: String = Strings.auth_options.toString()

    val cancel_download: String = Strings.cancel_download.toString()

    val channel_description: String =
        Strings.channel_description.toString()

    val channel_name: String = Strings.channel_name.toString()

    val close: String = Strings.close.toString()

    val connecting: String = Strings.connecting.toString()

    val create: String = Strings.create.toString()

    val delete: String = Strings.delete.toString()

    val deselect: String = Strings.deselect.toString()

    val development_preferences: String =
        Strings.development_preferences.toString()

    val disagree: String = Strings.disagree.toString()

    val dropbox_import_choices_albums_no_subfolders: String =
        Strings.dropbox_import_choices_albums_no_subfolders.toString()

    val dropbox_import_choices_albums_subfolders: String =
        Strings.dropbox_import_choices_albums_subfolders.toString()

    val dropbox_import_choices_albums_subfolders_images: String =
        Strings.dropbox_import_choices_albums_subfolders_images.toString()

    val dropbox_import_choices_images_no_subfolders: String =
        Strings.dropbox_import_choices_images_no_subfolders.toString()

    val dropbox_import_choices_images_subfolders: String =
        Strings.dropbox_import_choices_images_subfolders.toString()

    val fullscreen_transition_name: String =
        Strings.fullscreen_transition_name.toString()

    val gallery_images_loading: String =
        Strings.gallery_images_loading.toString()

    val google_account_dialog_message: String =
        Strings.google_account_dialog_message.toString()

    val google_account_dialog_title: String =
        Strings.google_account_dialog_title.toString()

    val google_api_authorized: String =
        Strings.google_api_authorized.toString()

    val google_loading_albums: String =
        Strings.google_loading_albums.toString()

    val google_loading_images: String =
        Strings.google_loading_images.toString()

    val hello_blank_fragment: String =
        Strings.hello_blank_fragment.toString()

    val image: String = Strings.image.toString()

    val interstitial_ad_sample: String =
        Strings.interstitial_ad_sample.toString()

    val interstitial_ad_unit_id: String =
        Strings.interstitial_ad_unit_id.toString()

    val intro_header: String = Strings.intro_header.toString()

    val licence_checker_public_key: String =
        Strings.licence_checker_public_key.toString()

    val load: String = Strings.load.toString()

    val loading: String = Strings.loading.toString()

    val login_hint_value: String =
        Strings.login_hint_value.toString()

    val logout: String = Strings.logout.toString()

    val logout_message: String = Strings.logout_message.toString()

    val menu_about: String = Strings.menu_about.toString()

    val menu_import_dropbox: String =
        Strings.menu_import_dropbox.toString()

    val menu_logout: String = Strings.menu_logout.toString()

    val menu_settings: String = Strings.menu_settings.toString()

    val menu_update: String = Strings.menu_update.toString()

    val mnav_import_from_gallery: String =
        Strings.mnav_import_from_gallery.toString()

    val nav_header_subtitle: String =
        Strings.nav_header_subtitle.toString()

    val nav_header_title: String =
        Strings.nav_header_title.toString()

    val next_level: String = Strings.next_level.toString()

    val no: String = Strings.no.toString()

    val notification_title: String =
        Strings.notification_title.toString()

    val preference_fingerprint: String =
        Strings.preference_fingerprint.toString()

    val preference_sync_on_idle: String =
        Strings.preference_sync_on_idle.toString()

    val preference_sync_on_wifi: String =
        Strings.preference_sync_on_wifi.toString()

    val progress_loader_fetching: String =
        Strings.progress_loader_fetching.toString()

    val progress_loader_synchronisation: String =
        Strings.progress_loader_synchronisation.toString()

    val saufoto: String = Strings.saufoto.toString()

    val security_no_lock_set: String =
        Strings.security_no_lock_set.toString()

    val security_setup_disable_button: String =
        Strings.security_setup_disable_button.toString()

    val security_setup_lock_button: String =
        Strings.security_setup_lock_button.toString()

    val security_title: String = Strings.security_title.toString()

    val select_all: String = Strings.select_all.toString()

    val signed_in_title: String = Strings.signed_in_title.toString()

    val start_authorization: String = Strings.start_authorization.toString()

    val start_level: String = Strings.start_level.toString()

    val switch_browse: String = Strings.switch_browse.toString()

    val switch_select: String = Strings.switch_select.toString()

    val title_activity_about: String =
        Strings.title_activity_about.toString()

    val title_activity_admob: String =
        Strings.title_activity_admob.toString()

    val title_activity_auth: String =
        Strings.title_activity_auth.toString()

    val title_activity_gallery: String =
        Strings.title_activity_gallery.toString()

    val title_activity_import: String =
        Strings.title_activity_import.toString()

    val title_activity_settings: String =
        Strings.title_activity_settings.toString()

    val title_activity_update: String =
        Strings.title_activity_update.toString()

    val title_album_cover: String =
        Strings.title_album_cover.toString()

    val title_albums: String = Strings.title_albums.toString()

    val title_camera: String = Strings.title_camera.toString()

    val title_cancel_menu: String =
        Strings.title_cancel_menu.toString()

    val title_gallery: String = Strings.title_gallery.toString()

    val title_import_camera: String =
        Strings.title_import_camera.toString()

    val title_import_menu: String =
        Strings.title_import_menu.toString()

    val title_import_to_album: String =
        Strings.title_import_to_album.toString()

    val title_login: String = Strings.title_login.toString()

    val title_logout_button: String =
        Strings.title_logout_button.toString()

    val title_main_menu: String = Strings.title_main_menu.toString()

    val title_popup_menu_add: String =
        Strings.title_popup_menu_add.toString()

    val title_popup_menu_delete: String =
        Strings.title_popup_menu_delete.toString()

    val title_settings: String = Strings.title_settings.toString()

    val title_switch_fingerprint: String =
        Strings.title_switch_fingerprint.toString()

    val title_switch_on_idle: String =
        Strings.title_switch_on_idle.toString()

    val title_switch_on_wifi: String =
        Strings.title_switch_on_wifi.toString()

    val title_unlock_button: String =
        Strings.title_unlock_button.toString()

    val todo: String = Strings.todo.toString()

    val yes: String = Strings.yes.toString()

}
