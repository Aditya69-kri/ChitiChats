package com.example.chitichats.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chitichats.R
import com.example.chitichats.data.SuggestedAccount
import de.hdodenhof.circleimageview.CircleImageView

class SuggestedAccountAdapter(
private val listOfAccounts: List<SuggestedAccount>,
    private val context:Context,
    private val clickListener:ClickListener
):RecyclerView.Adapter<SuggestedAccountAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:CircleImageView=itemView.findViewById(R.id.suggested_account_image)
        val email:TextView=itemView.findViewById(R.id.suggested_account_email)
        val followBtn:Button=itemView.findViewById(R.id.btn_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_suggested_account,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAccounts.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currAccount=listOfAccounts[position]
        holder.email.text=currAccount.userEmail
        Glide.with(context)
            .load(currAccount.profileImage)
            .into(holder.image)
        holder.followBtn.setOnClickListener {
  clickListener.onFollowClicked(currAccount.uid)
        }
    }
    interface ClickListener{
        fun onFollowClicked(uid:String)
    }
}