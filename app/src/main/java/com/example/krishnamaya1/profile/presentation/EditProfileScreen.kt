package com.example.krishnamaya1.profile.presentation

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
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
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.CommonButton
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.LoadingScreen
import com.example.krishnamaya1.R
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.domain.isDetailValid
import com.example.krishnamaya1.authentication.domain.isValidEmail
import com.example.krishnamaya1.authentication.presentation.AuthViewModel
import com.example.krishnamaya1.discussions.presentation.DiscussionViewModel
import com.example.krishnamaya1.myLoaderDialogue
import com.example.krishnamaya1.myToast
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EditProfileScreen(curUserId: String?, navigate:()->Unit) {
    val context = LocalContext.current
    val editUserViewModel = viewModel<EditUserViewModel>()
    val discussionViewModel = viewModel<DiscussionViewModel>()
    var isLoading by remember { mutableStateOf(true) }
    var oldUser by remember { mutableStateOf<KrishnamayaUser>(KrishnamayaUser()) }
    var name by remember { mutableStateOf(oldUser.userName) }
    var bio by remember { mutableStateOf(oldUser.bio) }
    var imageLink by remember { mutableStateOf<String>(oldUser.imageLink) }
    var nameError by remember { mutableStateOf("") }
    var bioError by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val progressDialog = myLoaderDialogue(context)

    //Get User Data
    LaunchedEffect(key1 = Unit) {
        isLoading = true
        if(curUserId == null) {
            myToast(context, "Something went wrong!")
            navigate()
        } else {
            Log.d("important-harsh", "EditProfile: $curUserId")
            editUserViewModel.getUserdata(
                userId = curUserId,
                onSuccess = {
                    oldUser = it
                    name = it.userName
                    bio = it.bio
                    imageLink = it.imageLink
                    isLoading = false
                },
                onFailure = { myToast(context, it) }
            )
        }
    }

    val onButtonClick = { if(isDetailValid(name, bio, imageUri, context, nameError, bioError)) {
        progressDialog.setMessage("Saving.. ")
        progressDialog.show()
        if(imageUri == null) {
            editUserViewModel.editUser(
                oldUserId = curUserId!!,
                newName = name,
                newBio = bio,
                onSuccess = {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Edited Successfully", Toast.LENGTH_SHORT).show()
                    navigate()
                },
                onFailure = { it ->
                    progressDialog.dismiss()
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    navigate()
                }
            )
        } else {
            editUserViewModel.editUser(
                oldUserId = curUserId!!,
                newName = name,
                newBio = bio,
                newImageUri = imageUri.toString(),
                onSuccess = {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Edited Successfully", Toast.LENGTH_SHORT).show()
                    navigate()
                },
                onFailure = { it ->
                    progressDialog.dismiss()
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    navigate()
                }
            )
        }
    }}

    //Select Image From Gallery Setup
    val permissionToGrant =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
    val imageInputLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri?->
            imageUri = uri
        }
    val permissionThenImageInputLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean->
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

//User Interface
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(5))
            .animateContentSize()
            .background(Color(0xDAFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ElevatedMustard1),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigate() }) {
                Icon(tint = ElevatedMustard2,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow",
                    modifier = Modifier.padding(8.dp)
                )
            }
            BoldSubheading(
                text = "Edit Profile",
                color = ElevatedMustard2,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Image
        Image(painter =
            if(imageUri == null)
                if(imageLink == null)
                    painterResource(id = R.drawable.defimg)
                else
                    rememberAsyncImagePainter(imageLink)
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

        Column(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 8.dp)
        ) {
            // Enter Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = if (it.isNotBlank())
                        ""
                    else
                        "Name cannot be empty"
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

        Column(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 8.dp)) {
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

        CommonButton(text = "Confirm Changes",
            enabled = nameError.isBlank() && bioError.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onButtonClick
        )
    }

    if(isLoading) LoadingScreen()
}