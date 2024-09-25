package com.example.krishnamaya1

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _currentScreen = mutableStateOf<Screens>(Screens.BottomBarScreens.HomeScreen)
    val currentScreen: MutableState<Screens> = _currentScreen

    fun setCurrentScreen(screen: Screens){
        _currentScreen.value = screen
    }
}