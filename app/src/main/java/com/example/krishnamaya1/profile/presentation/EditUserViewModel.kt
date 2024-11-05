package com.example.krishnamaya1.profile.presentation

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.discussions.data.Reply
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditUserViewModel: ViewModel() {
    val userRef = FirebaseDatabase.getInstance().getReference("users")

    fun getUserdata(
        userId: String,
        onSuccess:(KrishnamayaUser)->Unit,
        onFailure:(String)->Unit
    ) {
        try {
            userRef.child(userId).get()
                .addOnSuccessListener { snap ->
                    Log.d("important-harsh", "getUserdata: snap = $snap")
                    val user = snap.getValue(KrishnamayaUser::class.java)
                    Log.d("important-harsh", "getUserdata: user extracted = $user")
                    user?.let {
                        Log.d("important-harsh", "getUserdata: user extracted = $it")
                        onSuccess(it)
                    }
                }
                .addOnFailureListener { e -> throw e }
        } catch (e: Exception) {
            Log.d("EditUserViewModel", e.toString())
            onFailure(e.toString())
        }
    }
    fun editUser(
        oldUserId: String,
        newName:String,
        newBio:String,
        newImageUri:String,
        onSuccess:()->Unit,
        onFailure:(String)->Unit
    ) {
        try {
            val uniqueImageId = UUID.randomUUID().toString()
            val imageRef = FirebaseStorage.getInstance().getReference("users/$uniqueImageId.jpg")

            imageRef.putFile(newImageUri.toUri())
                .addOnSuccessListener {
                    imageRef.downloadUrl
                        .addOnSuccessListener { newLink ->
                            userRef.child(oldUserId).get()
                                .addOnSuccessListener { snap ->
                                    snap.getValue(KrishnamayaUser::class.java)?.let { oldUser ->
                                        val replace = oldUser.copy(
                                            userName = newName,
                                            bio = newBio,
                                            imageLink = newLink.toString()
                                        )

                                        userRef.child(oldUserId).setValue(replace)
                                            .addOnSuccessListener { onSuccess() }
                                            .addOnFailureListener { e -> throw e }
                                    }
                                }
                                .addOnFailureListener { e -> throw e }
                        }
                        .addOnFailureListener { e -> throw e }
                }
                .addOnFailureListener { e -> throw e }
        } catch (e: Exception) {
            Log.d("EditUserViewModel", e.toString())
            onFailure(e.toString())
        }
    }

    fun editUser(
        oldUserId: String,
        newName:String,
        newBio:String,
        onSuccess:()->Unit,
        onFailure:(String)->Unit
    ) {
        try {
            userRef.child(oldUserId).get()
                .addOnSuccessListener { snap ->
                    snap.getValue(KrishnamayaUser::class.java)?.let { oldUser ->
                        val replace = oldUser.copy(
                            userName = newName,
                            bio = newBio
                        )

                        userRef.child(oldUserId).setValue(replace)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e -> throw e }
                    }
                }
                .addOnFailureListener { e -> throw e }
        } catch (e: Exception) {
            Log.d("EditUserViewModel", e.toString())
            onFailure(e.toString())
        }
    }
}