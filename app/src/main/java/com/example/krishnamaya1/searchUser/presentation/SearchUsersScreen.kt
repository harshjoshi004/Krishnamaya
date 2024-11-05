package com.example.krishnamaya1.searchUser.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.Paragraph
import com.example.krishnamaya1.Subheading
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun SearchUsersScreen(navController: NavHostController) {
    val context = LocalContext.current
    val searchUsersViewModel:SearchUsersViewModel = viewModel()
    val usersListState = searchUsersViewModel.userLiveData.observeAsState()
    var isLoading by remember { mutableStateOf(true) }
    var searchStr by remember { mutableStateOf("") }
    val consistentPadding = 8.dp

    val userDetailNavLogic: (KrishnamayaUser) -> Unit = { user ->
        navController.navigate("search-detail/${user.userId}")
    }

    LaunchedEffect(key1 = usersListState.value) {
        isLoading = usersListState.value == null
    }

    LaunchedEffect(key1 = Unit) {
        searchUsersViewModel.fetchUsers{ message ->
            Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(modifier = Modifier.background(BackgroundMustard).fillMaxSize()
    ) {

        //search bar
        item {
            OutlinedTextField(value = searchStr, onValueChange = {searchStr = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(consistentPadding),
                label = { Text(text = "Search Users", color = ElevatedMustard2) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ElevatedMustard2,
                    unfocusedBorderColor = ElevatedMustard1,
                    cursorColor = ElevatedMustard2,
                    textColor = ElevatedMustard2,
                    leadingIconColor = ElevatedMustard2,
                    unfocusedLabelColor = ElevatedMustard2,
                    focusedLabelColor = ElevatedMustard2,
                )
            )
        }

        //rest of content
        usersListState.value?.let { list->
            items(list.filter { it->
                it.userName.contains(searchStr) || it.email.contains(searchStr) || it.bio.contains(searchStr)
            }) { user->
                SearchUsersCard(user = user, onClick = userDetailNavLogic)
            }
        }

    }

    if(isLoading) LoadingScreen()
}