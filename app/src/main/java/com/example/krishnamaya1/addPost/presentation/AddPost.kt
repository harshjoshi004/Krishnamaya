package com.example.krishnamaya1.addPost.presentation

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.R
import com.example.krishnamaya1.Subheading
import com.example.krishnamaya1.authentication.presentation.AuthSharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.krishnamaya1.Paragraph
import com.example.krishnamaya1.myLoaderDialogue
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddPostUI(navController: NavController) {
    val context = LocalContext.current
    var postText by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val addPostViewModel:AddPostViewModel = viewModel()
    val progressDialog = myLoaderDialogue(context = context)

    // post button click logic
    val buttonClick: ()->Unit  = {
        if (postText.isEmpty()) {
            Toast.makeText(context, "Fields are empty!", Toast.LENGTH_SHORT).show()
        } else {
            progressDialog.show()

            addPostViewModel.makePost(
                postText = postText,
                postImageUrl = imageUri,
                context = context,
                onSuccessFun = {
                    postText = ""
                    imageUri = null
                    Toast.makeText(context, "Successfully Posted!", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    navController.popBackStack()
                },
                onFailureFun = { str ->
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            )
        }
    }

    // select image from gallery setup
    val permissionToGrant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    val imageInputLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val permissionThenImageInputLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permission", "Register: Permission Granted")
                Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show()
                imageInputLauncher.launch("image/*")
            } else {
                Log.d("Permission", "Register: Permission Denied")
                Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    val totalImageInputFun: () -> Unit = {
        val havePermission = ContextCompat.checkSelfPermission(
            context,
            permissionToGrant
        ) == PackageManager.PERMISSION_GRANTED
        if (havePermission) {
            imageInputLauncher.launch("image/*")
        } else {
            permissionThenImageInputLauncher.launch(permissionToGrant)
        }
    }

    // entire ui
    LazyColumn(modifier = Modifier.fillMaxSize()) { item{
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val (topRow, profileRow, imageBox, attachMedia, editText, postButton) = createRefs()

            // pseudo nav bar thing
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(topRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        tint = ElevatedMustard2,
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                BoldSubheading(text = "Add Post", color = ElevatedMustard2)
            }

            //profile details row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(profileRow) {
                    top.linkTo(topRow.bottom)
                    start.linkTo(parent.start)
                },
            ) {
                val imageUrl = AuthSharedPreferences.getPhotoUri(context)
                val userName = AuthSharedPreferences.getUserName(context)
                val bio = AuthSharedPreferences.getBio(context)
                Image(
                    painter =
                        if (imageUrl == null)
                            painterResource(id = R.drawable.defimg)
                        else
                            rememberAsyncImagePainter(model = imageUrl),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(ElevatedMustard1),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Column {
                    // User Name
                    Text( fontWeight = FontWeight.Bold, fontSize = 20.sp,
                        text = userName ?: "User Name", color = ElevatedMustard2,
                    )
                    // Time Stamp
                    Text( fontWeight = FontWeight.Normal, fontSize = 14.sp,
                        text = bio ?: "Bio", color = ElevatedMustard2,
                    )
                }
            }

            //post text field
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(editText) {
                        top.linkTo(profileRow.bottom);
                        start.linkTo(parent.start)
                    }
            ) {
                if (postText.isEmpty()) {
                    Subheading(
                        text = "What's on your mind?",
                        color = ElevatedMustard2
                    )
                }
                BasicTextField(
                    value = postText,
                    onValueChange = { postText = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (imageUri != null) {
                //attached image
                Box(modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .constrainAs(imageBox) {
                        top.linkTo(editText.bottom)
                        start.linkTo(parent.start)
                    }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = imageUri,
                            contentScale = ContentScale.FillHeight
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .height(250.dp)
                            .align(Alignment.Center),
                        contentDescription = null
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd),
                        onClick = {
                            imageUri = null
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null
                        )
                    }

                }
            } else {
                //attach media
                IconButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(attachMedia) {
                            top.linkTo(editText.bottom)
                            start.linkTo(parent.start)
                        },
                    onClick = {
                        totalImageInputFun()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.attach_file),
                        contentDescription = null
                    )
                }
            }

            Button(onClick = { buttonClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(postButton) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                BoldSubheading(text = "Post")
            }
        }
    }}
}

@Composable
@Preview(showBackground = true)
fun AddPostUIPreview(){
    AddPostUI(navController = rememberNavController())
}