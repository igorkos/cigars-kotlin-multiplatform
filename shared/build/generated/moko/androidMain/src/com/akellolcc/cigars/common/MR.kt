package com.akellolcc.cigars.common

import com.akellolcc.cigars.common.R
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.ResourceContainer
import dev.icerock.moko.resources.StringResource

public actual object MR {
  public actual object strings : ResourceContainer<StringResource> {
    public actual val hello_world: StringResource = StringResource(R.string.hello_world)
  }

  public actual object plurals : ResourceContainer<PluralsResource> {
    public actual val chars_count: PluralsResource = PluralsResource(R.plurals.chars_count)
  }

  public actual object images : ResourceContainer<ImageResource> {
    public actual val album_icon: ImageResource = ImageResource(R.drawable.album_icon)

    public actual val appicon_ipad: ImageResource = ImageResource(R.drawable.appicon_ipad)

    public actual val appicon_iphone: ImageResource = ImageResource(R.drawable.appicon_iphone)

    public actual val arrows_arrow_move: ImageResource = ImageResource(R.drawable.arrows_arrow_move)

    public actual val background: ImageResource = ImageResource(R.drawable.background)

    public actual val bar_button_bg: ImageResource = ImageResource(R.drawable.bar_button_bg)

    public actual val camera_icon: ImageResource = ImageResource(R.drawable.camera_icon)

    public actual val check_mark: ImageResource = ImageResource(R.drawable.check_mark)

    public actual val cigar_01: ImageResource = ImageResource(R.drawable.cigar_01)

    public actual val cigar_01_binder: ImageResource = ImageResource(R.drawable.cigar_01_binder)

    public actual val cigar_02: ImageResource = ImageResource(R.drawable.cigar_02)

    public actual val cigar_02_binder: ImageResource = ImageResource(R.drawable.cigar_02_binder)

    public actual val cigar_03: ImageResource = ImageResource(R.drawable.cigar_03)

    public actual val cigar_03_binder: ImageResource = ImageResource(R.drawable.cigar_03_binder)

    public actual val cigar_04: ImageResource = ImageResource(R.drawable.cigar_04)

    public actual val cigar_04_binder: ImageResource = ImageResource(R.drawable.cigar_04_binder)

    public actual val cigar_05: ImageResource = ImageResource(R.drawable.cigar_05)

    public actual val cigar_05_binder: ImageResource = ImageResource(R.drawable.cigar_05_binder)

    public actual val cigar_icon: ImageResource = ImageResource(R.drawable.cigar_icon)

    public actual val cigar_icon_rating: ImageResource = ImageResource(R.drawable.cigar_icon_rating)

    public actual val cigar_icon_rating_no: ImageResource =
        ImageResource(R.drawable.cigar_icon_rating_no)

    public actual val cigar_image_bg: ImageResource = ImageResource(R.drawable.cigar_image_bg)

    public actual val cigar_row_bg: ImageResource = ImageResource(R.drawable.cigar_row_bg)

    public actual val cigar_row_bg_selected: ImageResource =
        ImageResource(R.drawable.cigar_row_bg_selected)

    public actual val cigars_tab_div: ImageResource = ImageResource(R.drawable.cigars_tab_div)

    public actual val cog_wheel: ImageResource = ImageResource(R.drawable.cog_wheel)

    public actual val dark_wood_background: ImageResource =
        ImageResource(R.drawable.dark_wood_background)

    public actual val default_sigars: ImageResource = ImageResource(R.drawable.default_sigars)

    public actual val empty_icon: ImageResource = ImageResource(R.drawable.empty_icon)

    public actual val facebook_like_button: ImageResource =
        ImageResource(R.drawable.facebook_like_button)

    public actual val favorites_icon: ImageResource = ImageResource(R.drawable.favorites_icon)

    public actual val gold_nameplate: ImageResource = ImageResource(R.drawable.gold_nameplate)

    public actual val humidor_image_bg: ImageResource = ImageResource(R.drawable.humidor_image_bg)

    public actual val humidor_row_bg: ImageResource = ImageResource(R.drawable.humidor_row_bg)

    public actual val humidor_row_bg_1: ImageResource = ImageResource(R.drawable.humidor_row_bg_1)

    public actual val humidor_row_bg_selected: ImageResource =
        ImageResource(R.drawable.humidor_row_bg_selected)

    public actual val humidor_row_div: ImageResource = ImageResource(R.drawable.humidor_row_div)

    public actual val icon_minus: ImageResource = ImageResource(R.drawable.icon_minus)

    public actual val list_icon: ImageResource = ImageResource(R.drawable.list_icon)

    public actual val loading_spinner: ImageResource = ImageResource(R.drawable.loading_spinner)

    public actual val nav_bar: ImageResource = ImageResource(R.drawable.nav_bar)

    public actual val next_button: ImageResource = ImageResource(R.drawable.next_button)

    public actual val next_icon: ImageResource = ImageResource(R.drawable.next_icon)

    public actual val plate: ImageResource = ImageResource(R.drawable.plate)

    public actual val popover_arrov: ImageResource = ImageResource(R.drawable.popover_arrov)

    public actual val popover_arrov_down: ImageResource =
        ImageResource(R.drawable.popover_arrov_down)

    public actual val prev_button: ImageResource = ImageResource(R.drawable.prev_button)

    public actual val settings: ImageResource = ImageResource(R.drawable.settings)

    public actual val shopping: ImageResource = ImageResource(R.drawable.shopping)

    public actual val shopping_icon: ImageResource = ImageResource(R.drawable.shopping_icon)

    public actual val shopping_sel: ImageResource = ImageResource(R.drawable.shopping_sel)

    public actual val star_highlighted_yel: ImageResource =
        ImageResource(R.drawable.star_highlighted_yel)

    public actual val strength_0: ImageResource = ImageResource(R.drawable.strength_0)

    public actual val strength_1: ImageResource = ImageResource(R.drawable.strength_1)

    public actual val strength_2: ImageResource = ImageResource(R.drawable.strength_2)

    public actual val strength_3: ImageResource = ImageResource(R.drawable.strength_3)

    public actual val strength_4: ImageResource = ImageResource(R.drawable.strength_4)

    public actual val tab_icon_albums: ImageResource = ImageResource(R.drawable.tab_icon_albums)

    public actual val tab_icon_camera: ImageResource = ImageResource(R.drawable.tab_icon_camera)

    public actual val tab_icon_cigars: ImageResource = ImageResource(R.drawable.tab_icon_cigars)

    public actual val tab_icon_favorites: ImageResource =
        ImageResource(R.drawable.tab_icon_favorites)

    public actual val tab_icon_gallery: ImageResource = ImageResource(R.drawable.tab_icon_gallery)

    public actual val tab_icon_humidors: ImageResource = ImageResource(R.drawable.tab_icon_humidors)

    public actual val tweet_button: ImageResource = ImageResource(R.drawable.tweet_button)

    public actual val wood_arrov: ImageResource = ImageResource(R.drawable.wood_arrov)

    public actual val wood_background: ImageResource = ImageResource(R.drawable.wood_background)

    public actual val wood_background_borders: ImageResource =
        ImageResource(R.drawable.wood_background_borders)
  }

  public actual object fonts : ResourceContainer<FontResource> {
    public actual object cormorant {
      public actual val italic: FontResource = FontResource(fontResourceId =
          R.font.cormorant_italic)
    }
  }

  public actual object files : ResourceContainer<FileResource> {
    public actual val some_file: FileResource = FileResource(rawResId = R.raw.some_file)
  }

  public actual object colors : ResourceContainer<ColorResource> {
    public actual val seed: ColorResource = ColorResource(resourceId = R.color.seed)

    public actual val color_primary: ColorResource = ColorResource(resourceId =
        R.color.color_primary)

    public actual val color_onPrimary: ColorResource = ColorResource(resourceId =
        R.color.color_onPrimary)

    public actual val color_primaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_primaryContainer)

    public actual val color_onPrimaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_onPrimaryContainer)

    public actual val color_secondary: ColorResource = ColorResource(resourceId =
        R.color.color_secondary)

    public actual val color_onSecondary: ColorResource = ColorResource(resourceId =
        R.color.color_onSecondary)

    public actual val color_secondaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_secondaryContainer)

    public actual val color_onSecondaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_onSecondaryContainer)

    public actual val color_tertiary: ColorResource = ColorResource(resourceId =
        R.color.color_tertiary)

    public actual val color_onTertiary: ColorResource = ColorResource(resourceId =
        R.color.color_onTertiary)

    public actual val color_tertiaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_tertiaryContainer)

    public actual val color_onTertiaryContainer: ColorResource = ColorResource(resourceId =
        R.color.color_onTertiaryContainer)

    public actual val color_error: ColorResource = ColorResource(resourceId = R.color.color_error)

    public actual val color_errorContainer: ColorResource = ColorResource(resourceId =
        R.color.color_errorContainer)

    public actual val color_onError: ColorResource = ColorResource(resourceId =
        R.color.color_onError)

    public actual val color_onErrorContainer: ColorResource = ColorResource(resourceId =
        R.color.color_onErrorContainer)

    public actual val color_background: ColorResource = ColorResource(resourceId =
        R.color.color_background)

    public actual val color_onBackground: ColorResource = ColorResource(resourceId =
        R.color.color_onBackground)

    public actual val color_outline: ColorResource = ColorResource(resourceId =
        R.color.color_outline)

    public actual val color_inverseOnSurface: ColorResource = ColorResource(resourceId =
        R.color.color_inverseOnSurface)

    public actual val color_inverseSurface: ColorResource = ColorResource(resourceId =
        R.color.color_inverseSurface)

    public actual val color_inversePrimary: ColorResource = ColorResource(resourceId =
        R.color.color_inversePrimary)

    public actual val color_shadow: ColorResource = ColorResource(resourceId = R.color.color_shadow)

    public actual val color_surfaceTint: ColorResource = ColorResource(resourceId =
        R.color.color_surfaceTint)

    public actual val color_outlineVariant: ColorResource = ColorResource(resourceId =
        R.color.color_outlineVariant)

    public actual val color_scrim: ColorResource = ColorResource(resourceId = R.color.color_scrim)

    public actual val color_surface: ColorResource = ColorResource(resourceId =
        R.color.color_surface)

    public actual val color_onSurface: ColorResource = ColorResource(resourceId =
        R.color.color_onSurface)

    public actual val color_surfaceVariant: ColorResource = ColorResource(resourceId =
        R.color.color_surfaceVariant)

    public actual val color_onSurfaceVariant: ColorResource = ColorResource(resourceId =
        R.color.color_onSurfaceVariant)
  }

  public actual object assets : ResourceContainer<AssetResource> {
    public actual val some_asset: AssetResource = AssetResource(path = "some_asset.txt")
  }
}
