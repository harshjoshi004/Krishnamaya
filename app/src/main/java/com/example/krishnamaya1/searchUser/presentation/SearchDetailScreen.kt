package com.example.krishnamaya1.searchUser.presentation

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.presentation.AuthActivity
import com.example.krishnamaya1.authentication.presentation.AuthViewModel
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.discussions.presentation.DiscussionCard
import com.example.krishnamaya1.discussions.presentation.DiscussionViewModel
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.profile.presentation.UserBanner
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SearchDetailScreen(uid: String?, navigate: ()->Unit) {
    val discussionViewModel = viewModel<DiscussionViewModel>()
    val context = LocalContext.current

    var listState = remember {
        mutableStateOf<List<Pair<Discussion, KrishnamayaUser>>?>(null)
    }
    var curUser by remember { mutableStateOf<KrishnamayaUser?>(null) }
    var loading by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        loading = true
        Log.d("LaunchedEffect", "User Data Changed")
        uid?.let { userId->
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
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ElevatedMustard2),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigate() }) {
                        Icon(tint = ElevatedMustard1,
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    BoldSubheading(
                        text = "User Details",
                        color = ElevatedMustard1,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } //secondary top bar

            curUser?.let {
                item {
                    UserBannerForSearch (user = it)
                }
            }
            item {
                androidx.compose.material.Divider(color = ElevatedMustard1)
            }
            item {
                BoldSubheading(
                    text = "${curUser?.userName}'s Activity: ",
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