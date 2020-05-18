package com.enbcreative.demonoteapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        tv_splash_activity.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }
}
