package com.example.krishnamaya1.discussions.data

data class Discussion(
    val text: String,
    val timeStamp: String,
    val userId: String,
    val discussionId:String,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val listOfReplies: List<Discussion> = emptyList()
)