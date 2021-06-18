package com.me.cyberindigointerview.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.me.cyberindigointerview.model.User
import com.me.cyberindigointerview.remote.ApiClient
import com.me.cyberindigointerview.remote.ApiInterface
import mvvm.f4wzy.com.samplelogin.util.SingleLiveEvent
import mvvm.f4wzy.com.samplelogin.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInViewModel(application: Application) : AndroidViewModel(application), Callback<User> {

    var btnSelected: ObservableBoolean? = null
    var email: ObservableField<String>? = null
    var password: ObservableField<String>? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var userLogin: MutableLiveData<User>? = null

    init {
        btnSelected = ObservableBoolean(false)
        progressDialog = SingleLiveEvent<Boolean>()
        email = ObservableField("")
        password = ObservableField("")
        userLogin = MutableLiveData<User>()
    }

    fun onEmailChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(s.toString()) && password?.get()!!.length >= 2)
    }

    fun onPasswordChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(email?.get()!!) && s.toString().length >= 2)
    }

    fun login() {
        progressDialog?.value = true
        ApiClient.client.create(ApiInterface::class.java).LOGIN(email = email?.get()!!
            , password = password?.get()!!)
            .enqueue(this)
    }

    override fun onResponse(call: Call<User>?, response: Response<User>?) {
        progressDialog?.value = false
        userLogin?.value = response?.body()
    }

    override fun onFailure(call: Call<User>?, t: Throwable?) {
        progressDialog?.value = false
    }
}