package com.example.krishnamaya1

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.krishnamaya1.addPost.presentation.AddPostUI
import com.example.krishnamaya1.homeFeed.presentation.HomeScreenUI
import com.example.krishnamaya1.profile.presentation.ProfileScreenUI
import com.example.krishnamaya1.searchUser.presentation.SearchDetailScreen
import com.example.krishnamaya1.searchUser.presentation.SearchUsersScreen

@Composable
fun NavigationManager(
    navController: NavController,
    viewModel: MainViewModel,
    padval: PaddingValues,
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screens.BottomBarScreens.HomeScreen.bRoute,
        modifier = Modifier.padding(padval)
    ){
        // Bottom Bar Screens
        composable(Screens.BottomBarScreens.HomeScreen.bRoute){
            viewModel.setCurrentScreen(Screens.BottomBarScreens.HomeScreen)
            HomeScreenUI(navController, viewModel)
        }
        composable(Screens.BottomBarScreens.Profile.bRoute){
            viewModel.setCurrentScreen(Screens.BottomBarScreens.Profile)
            ProfileScreenUI(navController,viewModel)
        }
        composable(Screens.BottomBarScreens.Vedabase.bRoute){
            viewModel.setCurrentScreen(Screens.BottomBarScreens.Vedabase)
            VedabaseScreenUI(viewModel)
        }
        composable(Screens.BottomBarScreens.SearchUsers.bRoute){
            viewModel.setCurrentScreen(Screens.BottomBarScreens.SearchUsers)
            SearchUsersScreen(navController)
        }
        composable(Screens.BottomBarScreens.Discussion.bRoute){
            viewModel.setCurrentScreen(Screens.BottomBarScreens.Discussion)
            DiscussionScreenUI(viewModel)
        }

        // Nav Drawer Screens
        composable(Screens.DrawerScreens.SpecialMantras.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.SpecialMantras)
            SpecialMantrasScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.ChantFlow.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.ChantFlow)
            ChantFlowScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.BhagwadGeetaNotes.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.BhagwadGeetaNotes)
            BhagwadGeetaNotesScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.MediaLibrary.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.MediaLibrary)
            MediaLibraryScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.VaishnavCalendar.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.VaishnavCalendar)
            VaishnavCalendarScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.Wallpapers.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.Wallpapers)
            WallpapersScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.ContactUs.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.ContactUs)
            ContactUsScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.Donate.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.Donate)
            DonateScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.Share.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.Share)
            ShareScreenUI(viewModel)
        }
        composable(Screens.DrawerScreens.AboutUs.dRoute){
            viewModel.setCurrentScreen(Screens.DrawerScreens.AboutUs)
            AboutUsScreenUI(viewModel)
        }

        // secondary screens
        composable("add-post"){
            AddPostUI(navController = navController)
        }
        composable("search-detail/{uid}"){ backStack->
            val uid = backStack.arguments?.getString("uid")
            SearchDetailScreen(uid = uid)
        }
    }
}

@Composable
fun BhagwadGeetaNotesScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun ChantFlowScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun MediaLibraryScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun VaishnavCalendarScreenUI(viewModel: MainViewModel) {
    Text(viewModel.currentScreen.value.title)
}

@Composable
fun WallpapersScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun ContactUsScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun DonateScreenUI(viewModel: MainViewModel) {
    Text(viewModel.currentScreen.value.title)
}

@Composable
fun ShareScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun AboutUsScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun SpecialMantrasScreenUI(viewModel: MainViewModel) {
    Text(viewModel.currentScreen.value.title)
}

@Composable
fun DiscussionScreenUI(viewModel: MainViewModel) {
    Text(text = viewModel.currentScreen.value.title)
}

@Composable
fun VedabaseScreenUI(viewModel: MainViewModel) {
    Text(viewModel.currentScreen.value.title)
}