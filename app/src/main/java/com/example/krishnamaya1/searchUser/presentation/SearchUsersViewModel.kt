package com.example.krishnamaya1.searchUser.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchUsersViewModel:ViewModel() {
    private val usersRef = FirebaseDatabase.getInstance().reference.child("users")

    private val _usersLiveData = MutableLiveData<List<KrishnamayaUser>?>(null)
    val userLiveData:MutableLiveData<List<KrishnamayaUser>?> = _usersLiveData

    // fetch user data
    fun fetchUsers(onFailure:(String)->Unit){
        val result = mutableListOf<KrishnamayaUser>()
        try{
            usersRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SearchUsersViewModel/fetchUsers", "Data changed $snapshot")

                    for(dataSnapshot in snapshot.children){
                        val user = dataSnapshot.getValue(KrishnamayaUser::class.java)
                        user?.let {
                            result.add(0,it)
                        }
                        if(result.size == snapshot.childrenCount.toInt()) _usersLiveData.postValue(result)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw Exception(error.message)
                }
            })
        } catch(e:Exception) {
            Log.d("SearchUsersViewModel/fetchUsers", e.message ?: "Something went wrong")
            onFailure(e.message ?: "Something went wrong")
        }
    }

    fun extractUserData(userId:String, onSuccess:(KrishnamayaUser)->Unit, onFailure: (String) -> Unit) {
        usersRef.child(userId).get()
            .addOnSuccessListener { krishnamayaUser->
                val user = krishnamayaUser.getValue(KrishnamayaUser::class.java)
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { e ->
                Log.d("SearchUsersViewModel/extractUserData", e.message ?: "Something went wrong")
                onFailure(e.message?: "Something went wrong")
            }
    }
}