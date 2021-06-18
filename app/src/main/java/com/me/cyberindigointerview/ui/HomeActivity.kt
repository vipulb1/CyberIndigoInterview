package com.me.cyberindigointerview.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.cyberindigointerview.R
import com.me.cyberindigointerview.adapter.Usersadapter
import com.me.cyberindigointerview.databinding.ActivityHomeBinding
import com.me.cyberindigointerview.utils.RecyclerItemClickListener
import com.me.cyberindigointerview.utils.dismissKeyboard
import com.me.cyberindigointerview.viewmodel.HomeViewModel
import com.me.cyberindigointerview.model.UsersData as UsersData1

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    var binding: ActivityHomeBinding? = null
    private val sharedPrefFile = "logPreference"
    private var backPressedTime:Long = 0
    lateinit var backToast: Toast

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }

    var viewmodel: HomeViewModel? = null

    private lateinit var usersadapter: Usersadapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        supportActionBar!!.title = "Home"

        viewmodel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding?.viewmodel = viewmodel

        setupUI()

        viewmodel!!.userData()

        initializeObserver()
        setupAPICall()
    }

    private fun setupUI() {
        usersadapter = Usersadapter()
        binding!!.userRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = usersadapter
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    applicationContext,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                        }
                    })
            )
        }
    }

    private fun initializeObserver() {
        viewmodel!!.userNameLiveData.observe(this, Observer {
            Log.i("Info", "Movie Name = $it")
        })
        viewmodel!!.loadMoreListLiveData.observe(this, Observer {
            if (it) {
                usersadapter.setData(null)
                Handler().postDelayed({
                    viewmodel!!.loadMore()
                }, 2000)
            }
        })
    }

    private fun setupAPICall() {
        viewmodel!!.userLiveData.observe(this, Observer {
            usersadapter.setData(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            queryHint = "Search"
            isSubmitButtonEnabled = true
            onActionViewExpanded()
        }
        search(searchView)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.location -> {
                startActivity(Intent(this,LocationActivity::class.java))
                finish()
                true
            }
            R.id.logout ->{
                val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
                    Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.clear()
                editor.apply()
                editor.commit()
                startActivity(Intent(this,LogInActivity::class.java))
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                dismissKeyboard(searchView)
                searchView.clearFocus()
                viewmodel!!.searchUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewmodel!!.searchUsers(newText)
                return false
            }
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