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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.discussions.presentation.DiscussionCard
import com.example.krishnamaya1.discussions.presentation.DiscussionViewModel
import com.example.krishnamaya1.myBasicDialogue
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreenUI(navController: NavController, viewModel: MainViewModel) {
    val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<AuthViewModel>()
    val discussionViewModel = androidx.lifecycle.viewmodel.compose.viewModel<DiscussionViewModel>()
    val context = LocalContext.current
    var listState = remember {
        mutableStateOf<List<Pair<Discussion, KrishnamayaUser>>?>(null)
    }

    var curUser by remember { mutableStateOf<KrishnamayaUser?>(null) }
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
    val onClickEdit = {
        Log.d("important-harsh", "Profile: ${curUser?.userId}")
        navController.navigate("edit-user/${curUser?.userId}")
    }

    LaunchedEffect(Unit) {
        loading = true
        Log.d("LaunchedEffect", "User Data Changed")
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            discussionViewModel.getUserFromId(uid) { user ->
                curUser = user
                Log.d("priyansh", "ProfileScreenUI: ${user.userId} ")
                discussionViewModel.getDiscussions(user) { it->
                    listState.value = it
                    loading = false
                }
            }
        }
    }

    if(loading){
        LoadingScreen()
    } else {
        LazyColumn {
            curUser?.let {
                item {
                    UserBanner(
                        user = it,
                        onClickLogout = { onClickLogOut() },
                        onClickEdit = { onClickEdit() }
                    )
                }
            }
            item {
                androidx.compose.material.Divider(color = ElevatedMustard1)
            }
            item {
                BoldSubheading(
                    text = "My Activity: ",
                    color = ElevatedMustard2,
                    modifier = Modifier.padding(8.dp)
                )
            }
            item {
                androidx.compose.material.Divider(color = ElevatedMustard1)
            }
            listState.value?.let { list ->
                for ((dis, user) in list) {
                    item {
                        DiscussionCard(discussionViewModel, user, dis)
                    }
                    item {
                        androidx.compose.material.Divider(color = ElevatedMustard1)
                    }
                }
                if(list.isEmpty()) {
                    item {
                        Text("No activity by ${curUser?.userName}..",
                            modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                }
            }
        }
    }
}