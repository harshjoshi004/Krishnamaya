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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RegisterInterface(authViewModel: AuthViewModel, navigate:()->Unit){
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var bioError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val progressDialog = myLoaderDialogue(context)

    //Select Image From Gallery Setup
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val permissionToGrant =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            android.Manifest.permission.READ_MEDIA_IMAGES
        }else{
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
    val imageInputLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri?->
        imageUri = uri
    }
    val permissionThenImageInputLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean->
        if(isGranted){
            Log.d("Permission", "Register: Permission Granted")
            Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show()
            imageInputLauncher.launch("image/*")
        }else{
            Log.d("Permission", "Register: Permission Denied")
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
    val totalImageInputFun: ()->Unit = {
        val havePermission = ContextCompat.checkSelfPermission(context, permissionToGrant) == PackageManager.PERMISSION_GRANTED
        if(havePermission){
            imageInputLauncher.launch("image/*")
        }else{
            permissionThenImageInputLauncher.launch(permissionToGrant)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(5))
            .animateContentSize()
            .background(Color(0xDAFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Heading(text = "Create Account",
            color = ElevatedMustard2,
            modifier = Modifier
                .fillMaxWidth()
                .background(ElevatedMustard1)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Image
        Image(
            painter =
            if(imageUri == null)
                painterResource(id = R.drawable.defimg)
            else
                rememberAsyncImagePainter(imageUri),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
                .clickable { totalImageInputFun() },
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.animateContentSize().padding(horizontal = 8.dp)) {
            // Enter Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = if (it.isNotBlank()) "" else "Name cannot be empty"
                },
                label = { Text("Name") },
                isError = nameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (nameError.isNotEmpty()) {
                Text(
                    nameError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.animateContentSize().padding(horizontal = 8.dp)) {
            // Enter Bio
            OutlinedTextField(
                value = bio,
                onValueChange = {
                    bio = it
                    bioError = if (it.isNotBlank()) "" else "Bio cannot be empty"
                },
                label = { Text("Bio") },
                isError = bioError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (bioError.isNotEmpty()) {
                Text(
                    bioError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

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
            text = "Register",
            enabled = emailError.isEmpty() && passwordError.isEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                if (isDetailValid(
                        name = name,
                        bio = bio,
                        email = email,
                        password = password,
                        context = context,
                        passwordError = passwordError,
                        emailError = emailError,
                        bioError = bioError,
                        nameError = nameError,
                        imageUri = imageUri )
                ) {
                    progressDialog.setMessage("Registering.. ")
                    progressDialog.show()

                    authViewModel.createNewAccount(
                        email = email,
                        password = password,
                        userName = name,
                        bio = bio,
                        photoUrl = imageUri!!,
                        context = context,
                        onSuccessFun = {
                            progressDialog.dismiss()
                            myToast(context, "Registered Successfully")
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