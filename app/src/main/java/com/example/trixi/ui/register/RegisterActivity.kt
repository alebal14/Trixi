package com.example.trixi.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //selectProfileImage()


        register_already_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        //button register:
        /*   val id : ObjectId? = null
           val userName = "anna"
           val email = "anna@a.com"
           val password = "pass123"

           val user = User(id, userName, email, password)

           val post = PostToDb()
           post. makeApiPost(user)*/

    }

//        var selectedPhotouri: Uri? = null
//
//        private fun selectProfileImage(){
//            register_profile_image.setOnClickListener {
//                val intent = Intent(Intent.ACTION_PICK)
//                intent.type = "image/*"
//                startActivityForResult(intent, 0)
//            }
//
//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotouri)
//            register_profile_image.setImageBitmap(bitmap)
//
//        }
}