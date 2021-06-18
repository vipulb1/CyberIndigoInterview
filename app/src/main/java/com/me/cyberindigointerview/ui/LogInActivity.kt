package com.me.cyberindigointerview.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.me.cyberindigointerview.R
import com.me.cyberindigointerview.databinding.ActivityLogInBinding
import com.me.cyberindigointerview.utils.CustomeProgressDialog
import com.me.cyberindigointerview.viewmodel.LogInViewModel

class LogInActivity : AppCompatActivity() {

    var binding: ActivityLogInBinding? = null
    var viewmodel: LogInViewModel? = null
    var customeProgressDialog: CustomeProgressDialog? = null
    private val sharedPrefFile = "logPreference"
    private var backPressedTime:Long = 0
    lateinit var backToast:Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        supportActionBar!!.title = "Log In Page"

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("token_key","defaulttoken")

        if (!sharedNameValue.equals("defaulttoken")){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        viewmodel = ViewModelProviders.of(this).get(LogInViewModel::class.java)
        binding?.viewmodel = viewmodel
        customeProgressDialog = CustomeProgressDialog(this)
        initObservables()
    }

    private fun initObservables() {
        viewmodel?.progressDialog?.observe(this, Observer {
            if (it!!) customeProgressDialog?.show() else customeProgressDialog?.dismiss()
        })

        viewmodel?.userLogin?.observe(this, Observer { user ->
            Toast.makeText(this, "welcome, ${user?.token}", Toast.LENGTH_LONG).show()

            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
                Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("token_key","${user?.token}")
            editor.apply()
            editor.commit()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })
    }

    override fun onBackPressed() {
        backToast = Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            val exitIntent = Intent(Intent.ACTION_MAIN)
            exitIntent.addCategory(Intent.CATEGORY_HOME)
            exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(exitIntent)
            return
        } else {
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}