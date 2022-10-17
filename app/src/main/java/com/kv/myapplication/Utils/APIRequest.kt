package com.kv.myapplication.Utils

import android.app.Activity
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.error.*
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import org.json.JSONObject

class APIRequest
private constructor() {

    companion object {
        //// create singleton
        private var apiRequest: APIRequest? = null
        var TAG = "Apitest"
        fun getInstant(): APIRequest {
            synchronized(this) {
                apiRequest = APIRequest()
            }
            return apiRequest!!
        }
    }


    fun getApi(
        jsoanobject: JSONObject?,
        method: Int,
        url: String,
        responseCallback: (response: String) -> Unit,
        errorCallback: (error: VolleyError) -> Unit
    ):
            StringRequest {
       val murl=Constraint.BASE_URL+url
        return volleyCall(method, murl, jsoanobject, responseCallback, errorCallback)
    }

    /////// global function to call volley network
    fun volleyCall(
        method: Int,
        url: String,
        jsoanobject: JSONObject?,
        responseCallback: (response: String) -> Unit,
        errorCallback: (error: VolleyError) -> Unit
    ): StringRequest { return object : StringRequest(method, url,
        Response.Listener { response ->
            responseCallback(response.toString() )
        },
        Response.ErrorListener { error ->
            getErrorMessageLogOut(error, errorCallback)
        }) {

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val params: MutableMap<String, String> = HashMap()
            params["Content-Type"] = "application/x-www-form-urlencoded"
            return params
        }
    }  }




//            JsonObjectRequest {
//        return object :
//
//            JsonObjectRequest(method, url, jsoanobject,
//                { response ->
//                    Log.e(
//                        "device_token",
//                        jsoanobject.toString() + url + "volley  " + response.toString()
//                    )
//                    responseCallback(response.toString() )
//                },
//                { error ->
//                    Log.e("device_token", "volley  error " + error.message.toString())
//                    // errorCallback(error) // getErrorMessageLogOut(error, errorCallback)
//                    getErrorMessageLogOut(error, errorCallback)
//                }) {
//            override fun getHeaders(): Map<String, String> {
//                val map = HashMap<String, String>()
//                map["Content-Type"] = "application/json"
//                return map
//            }
//        }
//    }
}

private fun getErrorMessageLogOut(
    volleyError: VolleyError,
    errorCallback: (error: VolleyError) -> Unit
) {
    var message: String? = null
    when (volleyError) {
        is NetworkError -> message = "Cannot connect to Internet...Please check your connection!"
        is ServerError -> message =
            "The server could not be found. Please try again after some time!!"
        is NoConnectionError -> message =
            "Cannot connect to Internet...Please check your connection!"
        is TimeoutError -> message = "Connection TimeOut! Please check your internet connection."
        else -> errorCallback(volleyError)
    }
    if (message != null) {
        val error = VolleyError(NetworkResponse("{\"message\":\"$message\"}".toByteArray()))
        errorCallback(error)
    }
}

fun get() {}
