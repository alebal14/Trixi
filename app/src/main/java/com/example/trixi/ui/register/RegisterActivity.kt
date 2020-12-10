package com.example.trixi.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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
}