package com.example.trixi.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.trixi.R
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.GetFromDbViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.bson.types.ObjectId


class LoginActivity : AppCompatActivity() {

    val model: GetFromDbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login_no_account.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        login_button.setOnClickListener{
            val username = login_input_username_or_password.text.toString()
            val password = login_input_password.text.toString()


            val user = User("",username, "", password, "","","",null, null, "user")

            val post = PostToDb()
            post.PostLoginUser(user)
        }



        //testObserver getAllUsers:
        model.makeApiCall()
        model.getUserMutableLiveDataList().observe(this, Observer{
            it.forEach{
                Log.d("uus", "UserName : ${it.userName!!}")
            }
        } )

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