package com.twogentle.inartist.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.twogentle.inartist.MainActivity
import com.twogentle.inartist.R
import com.twogentle.inartist.fragments.AddComicFragment
import com.twogentle.inartist.model.ComicImageItem
import kotlin.collections.ArrayList

class AddComicItemRecyclerAdapter(
    private val context: Context,
    private val data: ArrayList<ComicImageItem>,
    private val activity: MainActivity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectComicImageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_comic_image_select, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectComicImageViewHolder).bindViews(context, data[position], activity)
    }

    class SelectComicImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            lateinit var imageView: ImageView
        }

        fun bindViews(
            context: Context,
            data: ComicImageItem,
            activity: MainActivity
        ) {
            //val countTextView = itemView.findViewById<TextView>(R.id.comicSelectImageCountTextView)
            //imageView = itemView.findViewById(R.id.comicSelectImageView)
            //val browseButton = itemView.findViewById<MaterialButton>(R.id.comicSelectBrowseButton)

            //countTextView.text = (1 + adapterPosition).toString()

            /*browseButton.setOnClickListener {
                val sharedPreferences = context.getSharedPreferences("comic", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("count", adapterPosition)
                editor.apply()
                val intent = Intent()
                intent.type = "image*"
                intent.action = Intent.ACTION_GET_CONTENT
                activity.startActivityForResult(
                    Intent.createChooser(intent, "Select Post Image"),
                    AddComicFragment.RESULT_BROWSE_IMAGE

            }*/
        }
    }

}
