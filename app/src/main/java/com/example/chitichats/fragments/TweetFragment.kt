package com.example.chitichats.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chitichats.R
import com.example.chitichats.adapters.TweetAdapter
import com.example.chitichats.data.Tweet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TweetFragment: Fragment() {
    private lateinit var tweetAdapter:TweetAdapter
    private lateinit var rvTweet:RecyclerView
    private val listOfTweets= mutableListOf<Tweet>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_tweet,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   rvTweet= view.findViewById(R.id.rv_tweet)
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowingUids=snapshot.child("listOfFollowings").value as MutableList<String>
                    listOfFollowingUids.add(FirebaseAuth.getInstance().uid.toString())
                   listOfFollowingUids.forEach{
                       getTweetFromUids(it)
                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
    private fun getTweetFromUids(uid:String){
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var tweetList= mutableListOf<String>()
                    snapshot.child("listOfTweets").value?.let {
                        tweetList=it as MutableList<String>
                    }
                    tweetList.forEach {
                        if(!it.isNullOrBlank()){
                            listOfTweets.add(Tweet(it))
                        }
                    }
                    tweetAdapter= TweetAdapter(listOfTweets)
                    rvTweet.layoutManager=LinearLayoutManager(requireContext())
                    rvTweet.adapter=tweetAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}