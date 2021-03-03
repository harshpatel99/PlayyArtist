package com.twogentle.inartist.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
import com.twogentle.inartist.R
import com.twogentle.inartist.activities.DeletePostActivity
import com.twogentle.inartist.fragments.AddCategoryFragment
import com.twogentle.inartist.model.WallData
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.collections.ArrayList

class WallRecyclerAdapter(
    private val context: Context,
    private val data: ArrayList<WallData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_IMAGE = 1
        private const val TYPE_IMAGE_LOCKED = 2
        private const val TYPE_UNAVAILABLE = 99
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_wall_post, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bindViews(context, data[position], this)
    }

    override fun getItemViewType(position: Int): Int {
        return WallData.TYPE_IMAGE
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var url = ""
        lateinit var postData: WallData
        lateinit var appContext: Context
        lateinit var dialogUpload: AlertDialog

        fun bindViews(context: Context, data: WallData, adapter: WallRecyclerAdapter) {
            val imageView = itemView.findViewById<ImageView>(R.id.listItemWallPostImageView)
            val videoImageView =
                itemView.findViewById<ImageView>(R.id.listItemWallPostVideoImageView)
            val deletePost = itemView.findViewById<ImageView>(R.id.listItemWallPostDeleteImageView)

            postData = data
            appContext = context

            if(data.post!!.type == "video") {
                videoImageView.visibility = View.VISIBLE
                Glide.with(context).load(data.post!!.thumbnail)
                    .centerCrop().placeholder(R.drawable.ic_round_blur_on_24).into(imageView)
            }else{
                Glide.with(context).load(data.url)
                    .centerCrop().placeholder(R.drawable.ic_round_blur_on_24).into(imageView)
            }

            deletePost.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                builder.setTitle("Delete Post?")
                builder.setMessage("This can't be undone.")
                builder.setPositiveButton(
                    "Delete"
                ) { dialogInterface, _ ->
                    val processBuilder = AlertDialog.Builder(context)
                    processBuilder.setCancelable(false)
                    processBuilder.setView(R.layout.progress_dialog)
                    val dialog = processBuilder.create()
                    dialog.show()
                    FirebaseFirestore.getInstance().collection("posts").document(data.id).delete()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Snackbar.make(deletePost, "Post Deleted.", Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                            dialog.dismiss()
                            adapter.removeItem(adapterPosition)
                        }
                    dialogInterface.dismiss()
                }
                builder.show()
            }
        }
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

}
