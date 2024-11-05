package com.example.krishnamaya1.searchUser.presentation

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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextAlign
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
fun UserBannerForSearch(user: KrishnamayaUser) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        val profilePicUri = user.imageLink
        val userNameText = user.userName
        val statusText = user.bio
        val emailText = user.email

        val (pfp, name, status, mail) = createRefs()

        //name
        Heading(text = userNameText,
            textAlign = TextAlign.Start,
            color = ElevatedMustard2,
            modifier = Modifier
                .widthIn(100.dp,160.dp)
                .constrainAs(name) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })

        //mail
        BoldSubheading(text = emailText,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .widthIn(100.dp,160.dp)
                .constrainAs(mail) {
                    start.linkTo(parent.start)
                    top.linkTo(name.bottom)
                })

        //status
        Subheading(text = statusText,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .widthIn(100.dp,160.dp)
                .padding(top = 8.dp)
                .constrainAs(status) {
                    start.linkTo(parent.start)
                    top.linkTo(mail.bottom)
                })

        //pfp
        Image(
            modifier = Modifier
                .size(140.dp)
                .constrainAs(pfp) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clip(CircleShape)
                .background(ElevatedMustard1),
            painter = rememberAsyncImagePainter(
                model = profilePicUri,
                contentScale = ContentScale.Crop
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}