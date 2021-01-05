package com.example.trixi.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trixi.R
import com.example.trixi.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed(
            {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)

    }

}