package com.kv.myapplication.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.kv.myapplication.R

class Common {
    companion object {
        const val USER_NAME = "username"
        const val USER_ID = "userid"
        const val UESR_EMAIL = "email"
        const val PASSWORD = "password"
        const val SHAREPREF_TABLENAME = "ItemTable"
        /////// progress bar
        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog_ui, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog
        }}
    }