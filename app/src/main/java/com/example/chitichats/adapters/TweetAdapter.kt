package com.example.chitichats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chitichats.R
import com.example.chitichats.data.Tweet

class TweetAdapter(
    private val listOfTweets: List<Tweet>
): RecyclerView.Adapter<TweetAdapter.viewHolder>() {
    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tweetText:TextView=itemView.findViewById(R.id.text_tweet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_tweet,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTweets.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
   val currentTweet= listOfTweets[position]
        holder.tweetText.text=currentTweet.tweetContent
    }

}