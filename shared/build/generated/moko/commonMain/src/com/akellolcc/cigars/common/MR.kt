package com.akellolcc.cigars.common

import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.ResourceContainer
import dev.icerock.moko.resources.StringResource

public expect object MR {
  public object strings : ResourceContainer<StringResource> {
    public val hello_world: StringResource
  }

  public object plurals : ResourceContainer<PluralsResource> {
    public val chars_count: PluralsResource
  }

  public object images : ResourceContainer<ImageResource> {
    public val album_icon: ImageResource

    public val appicon_ipad: ImageResource

    public val appicon_iphone: ImageResource

    public val arrows_arrow_move: ImageResource

    public val background: ImageResource

    public val bar_button_bg: ImageResource

    public val camera_icon: ImageResource

    public val check_mark: ImageResource

    public val cigar_01: ImageResource

    public val cigar_01_binder: ImageResource

    public val cigar_02: ImageResource

    public val cigar_02_binder: ImageResource

    public val cigar_03: ImageResource

    public val cigar_03_binder: ImageResource

    public val cigar_04: ImageResource

    public val cigar_04_binder: ImageResource

    public val cigar_05: ImageResource

    public val cigar_05_binder: ImageResource

    public val cigar_icon: ImageResource

    public val cigar_icon_rating: ImageResource

    public val cigar_icon_rating_no: ImageResource

    public val cigar_image_bg: ImageResource

    public val cigar_row_bg: ImageResource

    public val cigar_row_bg_selected: ImageResource

    public val cigars_tab_div: ImageResource

    public val cog_wheel: ImageResource

    public val dark_wood_background: ImageResource

    public val default_sigars: ImageResource

    public val empty_icon: ImageResource

    public val facebook_like_button: ImageResource

    public val favorites_icon: ImageResource

    public val gold_nameplate: ImageResource

    public val humidor_image_bg: ImageResource

    public val humidor_row_bg: ImageResource

    public val humidor_row_bg_1: ImageResource

    public val humidor_row_bg_selected: ImageResource

    public val humidor_row_div: ImageResource

    public val icon_minus: ImageResource

    public val list_icon: ImageResource

    public val loading_spinner: ImageResource

    public val nav_bar: ImageResource

    public val next_button: ImageResource

    public val next_icon: ImageResource

    public val plate: ImageResource

    public val popover_arrov: ImageResource

    public val popover_arrov_down: ImageResource

    public val prev_button: ImageResource

    public val settings: ImageResource

    public val shopping: ImageResource

    public val shopping_icon: ImageResource

    public val shopping_sel: ImageResource

    public val star_highlighted_yel: ImageResource

    public val strength_0: ImageResource

    public val strength_1: ImageResource

    public val strength_2: ImageResource

    public val strength_3: ImageResource

    public val strength_4: ImageResource

    public val tab_icon_albums: ImageResource

    public val tab_icon_camera: ImageResource

    public val tab_icon_cigars: ImageResource

    public val tab_icon_favorites: ImageResource

    public val tab_icon_gallery: ImageResource

    public val tab_icon_humidors: ImageResource

    public val tweet_button: ImageResource

    public val wood_arrov: ImageResource

    public val wood_background: ImageResource

    public val wood_background_borders: ImageResource
  }

  public object fonts : ResourceContainer<FontResource> {
    public object cormorant {
      public val italic: FontResource
    }
  }

  public object files : ResourceContainer<FileResource> {
    public val some_file: FileResource
  }

  public object colors : ResourceContainer<ColorResource> {
    public val seed: ColorResource

    public val color_primary: ColorResource

    public val color_onPrimary: ColorResource

    public val color_primaryContainer: ColorResource

    public val color_onPrimaryContainer: ColorResource

    public val color_secondary: ColorResource

    public val color_onSecondary: ColorResource

    public val color_secondaryContainer: ColorResource

    public val color_onSecondaryContainer: ColorResource

    public val color_tertiary: ColorResource

    public val color_onTertiary: ColorResource

    public val color_tertiaryContainer: ColorResource

    public val color_onTertiaryContainer: ColorResource

    public val color_error: ColorResource

    public val color_errorContainer: ColorResource

    public val color_onError: ColorResource

    public val color_onErrorContainer: ColorResource

    public val color_background: ColorResource

    public val color_onBackground: ColorResource

    public val color_outline: ColorResource

    public val color_inverseOnSurface: ColorResource

    public val color_inverseSurface: ColorResource

    public val color_inversePrimary: ColorResource

    public val color_shadow: ColorResource

    public val color_surfaceTint: ColorResource

    public val color_outlineVariant: ColorResource

    public val color_scrim: ColorResource

    public val color_surface: ColorResource

    public val color_onSurface: ColorResource

    public val color_surfaceVariant: ColorResource

    public val color_onSurfaceVariant: ColorResource
  }

  public object assets : ResourceContainer<AssetResource> {
    public val some_asset: AssetResource
  }
}
