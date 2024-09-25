package com.example.krishnamaya1.authentication.presentation

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.CommonButton
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.R
import com.example.krishnamaya1.authentication.domain.isDetailValid
import com.example.krishnamaya1.authentication.domain.isValidEmail
import com.example.krishnamaya1.myLoaderDialogue
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun LoginInterface(authViewModel: AuthViewModel, navigate:()->Unit){
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val progressDialog = myLoaderDialogue(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(10))
            .animateContentSize()
            .background(Color(0xDAFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Heading(text = "Welcome Back",
            color = ElevatedMustard2,
            modifier = Modifier
                .fillMaxWidth()
                .background(ElevatedMustard1)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 8.dp)) {
            //Enter Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isValidEmail()) "" else "Invalid email format"
                },
                label = { Text("Email") },
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError.isNotEmpty()) {
                Text(
                    emailError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        //Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 8.dp)
        ) {
            //Enter Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError =
                        if (it.length >= 8) "" else "Password must be at least 8 characters"
                },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible) R.drawable.visible else R.drawable.invisible
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(painter = painterResource(id = image), contentDescription = null)
                    }
                },
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError.isNotEmpty()) {
                Text(
                    passwordError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CommonButton(
            text = "Login",
            enabled = emailError.isEmpty() && passwordError.isEmpty(),
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = {
                if(isDetailValid(password = password, email = email, passwordError = passwordError, emailError = emailError, context = context)){
                    progressDialog.setMessage("Logging In.. ")
                    progressDialog.show()

                    authViewModel.loginUser(context, email,password,
                        onSuccessFun = {
                            progressDialog.dismiss()
                            myToast(context, "Logged in successfully!")
                            navigate()
                        },
                        onErrorFun = { message->
                            progressDialog.dismiss()
                            myToast(context, message)
                        }
                    )
                }
            }
        )
    }
}