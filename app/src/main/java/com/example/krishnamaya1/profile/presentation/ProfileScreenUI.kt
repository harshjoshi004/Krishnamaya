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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.MainActivity
import com.example.krishnamaya1.MainViewModel
import com.example.krishnamaya1.Subheading
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.presentation.AuthActivity
import com.example.krishnamaya1.authentication.presentation.AuthViewModel
import com.example.krishnamaya1.myBasicDialogue
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

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
    val onClickLogOut = {
        authViewModel.logout( context, afterFun = {
            Log.d("ProfileScreenUI", "User Logged Out")
            myToast(context, "User Logged Out")
        })

        val activity = context as? Activity

        activity?.let { act->
            act.startActivity(Intent(act, AuthActivity::class.java))
            act.finish()
        }
    }
    val onClickEdit = {}

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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        val (pfp, name, status, mail, logout, edit) = createRefs()

        //name
        Heading(text = userName, color = ElevatedMustard2,
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })

        //mail
        BoldSubheading(text = email,
            modifier = Modifier
                .constrainAs(mail) {
                    start.linkTo(parent.start)
                    top.linkTo(name.bottom)
                })

        //status
        Subheading(text = bio,
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(status) {
                    start.linkTo(parent.start)
                    top.linkTo(mail.bottom)
                })

        //pfp
        Image(
            modifier = Modifier
                .size(150.dp)
                .constrainAs(pfp) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clip(CircleShape),
            painter = rememberAsyncImagePainter(
                model = photoUri,
                contentScale = ContentScale.FillBounds
            ),
            contentDescription = null
        )

        //button
        Button(
            onClick = { onClickLogOut() },
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(logout) {
                    top.linkTo(status.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            BoldSubheading(text = "Logout", color = ElevatedMustard2)
        }

        //button2
        IconButton(
            onClick = { onClickEdit() },
            modifier = Modifier
                .constrainAs(edit) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clip(CircleShape)
                .background(ElevatedMustard1)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = ElevatedMustard2)
        }
    }

    if(loading) LoadingScreen()
}