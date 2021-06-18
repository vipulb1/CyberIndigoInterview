package com.me.cyberindigointerview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.cyberindigointerview.model.UsersData
import com.me.cyberindigointerview.remote.ApiClient
import com.me.cyberindigointerview.remote.ApiInterface
import mvvm.f4wzy.com.samplelogin.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application), Callback<UsersData> {

    var progressDialog: SingleLiveEvent<Boolean>? = null
    var userData: MutableLiveData<UsersData>? = null
    private var searchUser = ArrayList<UsersData.UserInfo?>()
    private var userList = ArrayList<UsersData.UserInfo?>()

    private val _userLiveData = MutableLiveData<ArrayList<UsersData.UserInfo?>>()
    val userLiveData: LiveData<ArrayList<UsersData.UserInfo?>>
        get() = _userLiveData

    private val _userNameLiveData = MutableLiveData<String>()
    val userNameLiveData: LiveData<String>
        get() = _userNameLiveData

    private val _loadMoreListLiveData = MutableLiveData<Boolean>()
    val loadMoreListLiveData: LiveData<Boolean>
        get() = _loadMoreListLiveData

    init {
        _loadMoreListLiveData.value = false
        _userNameLiveData.value = ""
    }

    fun userData() {
        searchUser.clear()
        progressDialog?.value = true
        ApiClient.client.create(ApiInterface::class.java)
            .USERDATA().enqueue(this)
    }

    fun searchUsers(query: String) {
        if (query.isEmpty() || query == ""){
            userData()
        }else {
            filterData(query)
        }
    }

    private fun filterData(query: String) {
        progressDialog?.value =true
        searchUser.clear()

        userList.forEach { it ->
            if (it!!.first_name.contains(query) ||
                it.last_name.contains(query) || it.email.contains(query)
            ) {
                searchUser.add(it)
            }
        }
        /*searchUser = userList.filter {it -> it!!.first_name.contains(query) ||  it.last_name.contains(
            query) || it.email.contains(query)} as ArrayList<UsersData.UserInfo?>*/
        _userLiveData.postValue(searchUser)
    }

    override fun onResponse(call: Call<UsersData>?, response: Response<UsersData>?) {
        progressDialog?.value = false

        if (response?.body()?.userInfo?.size!! > 0) {
            response?.body()?.userInfo?.let { userList.addAll(it) }
            response?.body()?.userInfo?.let { searchUser.addAll(it) }
            _userLiveData.postValue(searchUser)
        }
    }

    override fun onFailure(call: Call<UsersData>?, t: Throwable?) {
        progressDialog?.value = false
    }

    fun loadMore() {
        _userLiveData.postValue(searchUser)
    }
}