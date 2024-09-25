package com.example.krishnamaya1

import androidx.annotation.DrawableRes

sealed class Screens(val title:String, val route:String) {

    sealed class DrawerScreens(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int): Screens(dTitle, dRoute) {
        object SpecialMantras: DrawerScreens("Special Mantras", "special-mantras", R.drawable.om_chanting)
        object ChantFlow: DrawerScreens("Chant Flow", "chant-flow", R.drawable.chant_flow)
        object BhagwadGeetaNotes: DrawerScreens("Bhagwad Geeta Notes", "bhagwad-geeta-notes", R.drawable.bhagwad_geeta_notes)
        object MediaLibrary: DrawerScreens("Media Library", "media-library", R.drawable.media_library)
        object VaishnavCalendar: DrawerScreens("Vaishnav Calendar", "vaishnav-calendar", R.drawable.vaishnav_calendar)
        object Wallpapers: DrawerScreens("Wallpapers", "wallpapers", R.drawable.wallpapers)
        object ContactUs: DrawerScreens("Contact Us", "contact-us", R.drawable.contact_us)
        object Donate: DrawerScreens("Donate", "donate", R.drawable.donate)
        object Share: DrawerScreens("Share", "share", R.drawable.share)
        object AboutUs: DrawerScreens("About Us", "about-us", R.drawable.about)
    }

    sealed class BottomBarScreens(val bTitle: String, val bRoute: String, @DrawableRes val icon: Int): Screens(bTitle, bRoute) {
        object Discussion: BottomBarScreens("Discussion", "discussion", R.drawable.discussion_forum)
        object SearchUsers: BottomBarScreens("Search Users", "search-users", R.drawable.live_stream)
        object HomeScreen: BottomBarScreens("Home Screen", "Home-screen", R.drawable.home_screen)
        object Vedabase: BottomBarScreens("Vedabase", "vedabase", R.drawable.vedabase)
        object Profile: BottomBarScreens("Profile", "profile", R.drawable.krishna_store)
    }
}

val myDrawerScreens = listOf(
    Screens.DrawerScreens.SpecialMantras,
    Screens.DrawerScreens.ChantFlow,
    Screens.DrawerScreens.BhagwadGeetaNotes,
    Screens.DrawerScreens.MediaLibrary,
    Screens.DrawerScreens.VaishnavCalendar,
    Screens.DrawerScreens.Wallpapers,
    Screens.DrawerScreens.ContactUs,
    Screens.DrawerScreens.Donate,
    Screens.DrawerScreens.Share,
    Screens.DrawerScreens.AboutUs
)

val myBottomScreens = listOf(
    Screens.BottomBarScreens.Discussion,
    Screens.BottomBarScreens.SearchUsers,
    Screens.BottomBarScreens.HomeScreen,
    Screens.BottomBarScreens.Vedabase,
    Screens.BottomBarScreens.Profile
)