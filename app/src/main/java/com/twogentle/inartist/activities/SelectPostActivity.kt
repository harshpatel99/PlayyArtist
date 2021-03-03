package com.twogentle.inartist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.twogentle.inartist.R
import com.twogentle.inartist.adapter.SelectPollRecyclerAdapter
import com.twogentle.inartist.extras.GridSpacingItemDecorator
import com.twogentle.inartist.model.Post
import com.twogentle.inartist.model.WallData

class SelectPostActivity : AppCompatActivity() {

    lateinit var adapter: SelectPollRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_post)

        val recyclerView = findViewById<RecyclerView>(R.id.selectRecyclerView)
        val backButton = findViewById<ImageView>(R.id.selectBackImageView)
        val progressBarView = findViewById<View>(R.id.selectProgressBar)

        progressBarView.visibility = View.VISIBLE

        backButton.setOnClickListener {
            finish()
        }

        val data = ArrayList<WallData>()

        val query = FirebaseFirestore.getInstance().collection("posts")
            .orderBy("datePosted", Query.Direction.DESCENDING)

        query.get().addOnCompleteListener {
            if (it.isSuccessful) {

                for (document in it.result!!.documents) {
                    val post = document.toObject(Post::class.java)

                    if (post!!.contentType == "image") {
                        data.add(WallData(1, post.url!!, post.id!!, post))
                    }
                    adapter.notifyDataSetChanged()
                    progressBarView.visibility = View.GONE
                }
            }
        }

        adapter = SelectPollRecyclerAdapter(this, this@SelectPostActivity, data)
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