package com.example.krishnamaya1.discussions.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.discussions.data.Reply
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DiscussionViewModel: ViewModel() {
    private val discussionsRef = FirebaseDatabase.getInstance().getReference("discussions")
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    private val _liveDiscussionsData = MutableLiveData< List< Pair<Discussion, KrishnamayaUser> > >()
    val liveDiscussionsData:MutableLiveData<List<Pair<Discussion, KrishnamayaUser>>> = _liveDiscussionsData

    fun addReply(discussion: Discussion, replyText: String, onSuccess:()->Unit, onFailure:(String)->Unit) {
        try {
            discussionsRef.child(discussion.discussionId).child("listOfReplies").get()
                .addOnSuccessListener { snap ->
                    val listOfReplies = mutableListOf<Reply>()
                    snap.children.forEach { dataSnap ->
                        val reply = dataSnap.getValue(Reply::class.java)
                        reply?.let {
                            listOfReplies.add(it)
                        }
                    }
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    uid?.let { userId->
                        val reply = Reply(
                            text = replyText,
                            replyId = "replyId",
                            userId = userId,
                            timeStamp = System.currentTimeMillis().toString()
                        )
                        listOfReplies.add(reply)
                        discussionsRef.child(discussion.discussionId).child("listOfReplies")
                            .setValue(listOfReplies)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e-> throw e}
                    }
                }
                .addOnFailureListener{e->throw e}
        } catch (e:Exception) {
            Log.e("DiscussionViewModel", "addReply: ${e.message}")
            onFailure(e.message.toString())
        }
    }

    fun addReply2(discussion: Discussion, replyText: String, onSuccess:()->Unit, onFailure:(String)->Unit) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            uid?.let { userId->
                val key = discussionsRef.child(discussion.discussionId).child("listOfReplies").push().key
                key?.let { replyId->
                    val reply = Reply(
                        text = replyText,
                        replyId = replyId,
                        userId = userId,
                        timeStamp = System.currentTimeMillis().toString()
                    )

                    discussionsRef.child(discussion.discussionId).child("listOfReplies").child(replyId)
                        .setValue(reply)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e-> throw e}
                }
            }
        } catch (e:Exception) {
            Log.e("DiscussionViewModel", "addReply: ${e.message}")
            onFailure(e.message.toString())
        }
    }

    fun addDiscussion(text: String, onSuccess:()->Unit, onFailure:(String)->Unit) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            uid?.let { userId->
                val dId = discussionsRef.push().key
                dId?.let { discussionId ->
                    val discussion = Discussion(
                        text = text,
                        timeStamp = System.currentTimeMillis().toString(),
                        discussionId = discussionId,
                        userId = userId,
                        listOfReplies = listOf<Reply>()
                    )

                    discussionsRef.child(discussionId).setValue(discussion)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> throw e }
                }
            }
        } catch (e:Exception) {
            Log.e("DiscussionViewModel", "addDiscussion: ${e.message}")
            onFailure(e.message.toString())
        }
    }

    fun getUserFromId(id: String, onSuccess: (KrishnamayaUser)->Unit) {
        Log.d("Harsh", "DiscussionCard/input id = : $id")
        usersRef.child(id).get()
            .addOnSuccessListener { snap ->
                Log.d("Harsh", "DiscussionCard/snap: $snap")
                val user = snap.getValue(KrishnamayaUser::class.java)
                user?.let {
                    onSuccess(it)
                }
            }
            .addOnFailureListener { e-> throw e}
    }

    private fun getUserFromDiscussion(discussion: Discussion, onSuccess:(KrishnamayaUser)->Unit) {
        usersRef.child(discussion.userId).get()
            .addOnSuccessListener { snap ->
                val user = snap.getValue(KrishnamayaUser::class.java)
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { e -> throw e}
    }

    fun getDiscussions(onSuccess: () -> Unit = {}) {
        try {
            val list = mutableListOf<Pair<Discussion, KrishnamayaUser>>()

            discussionsRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { dataSnap->
                        val discussionResult = dataSnap.getValue(Discussion::class.java)

                        discussionResult?.let { myDiscussion ->
                            getUserFromDiscussion(myDiscussion) { myUser ->
                                list.add(myDiscussion to myUser)

                                if(list.size == snapshot.childrenCount.toInt()) {
                                    _liveDiscussionsData.postValue(list)
                                    onSuccess()
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })
        } catch (e:Exception) {
            Log.e("DiscussionViewModel", "getDiscussions: ${e.message}")
        }
    }

    fun getDiscussions(user: KrishnamayaUser, onSuccess: (List<Pair<Discussion, KrishnamayaUser>>) -> Unit = {}) {
        try {
            val list = mutableListOf<Pair<Discussion, KrishnamayaUser>>()

            discussionsRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { dataSnap->
                        val discussionResult = dataSnap.getValue(Discussion::class.java)

                        discussionResult?.let { myDiscussion ->
                            Log.d("priyansh", "getDiscussions: thisId: ${myDiscussion.userId} userId: ${user.userId}")
                            if(myDiscussion.userId == user.userId) {
                                Log.d("priyansh", "getDiscussions: ${myDiscussion to user}")
                                list.add(myDiscussion to user)
                            }
                        }
                    }
                    onSuccess(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })
        } catch (e:Exception) {
            Log.e("DiscussionViewModel", "getDiscussions: ${e.message}")
        }
    }
}