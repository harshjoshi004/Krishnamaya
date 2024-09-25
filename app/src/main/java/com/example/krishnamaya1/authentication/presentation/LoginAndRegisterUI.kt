package com.example.krishnamaya1.authentication.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.MainViewModel
import com.example.krishnamaya1.Subheading

@Composable
fun RegisterUI(
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    wrapUp:()->Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .paint(
                painterResource(id = com.example.krishnamaya1.R.drawable.bg2),
                contentScale = ContentScale.Crop
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            RegisterInterface(authViewModel){ wrapUp() }
        }
        item{
            Subheading("Already have an account? Log-In",
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp).clickable {
                    navHostController.navigate("login"){
                        popUpTo(navHostController.graph.startDestinationId){inclusive = true}
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun LoginUI(
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    wrapUp:()->Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .paint(
                painterResource(id = com.example.krishnamaya1.R.drawable.bg1),
                contentScale = ContentScale.Crop
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        item{
            Heading(text = "Krishnamay", color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
        }
        item{
            Subheading(text = "Krishnamay helps you connect and share with devotees in your in your life",
                color = Color.White, modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 8.dp)
            )
        }
        item {
            LoginInterface(authViewModel){ wrapUp() }
        }
        item{
            Subheading("Don't have an account? Sign Up",
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp).clickable {
                    navHostController.navigate("register"){
                        popUpTo(navHostController.graph.startDestinationId){inclusive = true}
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginUI(
        authViewModel = AuthViewModel(),
        navHostController = rememberNavController()
    )
}
@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterUI(
        authViewModel = AuthViewModel(),
        navHostController = rememberNavController()
    )
}