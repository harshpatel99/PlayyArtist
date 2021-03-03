package com.twogentle.inartist.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.twogentle.inartist.R
import com.twogentle.inartist.activities.DeletePostActivity
import com.twogentle.inartist.activities.SelectPostActivity
import com.twogentle.inartist.model.WallData
import kotlin.collections.ArrayList

class SelectPollRecyclerAdapter(
    private val context: Context,
    private val activity: SelectPostActivity,
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
                R.layout.list_item_select_post, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bindViews(context,activity, data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return WallData.TYPE_IMAGE
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViews(context: Context, activity: SelectPostActivity, data: WallData) {
            val imageView = itemView.findViewById<ImageView>(R.id.listItemWallPostImageView)

            Glide.with(context).load(data.url)
                .centerCrop().placeholder(R.drawable.ic_round_blur_on_24).into(imageView)

            imageView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("post", data.post)
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
        }
    }
}
