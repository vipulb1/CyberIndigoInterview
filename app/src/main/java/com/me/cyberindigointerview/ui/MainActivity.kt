package com.me.cyberindigointerview.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.me.cyberindigointerview.R
import com.me.cyberindigointerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityMainBinding.imageView.animation = AnimationUtils.loadAnimation(this,
            R.anim.spash_anim
        )

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        },5000);
    }
}