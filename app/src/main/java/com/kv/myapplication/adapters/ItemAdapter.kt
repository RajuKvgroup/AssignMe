package com.kv.myapplication.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.kv.myapplication.R
import com.kv.myapplication.Utils.executeAsyncTask
import com.kv.myapplication.databinding.ItemListRecUiBinding
import com.kv.myapplication.models.ItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class ItemAdapter (
    var context: Activity?, var mList: List<ItemModel>,
    var onclicks: (Int) -> Unit
    ) : RecyclerView.Adapter<ItemAdapter.myView>() {
        class myView(var binding: ItemListRecUiBinding) : RecyclerView.ViewHolder(binding.root) {}
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myView {
            var bindings = ItemListRecUiBinding.inflate(context!!.layoutInflater, parent, false)
            return myView(bindings)
        }

        @SuppressLint("ResourceType")
        override fun onBindViewHolder(holder: myView, position: Int) {
            var data = mList[holder.adapterPosition]
            holder.binding.apply {
                username.setText(data.username)
                data.apply {

                    //// for display image
                    val options: RequestOptions = RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.loading_image_dr)
                        .error(R.drawable.loading_image_dr)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .dontAnimate()
                        .dontTransform()
                    /// check if this image is in local or downloaded
                    val path: File =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AssignMe")
                    val imageFile = File(
                        path,
                        downloadurl.subSequence(downloadurl.length - 4, downloadurl.length)
                            .toString() + ".jpg"
                    )

                    if (imageFile.exists()) {
                        download.setText("View")
                        Glide.with(holder.itemView.context).load(imageFile.absoluteFile)
                            .apply(options).into(post)
                        Log.e(
                            "dsvbdjsb",
                            imageFile.toString() + " cmaklscm" + downloadurl.subSequence(
                                downloadurl.length - 4,
                                downloadurl.length
                            ).toString() + ".jpg"
                        )
                    } else {
                        download.setText("Download")
                        Glide.with(holder.itemView.context).load(downloadurl).apply(options)
                            .into(post)
                        //// click on download button

                    }
                    holder.binding.download.setOnClickListener {
                        if (download.text.toString() != "View") {

                            MaterialDialog(it.context).show {
                                title(text = "Download this Image")
                                message(text = "Do you want to download this image?")
                                positiveButton(R.string.yes) { dialog ->
                                    GlobalScope.executeAsyncTask(
                                        onPreExecute = {
                                            message(text = "downloading....")
                                        },
                                        doInBackground = {
                                            doInBackground(data.downloadurl)
                                        },
                                        onPostExecute = {
                                            dialog.dismiss()
                                            notifyItemChanged(position)
                                        }
                                    )
                                }
                                negativeButton(R.string.disagree) { dialog ->
                                    dialog.dismiss()
                                }
                            }

                        } else {
                            Snackbar.make(
                                it.rootView,
                                "Oops!! This is already downloaded",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }

                    ///// display dialog for each cell
                    holder.itemView.setOnClickListener {
                        onclicks.invoke(holder.adapterPosition)
                    }

                }}}
        override fun getItemCount(): Int {
            return mList.size
        }

    /// download image
    fun doInBackground(params: String?): String? {
        var imageFile:File?=null
        try {
            val url = URL(params)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            val myBitmap = BitmapFactory.decodeStream(input)
            /// create folder to save image
            val path: File =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AssignMe") //Creates app specific folder
            if (!path.exists()) {
                path.mkdirs()
            }

             imageFile = File(path, params!!.subSequence(params.length-4,params.length).toString()+".jpg")
            val stream = FileOutputStream(imageFile)
            val outstream = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream)
            val byteArray: ByteArray = outstream.toByteArray()
            stream.write(byteArray)
            stream.close()
           // Log.e("dsklmvlsd","snclsnv finish"+imageFile)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
          //  Log.e("dsklmvlsd","error"+e.message)
        }
        return imageFile.toString()

    }

}
