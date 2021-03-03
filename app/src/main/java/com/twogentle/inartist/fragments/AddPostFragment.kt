package com.twogentle.inartist.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
import com.twogentle.inartist.R
import com.twogentle.inartist.model.Artist
import com.twogentle.inartist.model.Post
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddPostFragment : Fragment() {

    private lateinit var postData: Post
    private lateinit var postDoc: DocumentReference
    private lateinit var dialog: AlertDialog
    private lateinit var imageUrlEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_fragment, container, false)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
        dialog.show()

        var isPostTypeDone = false
        var isCategoryDone = false
        var isCollectionDone = false
        var isArtistDone = false

        val postTypeSpinner = view.findViewById<Spinner>(R.id.postTypeSpinner)
        val artistSpinner = view.findViewById<Spinner>(R.id.postArtistSpinner)
        val categorySpinner = view.findViewById<Spinner>(R.id.postCategorySpinner)
        val collectionSpinner = view.findViewById<Spinner>(R.id.postCollectionSpinner)
        val uploadButton = view.findViewById<MaterialCardView>(R.id.postUploadCard)
        imageUrlEditText = view.findViewById<TextInputEditText>(R.id.postImageUrlEditText)

        postData = Post()

        val postTypeArray = ArrayList<String>()
        postTypeArray.add("Image Post")
        postTypeArray.add("Premium Image Post")

        val firestore = FirebaseFirestore.getInstance()

        val artistArray = ArrayList<String>()
        val artistDataArray = ArrayList<Artist>()
        firestore.collection("artist").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!.documents) {
                        val artist = Artist()
                        val artistName = document["name"].toString()
                        artist.name = document["name"].toString()
                        artist.bio = document["bio"].toString()
                        artist.username = document["username"].toString()
                        artist.social = document["social"] as HashMap<String, String>
                        artist.id = document.id
                        artistDataArray.add(artist)
                        artistArray.add(artistName)
                    }

                    artistSpinner.adapter =
                        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, artistArray)
                    artistSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                Toast.makeText(context, "Please select Artist", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                postData.artistName = artistDataArray[position].name
                                postData.artistSocial = artistDataArray[position].social
                                postData.artistID = artistDataArray[position].id
                                postData.artistUsername = artistDataArray[position].username
                                postData.artistBio = artistDataArray[position].bio
                            }
                        }
                    isArtistDone = true
                    if (isArtistDone and isCategoryDone and isCollectionDone and isPostTypeDone) {
                        dialog.dismiss()
                    }
                }
            }

        val categoryArray = ArrayList<String>()


        val collectionArray = ArrayList<String>()
        firestore.collection("collections").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!.documents) {
                        val collectionName = document["title"].toString()
                        collectionArray.add(collectionName)
                    }

                    collectionSpinner.adapter =
                        ArrayAdapter(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            collectionArray
                        )
                    collectionSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                Toast.makeText(
                                    context,
                                    "Please select Collection",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                postData.collection = collectionArray[position]
                                dialog.show()
                                categoryArray.clear()
                                firestore.collection("categories")
                                    .whereEqualTo("collection", postData.collection).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            for (document in it.result!!.documents) {
                                                val categoryName = document["title"].toString()
                                                categoryArray.add(categoryName)
                                            }

                                            categorySpinner.adapter =
                                                ArrayAdapter(
                                                    context!!,
                                                    android.R.layout.simple_spinner_item,
                                                    categoryArray
                                                )
                                            categorySpinner.onItemSelectedListener =
                                                object : AdapterView.OnItemSelectedListener {
                                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                                        Toast.makeText(
                                                            context,
                                                            "Please select Category",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                    }

                                                    override fun onItemSelected(
                                                        p0: AdapterView<*>?,
                                                        p1: View?,
                                                        position: Int,
                                                        p3: Long
                                                    ) {
                                                        postData.category = categoryArray[position]
                                                    }
                                                }
                                            isCategoryDone = true
                                            if (isArtistDone and isCategoryDone and isCollectionDone and isPostTypeDone) {
                                                dialog.dismiss()
                                            }
                                        }
                                    }
                            }
                        }
                    isCollectionDone = true
                    if (isArtistDone and isCategoryDone and isCollectionDone and isPostTypeDone) {
                        dialog.dismiss()
                    }
                }
            }


        val arrayAdapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, postTypeArray)
        postTypeSpinner.adapter = arrayAdapter
        postTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Please select Post Type", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (postTypeArray[position]) {
                    "Image Post" -> postData.type = "free"
                    "Premium Image Post" -> postData.type = "premium"
                }
            }
        }
        isPostTypeDone = true
        if (isArtistDone and isCategoryDone and isCollectionDone and isPostTypeDone) {
            dialog.dismiss()
        }

        collectionSpinner.adapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, collectionArray)
        collectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Please select collecction", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                postData.collection = collectionArray[position]
            }
        }
        isCollectionDone = true
        if (isArtistDone and isCategoryDone and isCollectionDone and isPostTypeDone) {
            dialog.dismiss()
        }

        postData.datePosted = Calendar.getInstance().timeInMillis
        postData.views = 0
        postData.likes = 0
        postData.saves = 0
        postData.shares = 0
        postData.views = 0
        postData.contentType = "image"
        postData.userLiked = ArrayList()
        postData.userSaved = ArrayList()
        postData.userUnlocked = ArrayList()
        postData.date = Calendar.getInstance().timeInMillis


        postDoc = firestore.collection("posts").document()
        postData.id = postDoc.id

        uploadButton.setOnClickListener {


            when {
                imageUrlEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        context,
                        "Post Url Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            postData.title =
                "art/" + postData.collection.toString() + "/" + postData.artistName + "_" + Calendar.getInstance().timeInMillis


            if (imageUrlEditText.text.toString().contains("insta")) {
                val instaUrl = imageUrlEditText.text.toString().split("/?")[0]
                postData.instagramUrl = instaUrl
                getInstaUrl(instaUrl)

            } else {
                postData.url = imageUrlEditText.text.toString()
                postData.artistProfilePic = imageUrlEditText.text.toString()
                postData.thumbnail = ""
                postData.instagramUrl = ""

                postDoc.set(postData).addOnCompleteListener {
                    Toast.makeText(context, "Post is Live!", Toast.LENGTH_SHORT).show()
                    val map = mapOf<String, Any>(
                        Pair("totalPosts", FieldValue.increment(1)),
                        Pair("posts", FieldValue.arrayUnion(postData.id)),
                        Pair("artistProfilePic", imageUrlEditText.text.toString())
                    )
                    FirebaseFirestore.getInstance().collection("artist")
                        .document(postData.artistID!!)
                        .update(map)

                    FirebaseFirestore.getInstance().collection("posts")
                        .whereEqualTo("artistID",postData.artistID)
                        .get().addOnCompleteListener {
                            if(it.isSuccessful){
                                for(document in it.result!!.documents){
                                    document.reference.update("artistProfilePic",imageUrlEditText.text.toString())
                                }
                            }
                        }

                    dialog.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            /*postDoc.set(postData).addOnCompleteListener {
                Toast.makeText(context, "Post is Live!", Toast.LENGTH_SHORT).show()
                val map = mapOf<String, Any>(
                    Pair("totalPosts", FieldValue.increment(1)),
                    Pair("posts", FieldValue.arrayUnion(postData.id))
                )
                FirebaseFirestore.getInstance().collection("artist")
                    .document(postData.artistID!!)
                    .update(map)
            }.addOnFailureListener {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }*/
        }

        return view
    }

    private fun getInstaUrl(requestUrl: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$requestUrl/?__a=1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                activity!!.runOnUiThread {
                    Toast.makeText(context, "Error Getting Image", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onResponse(response: Response?) {
                val strResponse = response!!.body().string()
                val jsonContact = JSONObject(strResponse)
                val jsonQL = jsonContact["graphql"] as JSONObject
                val jsonMedia = jsonQL["shortcode_media"] as JSONObject
                val thumbnailUrl = jsonMedia["display_url"].toString()

                if (thumbnailUrl.isNotEmpty())
                    activity!!.runOnUiThread {
                        postData.url = thumbnailUrl
                        postData.artistProfilePic = thumbnailUrl
                        postData.thumbnail = ""

                        postDoc.set(postData).addOnCompleteListener {
                            Toast.makeText(context, "Post is Live!", Toast.LENGTH_SHORT).show()
                            val map = mapOf<String, Any>(
                                Pair("totalPosts", FieldValue.increment(1)),
                                Pair("posts", FieldValue.arrayUnion(postData.id))
                            )
                            FirebaseFirestore.getInstance().collection("artist")
                                .document(postData.artistID!!)
                                .update(map)
                            dialog.dismiss()
                            imageUrlEditText.setText("")

                        }.addOnFailureListener {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            imageUrlEditText.setText("")
                        }
                    }

            }


        })
    }
}