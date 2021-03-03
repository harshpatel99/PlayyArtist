package com.twogentle.inartist.activities

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.okhttp.MediaType
import com.twogentle.inartist.R
import com.twogentle.inartist.adapter.WallRecyclerAdapter
import com.twogentle.inartist.extras.GridSpacingItemDecorator
import com.twogentle.inartist.fragments.AddCategoryFragment
import com.twogentle.inartist.model.Comic
import com.twogentle.inartist.model.Post
import com.twogentle.inartist.model.WallData

class DeletePostActivity : AppCompatActivity() {

    lateinit var adapter: WallRecyclerAdapter
    var data = ArrayList<WallData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_post)

        val recyclerView = findViewById<RecyclerView>(R.id.wallRecyclerView)
        val backButton = findViewById<ImageView>(R.id.wallBackImageView)
        val progressBarView = findViewById<View>(R.id.wallProgressBar)

        progressBarView.visibility = View.VISIBLE

        backButton.setOnClickListener {
            finish()
        }


        val query = FirebaseFirestore.getInstance().collection("posts")
            .orderBy("datePosted", Query.Direction.DESCENDING)
            .limit(40)

        query.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result!!.documents) {
                    val post = document.toObject(Post::class.java)
                    data.add(WallData(1, post!!.url!!, post.id!!, post))
                    adapter.notifyDataSetChanged()
                    progressBarView.visibility = View.GONE
                }
            }
        }

        adapter = WallRecyclerAdapter(this, data)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            GridSpacingItemDecorator(
                2,
                25,
                true
            )
        )
        recyclerView.layoutManager = GridLayoutManager(this, 2)

    }

}