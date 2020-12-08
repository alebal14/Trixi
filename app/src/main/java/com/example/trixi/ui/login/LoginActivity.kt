package com.example.trixi.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.trixi.R
import com.example.trixi.repository.RepoViewModel
import com.example.trixi.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val model: RepoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login_no_account.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //testObserver getAllUsers:
        model.makeApiCall()
        model.getUserMutableLiveDataList().observe(this, Observer{
            it.forEach{
                Log.d("uus", "UserName : ${it.userName} UserId : ${it.id}")
            }
        } )

    }
}