package com.example.krishnamaya1.addPost.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.krishnamaya1.addPost.data.KrishnamayaPost
import com.example.krishnamaya1.authentication.domain.handleFirebaseAuthException
import com.example.krishnamaya1.authentication.presentation.AuthSharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import java.util.UUID

class AddPostViewModel:ViewModel() {
    private val storageRef = Firebase.storage.reference
    private val databaseRef = Firebase.database.reference

    //store image function
    fun makePost(postText:String, postImageUrl:Uri?, context:Context, onSuccessFun:()->Unit, onFailureFun:(String)->Unit){
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId->
            try {
                if (postImageUrl == null) {
                    postWithoutImage(userId, postText, context, onSuccessFun)
                } else {
                    postWithImage(userId, postText, postImageUrl, context, onSuccessFun)
                }
            } catch (e:Exception) {
                val message = handleFirebaseAuthException(e)
                Log.d("AddPostViewModel/makePost", "makePost message: $message, makePost error: ${e.message}")
                onFailureFun(message)
            }
        }
    }

    //make post with image
    private fun postWithImage(uid:String, postText:String, postImageUrl:Uri, context:Context, onSuccessFun:()->Unit){
        val uniqueImageLocation = storageRef.child("posts/${UUID.randomUUID()}.jpg")
        uniqueImageLocation.putFile(postImageUrl)
            .addOnSuccessListener { snapshot->
                Log.d("AddPostViewModel/postWithImage", "Image Uploaded Successfully!")

                uniqueImageLocation.downloadUrl
                    .addOnSuccessListener { imageFirebaseLink ->
                        Log.d("AddPostViewModel/postWithImage", "Image Link Retrieved - $imageFirebaseLink")

                        val postData = KrishnamayaPost(
                            postText = postText,
                            userId = uid,
                            timeStamp = System.currentTimeMillis().toString(),
                            postImage = imageFirebaseLink.toString()
                        )

                        // Generate a unique key for the new post in the database
                        val postKey = databaseRef.child("posts").push().key
                        Log.d("AddPostViewModel/postWithImage", "Post Key: $postKey")

                        postKey?.let { key ->
                            databaseRef.child("posts").child(key).setValue(postData)
                                .addOnSuccessListener {
                                    Log.d("AddPostViewModel/postWithImage", "Post Data Saved Successfully!")

                                    onSuccessFun()
                                }
                                .addOnFailureListener { databaseException -> throw databaseException } }
                    }
                    .addOnFailureListener { imageLinkDownloadException -> throw imageLinkDownloadException }
            }
            .addOnFailureListener { imageUploadException -> throw imageUploadException }
    }

    //make post without image
    private fun postWithoutImage(uid: String, postText:String, context:Context, onSuccessFun:()->Unit){
        val postData = KrishnamayaPost(
            postText = postText,
            userId = uid,
            timeStamp = System.currentTimeMillis().toString(),
            postImage = null
        )

        // Generate a unique key for the new post in the database
        val postKey = databaseRef.child("posts").push().key
        Log.d("AddPostViewModel/postWithoutImage", "Post Key: $postKey")

        postKey?.let { key ->
            databaseRef.child("posts").child(key).setValue(postData)
                .addOnSuccessListener {
                    Log.d("AddPostViewModel/postWithoutImage", "Post Data Saved Successfully!")

                    onSuccessFun()
                }
                .addOnFailureListener { databaseException -> throw databaseException }
        }
    }
}