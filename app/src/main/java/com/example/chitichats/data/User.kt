package com.example.chitichats.data

data class User(
    val userEmail:String="",
    val userImage:String="",
    val listOfFollowings: List<String> = listOf(),
    val listOfTweets: List<String> = listOf(),
    val uid:String=""
    )

