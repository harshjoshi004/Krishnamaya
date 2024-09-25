package com.example.krishnamaya1

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.authentication.presentation.AuthViewModel
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(){
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedIndex = remember { mutableStateOf(2) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyAppDrawer(viewModel = viewModel, scope, drawerState)
        }
    ) {
        Scaffold(
            topBar = { MyAppBar(viewModel, scope, drawerState) },
            bottomBar = { MyAnimatedBottomBar(navController = navController, selectedIndex = selectedIndex, vm = viewModel) },
            drawerBackgroundColor = ElevatedMustard1,
            backgroundColor = BackgroundMustard
        ) { paddingValues->
            NavigationManager(
                navController = navController,
                viewModel = viewModel,
                padval = paddingValues
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(viewModel: MainViewModel, scope: CoroutineScope, drawerState: DrawerState){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = ElevatedMustard1,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black
        ),
        title = {
            Text(text = viewModel.currentScreen.value.title, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun MyAppDrawer(viewModel: MainViewModel, scope: CoroutineScope, drawerState: DrawerState){
    val width = 300.dp
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxHeight()
            .width(width)
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)),
        ) {
        Box(modifier = Modifier
            .width(width)
            .height(200.dp)
            .paint(painterResource(id = R.drawable.bg2), contentScale = ContentScale.FillWidth))
        {
            Row (
                modifier = Modifier.padding(16.dp).fillMaxWidth().align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Heading(text = "Krishnamaya", color = Color.White)
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.size(60.dp).clip(CircleShape))

            }
        }
        ModalDrawerSheet(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            drawerContainerColor = ElevatedMustard1,
            drawerShape = RectangleShape,
            drawerTonalElevation = 10.dp
        ) {
            for (it in myDrawerScreens) {
                NavigationDrawerItem(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    label = { Text(text = it.title, color = Color.Black) },
                    icon = {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = it.title
                        )
                    },
                    selected = viewModel.currentScreen.value.route == it.dRoute,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = BackgroundMustard
                    ),
                    onClick = {
                        viewModel.setCurrentScreen(it)
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MyAnimatedBottomBar(navController: NavController, selectedIndex: MutableState<Int>, vm: MainViewModel){
    AnimatedNavigationBar(
        modifier = Modifier
            .systemBarsPadding()
            .padding(8.dp),
        cornerRadius = shapeCornerRadius(50f),
        selectedIndex = selectedIndex.value,
        barColor = ElevatedMustard2,
        ballColor = ElevatedMustard2,
    ) {
        myBottomScreens.forEachIndexed { screenIndex, bottomBarScreen ->
            Box(modifier = Modifier
                .wrapContentSize()
                .noRippleClickable {
                    selectedIndex.value = screenIndex
                    vm.setCurrentScreen(bottomBarScreen)
                    navController.navigate(bottomBarScreen.bRoute){
                        popUpTo(navController.graph.id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(id = bottomBarScreen.icon),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                    tint = ElevatedMustard1
                )
            }
        }
    }
}

