package com.example.krishnamaya1.discussions.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.krishnamaya1.ui.theme.ElevatedMustard1

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiscussionFeed() {
    val context = LocalContext.current
    val discussionViewModel: DiscussionViewModel = viewModel()

    val listState = discussionViewModel.liveDiscussionsData.observeAsState(null)
    //Pull Refresh Implementation
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            // Simulate network delay
            discussionViewModel.getDiscussions {
                isRefreshing = false
            }
        }
    )
    LaunchedEffect(Unit) {
        isRefreshing = true
        discussionViewModel.getDiscussions {
            isRefreshing = false
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        item {
            AddingDiscussion(discussionViewModel)
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
        }
    }

    // PullRefreshIndicator shows an animated indicator during refresh
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState
        )
    }
}