package com.example.krishnamaya1.homeFeed.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.MainViewModel
import com.example.krishnamaya1.addPost.data.KrishnamayaPost
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun HomeScreenUI(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val postsFeedViewModel: PostsFeedViewModel = viewModel()

    val postsAndUsers = postsFeedViewModel.livePostsAndUsers.observeAsState(null)
    var isLoading by remember { mutableStateOf(true) }

    //add post logic
    val onFloatingActionButtonClick = {navController.navigate("add-post")}

    LaunchedEffect(key1 = postsAndUsers.value) {
        if(postsAndUsers.value != null){
            isLoading = false
        } else {
            isLoading = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        postsFeedViewModel.getPostsAndUsers { message -> Toast.makeText(context, message , Toast.LENGTH_SHORT).show() }
    }

    Scaffold (
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onFloatingActionButtonClick() },
                icon = { Icon(Icons.Default.Add, "Extended floating action button.") },
                text = { BoldSubheading(text = " Add ", color = ElevatedMustard2) },
                containerColor = ElevatedMustard1,
                contentColor = ElevatedMustard2
            )
        }
    ){ padVal->
        LazyColumn (
            modifier = Modifier
                .background(BackgroundMustard)
                .padding(padVal)
                .fillMaxSize(),
        ){
            postsAndUsers.value?.let { list ->
                items(list){ (post, user) ->
                    println("Harsh: post = $post, user =  $user")
                    Column {
                        KrishnamayaPostCard(post, user.userName, user.imageLink)
                        Divider(color = ElevatedMustard1)
                    }
                }
            }
        }
    }

    if (isLoading) LoadingScreen()
}