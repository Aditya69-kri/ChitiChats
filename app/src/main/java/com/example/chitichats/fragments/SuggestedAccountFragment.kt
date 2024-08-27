package com.example.chitichats.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chitichats.R
import com.example.chitichats.adapters.SuggestedAccountAdapter
import com.example.chitichats.data.SuggestedAccount
import com.example.chitichats.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SuggestedAccountFragment:Fragment(),SuggestedAccountAdapter.ClickListener {
    private lateinit var suggestedAccountAdapter: SuggestedAccountAdapter
    private lateinit var rvSuggestedAccount: RecyclerView
    private val listOfAccount= mutableListOf<SuggestedAccount>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_suggested_account,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSuggestedAccount=view.findViewById(R.id.rvSuggestedAccounts)

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowings=snapshot.child("listOfFollowings").value as  MutableList<String>

                    FirebaseDatabase.getInstance().getReference().child("users")
                        .addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(dataSnapshot in snapshot.children){
                                    val user =dataSnapshot.getValue(User::class.java)
                                    if(user?.uid.toString()!=FirebaseAuth.getInstance().uid.toString()&& !listOfFollowings.contains(user?.uid.toString())){
                                        val suggestedAccount=SuggestedAccount(user?.userImage.toString(),user?.userEmail.toString(),user?.uid.toString())
                                        listOfAccount.add(suggestedAccount)
                                        suggestedAccountAdapter= SuggestedAccountAdapter(listOfAccount,requireContext(),this@SuggestedAccountFragment)
                                        rvSuggestedAccount.adapter=suggestedAccountAdapter
                                        rvSuggestedAccount.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                             //   TODO("Not yet implemented")
                            }

                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
  private fun followUser(uid:String){
      FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
          .addListenerForSingleValueEvent(object:ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                 val listOfFollowing=snapshot.child("listOfFollowings").value as MutableList<String>
                  listOfFollowing.add(uid)
                  FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
                      .child("listOfFollowings").setValue(listOfFollowing)
                  Toast.makeText(requireContext(),"Followed Sussfully",Toast.LENGTH_SHORT).show()
              }

              override fun onCancelled(error: DatabaseError) {
               //   TODO("Not yet implemented")
              }

          })
  }
    override fun onFollowClicked(uid:String) {
       followUser(uid)
    }
}