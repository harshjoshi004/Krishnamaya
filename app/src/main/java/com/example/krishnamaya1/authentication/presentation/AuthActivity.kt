package com.example.krishnamaya1.authentication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.krishnamaya1.MainActivity
import com.example.krishnamaya1.R
import com.example.krishnamaya1.ui.theme.Krishnamaya1Theme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fun wrapUp(){
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
            println("Intent fired to mainactivity")
            finish()
        }
        setContent {
            Krishnamaya1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val authNavCon = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()

                    if(authViewModel.getCurrentUser()!=null) wrapUp()

                    NavHost(navController = authNavCon, startDestination = "login"){
                        composable("login"){
                            LoginUI(authViewModel = authViewModel, navHostController = authNavCon){
                                wrapUp()
                            }
                        }
                        composable("register"){
                            RegisterUI(authViewModel = authViewModel, navHostController = authNavCon){
                                wrapUp()
                            }
                        }
                    }
                }
            }
        }
    }
}