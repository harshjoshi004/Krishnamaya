package com.example.krishnamaya1.homeFeed.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.krishnamaya1.addPost.data.KrishnamayaPost
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.domain.handleFirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostsFeedViewModel: ViewModel() {
    private val postsRef = FirebaseDatabase.getInstance().getReference("posts")
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")

    private val _livePostsAndUsers = MutableLiveData< List<Pair<KrishnamayaPost, KrishnamayaUser>>? >()
    val livePostsAndUsers: MutableLiveData< List<Pair<KrishnamayaPost, KrishnamayaUser>>? > = _livePostsAndUsers

    fun getPostsAndUsers(onFailure:(String)->Unit) {
        val result = mutableListOf<Pair<KrishnamayaPost, KrishnamayaUser>>()
        try {
            postsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("PostsFeedViewModel", "Data changed")

                    for (dataSnapshot in snapshot.children) {
                        val post = dataSnapshot.getValue(KrishnamayaPost::class.java)

                        post?.let { krishnamayaUser ->
                            extractUserData(krishnamayaUser.userId) { user ->
                                val entry = post to user
                                result.add(0, entry)

                                if(result.size == snapshot.childrenCount.toInt()){
                                    _livePostsAndUsers.postValue(result)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw Exception(error.message)
                }
            })
        } catch (e:Exception) {
            Log.e("PostsFeedViewModel", e.message ?: "Something went wrong")
            onFailure(e.message ?: "Something went wrong")
        }
    }

    private fun extractUserData(userId:String, onSuccess:(KrishnamayaUser)->Unit) {
        usersRef.child(userId).get()
            .addOnSuccessListener { krishnamayaUser->
                val user = krishnamayaUser.getValue(KrishnamayaUser::class.java)
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { e -> throw e }
    }
}