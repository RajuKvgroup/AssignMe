package com.kv.myapplication.Utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedPref
    private constructor() {
        private var editor: SharedPreferences.Editor?=null
        companion object {
            private lateinit var pref: SharedPreferences
            var sharedPref: SharedPref? = null
            fun getInstance(context: Context): SharedPref {
                if (sharedPref == null) {
                    synchronized(this) {
                        if (sharedPref == null) {
                            sharedPref = SharedPref()
                            pref=context.getSharedPreferences(Common.SHAREPREF_TABLENAME, Context.MODE_PRIVATE)
                        }
                    }
                }
                return sharedPref!!
            }
        }

        ////// save data
        fun saveData(key:String?,value:String?){
            editor= pref.edit()
            editor!!.putString(key,value).apply()
            editor!!.commit()
        }
        //// retrive data
        fun getData(key:String?):String{
            var value=""
            value= pref.getString(key,"")!!+""
            return value
        }
       /// clear data
        fun clearData(){
            pref.edit().clear().apply()

        }
        //// save data as a flow
        private var key="kvsoft"
        // private val _constSate = MutableStateFlow(pref.getString(key,"0"))

        private var user = MutableStateFlow<String>("")
        val allUsers: StateFlow<String> = user
        sealed class MainControler() {
            data class  loading(val loadin:String) : MainControler()
            object Empty : MainControler()
            data class Error(val msg: String) : MainControler()
            data class Success(val result: String) : MainControler()
        }
        fun savedataFlow(key:String,value:String){
            editor= pref.edit()
            editor!!.putString(key,value).apply()
            editor!!.commit()
            user.value = allUsers.value
        }
        fun getFlowData(params:String) : StateFlow<String> {
            key=params
            return allUsers

        }

    }
