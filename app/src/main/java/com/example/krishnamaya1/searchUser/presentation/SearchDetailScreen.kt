package com.example.krishnamaya1.searchUser.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.myToast

@Composable
fun SearchDetailScreen(uid: String?) {
    val context = LocalContext.current
    val searchUsersViewModel:SearchUsersViewModel = viewModel()

    var isLoading by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf<KrishnamayaUser?>(null) }

    LaunchedEffect(key1 = user) {
        if(user==null) {
            isLoading = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        searchUsersViewModel.fetchUsers { message->
            myToast(context, message)
        }
    }

    if(isLoading) LoadingScreen()
}