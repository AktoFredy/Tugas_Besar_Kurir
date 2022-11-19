package com.example.tubes1.userSharedPreferences

import android.content.Context
import android.content.SharedPreferences

class PrefManager (var context: Context){
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor = pref.edit()

    fun setLogin(isLogin: Boolean){
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.commit()
    }

    fun setUsername(username: String){
        editor.putString("username", username)
        editor.commit()
    }

    fun setToken(token: String){
        editor.putString("token", token)
        editor.commit()
    }

    fun setEmail(email: String){
        editor.putString("email", email)
        editor.commit()
    }

    fun setNoTelepon(noTelepon: String){
        editor.putString("noTelepon", noTelepon)
        editor.commit()
    }

    fun islogin():Boolean{
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun getUsername():String?{
        return pref.getString("username", "")
    }

    fun getEmail():String?{
        return pref.getString("email", "")
    }

    fun getToken():String?{
        return pref.getString("token", "")
    }

    fun getNoTelepon():String?{
        return pref.getString("noTelepon", "")
    }

    fun removeData(){
        editor.clear()
        editor.commit()
    }
}