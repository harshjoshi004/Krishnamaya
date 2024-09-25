package com.example.krishnamaya1.authentication.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.snapshotFlow
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.domain.handleFirebaseAuthException
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel:ViewModel() {
    private val auth = Firebase.auth
    private val databaseRef = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private val _error = MutableLiveData<String?>()
    private val _firebaseCurrentUser = MutableLiveData<FirebaseUser?>()

    val error: MutableLiveData<String?> = _error
    val firebaseCurrentUser: MutableLiveData<FirebaseUser?> = _firebaseCurrentUser

    //get current user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    //logout
    fun logout(context:Context, afterFun:()->Unit) {
        auth.signOut()
        AuthSharedPreferences.clearData(context)
        afterFun()
        _firebaseCurrentUser.postValue(null)
        _error.postValue(null)
    }

    //create a new account
    fun createNewAccount(context: Context, email:String, password:String, userName:String,
                         bio:String, photoUrl:Uri, onSuccessFun:()->Unit, onErrorFun:(String)->Unit
    ){
        //setting unique image name for this registration attempt
        val uniqueImageId = UUID.randomUUID().toString()
        val imageRef = storageRef.child("users/$uniqueImageId.jpg")

        try {
            //create account
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult->
                    val userId = authResult.user?.uid
                    Log.d("AuthViewModel/CreateAccount", "Account created Successfully! id = $userId")
                    //updated live data
                    _firebaseCurrentUser.postValue(authResult.user)
                    _error.postValue(null)

                    //if account created, save profile image to cloud
                    userId?.let { uid ->
                        imageRef.putFile(photoUrl)
                            .addOnSuccessListener {
                                Log.d("AuthViewModel/CreateAccount", "Profile Photo Saved Successfully!")

                                //get link of saved photo refer to in future and store in userData
                                imageRef.downloadUrl
                                    .addOnSuccessListener { imageLink->
                                        Log.d("AuthViewModel/CreateAccount", "Profile Photo Link Retrieved")

                                        val userData = KrishnamayaUser(
                                            email = email,
                                            userName = userName,
                                            bio = bio,
                                            userId = uid,
                                            imageLink = imageLink.toString()
                                        )

                                        //save userData to database
                                        databaseRef.child("users").child(uid).setValue(userData)
                                            .addOnSuccessListener {
                                                Log.d("AuthViewModel/CreateAccount", "User Data Saved Successfully!")

                                                //save userData to SharedPreferences
                                                AuthSharedPreferences.storeData(
                                                    context = context,
                                                    userName = userName,
                                                    userId = uid,
                                                    bio = bio,
                                                    photoUri = imageLink.toString()
                                                )

                                                //perform desired action
                                                onSuccessFun()
                                            }
                                            .addOnFailureListener { databaseError -> throw databaseError }
                                    }
                                    .addOnFailureListener { imageDownloadError -> throw imageDownloadError }
                            }
                            .addOnFailureListener { storageError -> throw storageError }
                    }
                }
                .addOnFailureListener { authError -> throw authError }
        }
        catch (e:Exception) {
            val message = handleFirebaseAuthException(e)
            Log.d("AuthViewModel/CreateAccount", "Create Account Failed with Exception: ${e.message}")

            onErrorFun(message)
            _error.postValue(message)
        }
    }

    //login
    fun loginUser(context:Context, email:String, password: String, onSuccessFun:()->Unit, onErrorFun:(String)->Unit){
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener { authResult->
                    Log.d("AuthViewModel/LogIn", "User logged in successfully!")
                    //updated live data
                    _firebaseCurrentUser.postValue(authResult.user)
                    _error.postValue(null)

                    //storing the login data in SharedPreferences
                    val userId = authResult.user?.uid
                    userId?.let { uid->
                        databaseRef.child("users").child(uid).get()
                            .addOnSuccessListener { snapshot->
                                Log.d("AuthViewModel/LogIn", "User data downloaded and saved offline successfully!")

                                val data = snapshot.getValue(KrishnamayaUser::class.java)
                                AuthSharedPreferences.storeData(
                                    context = context,
                                    userName = data?.userName ?: "",
                                    userId = uid,
                                    bio = data?.bio ?: "",
                                    photoUri = data?.imageLink ?: ""
                                )
                            }
                            .addOnFailureListener { downloadException-> throw downloadException }
                    }

                    onSuccessFun()
                }
                .addOnFailureListener { loginException-> throw loginException }
        } catch (e:Exception) {
            val message = handleFirebaseAuthException(e)
            Log.d("AuthViewModel/LogIn", "Login Failed with exception: ${e.message}")
            onErrorFun(message)
            _error.postValue(message)
        }
    }

    //get user data
    fun getCurrentUserData(onSuccess: (KrishnamayaUser?)->Unit, onFailure: (String)->Unit) {
        val currentUser = auth.currentUser
        if(currentUser != null) {
            val userId = currentUser.uid
            databaseRef.child("users").child(userId).get()
                .addOnSuccessListener { snapshot->
                    val data = snapshot.getValue(KrishnamayaUser::class.java)
                    onSuccess(data)
                }
                .addOnFailureListener { e->
                    onFailure(e.message ?: "Unknown Error")
                    _error.postValue(handleFirebaseAuthException(e))
                }
        }
    }
}