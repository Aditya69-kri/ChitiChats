package com.example.chitichats.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.chitichats.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileImage:CircleImageView
    private lateinit var openGalleryBtn:Button
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        openGalleryBtn.setOnClickListener {
            val galleryIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,101)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101&&resultCode== RESULT_OK){
            profileImage.setImageURI(data?.data)
            uploadProfileImage(data?.data)
        }
    }

    private fun uploadProfileImage(uri:Uri?){
        val profileImageName=UUID.randomUUID().toString()+".jpg"
        val storeageRef=FirebaseStorage.getInstance().getReference().child("profileImages/$profileImageName")
        storeageRef.putFile(uri!!).addOnSuccessListener {
            val result=it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid.toString())
                    .child("userImage").setValue(it.toString())
            }
        }
    }

    private fun init(){
        profileImage=findViewById(R.id.profile_image)
        openGalleryBtn=findViewById(R.id.btn_open_gallary)
        toolbar=findViewById(R.id.toolbar)
        FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val link=snapshot.child("userImage").value.toString()

                    if(!link.isNullOrBlank()){
                        Glide.with(this@ProfileActivity)
                            .load(link)
                            .into(profileImage)
                    } else{
                        profileImage.setImageResource(R.drawable.ic_launcher_background)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}