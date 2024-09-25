package com.example.krishnamaya1.profile.presentation

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.MainActivity
import com.example.krishnamaya1.MainViewModel
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.presentation.AuthActivity
import com.example.krishnamaya1.authentication.presentation.AuthViewModel
import com.example.krishnamaya1.myBasicDialogue
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.ui.theme.BackgroundMustard

@Composable
fun ProfileScreenUI(navController: NavController, viewModel: MainViewModel) {
    val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<AuthViewModel>()
    val context = LocalContext.current
    val firebaseUser by authViewModel.firebaseCurrentUser.observeAsState()
    val firebaseError by authViewModel.error.observeAsState()
    var userName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf("") }
    var loading by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        loading = true
        Log.d("LaunchedEffect", "User Data Changed")
        authViewModel.getCurrentUserData(
            onSuccess = { user ->
                Log.d("ProfileScreenUI", "User Data: $user")
                userName = user?.userName ?: ""
                bio = user?.bio ?: ""
                email = user?.email ?: ""
                photoUri = user?.imageLink ?: ""
                loading = false
            },
            onFailure = { message -> Log.d("ProfileScreenUI", "Error Occurred: $message") }
        )
    }

    Column {
        Image(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            painter = rememberAsyncImagePainter(model = photoUri, contentScale = ContentScale.FillBounds),
            contentDescription = null
        )

        Text(text = userName)

        Text(text = bio)

        Text(text = email)

        Button(onClick = {
            authViewModel.logout( context, afterFun = {
                Log.d("ProfileScreenUI", "User Logged Out")
                myToast(context, "User Logged Out")
            } )
            val activity = context as? Activity
            activity?.let { act->
                act.startActivity(Intent(act, AuthActivity::class.java))
                act.finish()
            }
        }) {
            Text(text = "Logout")
        }
    }

    if(loading) LoadingScreen()
}