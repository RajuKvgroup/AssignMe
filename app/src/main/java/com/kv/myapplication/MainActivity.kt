package com.kv.myapplication

import android.R
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.kv.myapplication.Utils.APIConnect
import com.kv.myapplication.Utils.APIRequest
import com.kv.myapplication.Utils.Constraint
import com.kv.myapplication.adapters.ItemAdapter
import com.kv.myapplication.databinding.ActivityMainBinding
import com.kv.myapplication.models.ItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var list: ArrayList<ItemModel>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            itemRecycler.setHasFixedSize(true)
            itemRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
            refereshData.setOnClickListener { getData() }
        }
        getData()
    }

    private fun getData() {
        list = ArrayList()
        lifecycle.coroutineScope.launch(Dispatchers.IO){
            var anim= AnimationSet(true)
            anim.setInterpolator(DecelerateInterpolator())
            anim.setFillAfter(true)
            anim.setFillEnabled(true)
            var  animRotate = RotateAnimation(
                -360.0f, 0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
            )

            animRotate!!.duration = 1500
            animRotate!!.repeatCount=Animation.INFINITE
            animRotate!!.fillAfter = true
            anim.addAnimation(animRotate)
            binding.refereshData.startAnimation(anim);
            getInfo()
        }

    }

    private fun getInfo(){
     //// use volley for network request
        APIConnect.getInstance(this).addToRequestQueue(
            APIRequest.getInstant()
                .getApi(null, 0, Constraint.ITEM_LIST+"?page=2&limit=20", {
                    Log.e("cmkslmckls", " sdjknvs" + it)
                    lifecycle.coroutineScope.launch(Dispatchers.Main) {
                        binding.refereshData.setAnimation(null)
                        if (it != "ERROR" || it.isNotEmpty() || it != null) {
                            var jsArray = JSONArray(it)
                            list!!.clear()
                            for (i in 0 until jsArray.length()) {
                                var model = ItemModel()
                                var obj = jsArray.getJSONObject(i)
                                model.downloadurl = obj.getString("download_url")
                                model.username = obj.getString("author")
                                model.imageurl = obj.getString("url")
                                model.width = obj.getString("width")
                                model.height = obj.getString("height")
                                model.id = obj.getString("id")
                                list!!.add(model)
                            }

                            binding.itemRecycler.adapter = ItemAdapter(this@MainActivity, list!!, {
                                var pos = it;
                                MaterialDialog(this@MainActivity).show {
                                    title(text = list!![pos].username)
                                    message(text = list!![pos].username)
                                }
                            })
                        }
                    }
                }, {
                    Log.e("cmkslmckls", " sdjknvddddds " + it.message.toString())
                }))

    }

}