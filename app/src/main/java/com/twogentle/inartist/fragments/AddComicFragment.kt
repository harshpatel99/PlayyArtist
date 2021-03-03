package com.twogentle.inartist.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
import com.twogentle.inartist.MainActivity
import com.twogentle.inartist.R
import com.twogentle.inartist.adapter.AddComicItemRecyclerAdapter
import com.twogentle.inartist.adapter.WallRecyclerAdapter
import com.twogentle.inartist.extras.GridSpacingItemDecorator
import com.twogentle.inartist.model.*
import kotlinx.android.synthetic.main.fragment_add_comic.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddComicFragment : Fragment() {

    private lateinit var filePath: Uri

    //private lateinit var postImageView: ImageView
    private lateinit var comicData: Comic
    private lateinit var comicDoc: DocumentReference
    private lateinit var dialogUpload: AlertDialog
    private lateinit var dialog : AlertDialog
    private lateinit var newComicTotalImagesEditText : TextInputEditText

    lateinit var urls: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_comic, container, false)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
        dialog.show()

        /*var isPostTypeDone = false
        var isCategoryDone = false*/
        var isCollectionDone = false
        var isArtistDone = false

        newComicTotalImagesEditText =
            view.findViewById(R.id.newComicTotalImagesEditText)
        val newComicTotalUnlockedImagesEditText =
            view.findViewById<TextInputEditText>(R.id.newComicTotalUnlockedImagesEditText)
        val artistSpinner = view.findViewById<Spinner>(R.id.newComicArtistSpinner)
        val collectionSpinner = view.findViewById<Spinner>(R.id.newComicCollectionSpinner)
        val uploadButton = view.findViewById<MaterialCardView>(R.id.newComicUploadCard)

        comicData = Comic()

        val firestore = FirebaseFirestore.getInstance()

        val artistArray = ArrayList<String>()
        val artistDataArray = ArrayList<ComicArtist>()
        firestore.collection("comicArtist").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!.documents) {
                        val artist = ComicArtist()
                        val artistName = document["name"].toString()
                        artist.name = document["name"].toString()
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
                                comicData.artistName = artistDataArray[position].name
                                comicData.artistSocial = artistDataArray[position].social
                                comicData.artistID = artistDataArray[position].id
                            }
                        }
                    isArtistDone = true
                    if (isArtistDone and isCollectionDone) {
                        dialog.dismiss()
                    }
                }
            }

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
                                comicData.collection = collectionArray[position]
                            }
                        }
                    isCollectionDone = true
                    if (isArtistDone and isCollectionDone) {
                        dialog.dismiss()
                    }
                }
            }

        if (isArtistDone and isCollectionDone) {
            dialog.dismiss()
        }

        comicData.datePosted = Calendar.getInstance().timeInMillis
        comicData.views = 0
        comicData.likes = 0
        comicData.shares = 0
        comicData.views = 0
        comicData.contentType = "image"
        comicData.userLiked = ArrayList()
        comicData.userUnlocked = ArrayList()
        comicData.date = Calendar.getInstance().timeInMillis


        comicDoc = firestore.collection("comics").document()
        comicData.id = comicDoc.id

        uploadButton.setOnClickListener {

            if(newComicTotalImagesEditText.toString().isEmpty()){
                Toast.makeText(
                    context,
                    "Url Required.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(newComicTotalUnlockedImagesEditText.text.toString().isEmpty()){
                Toast.makeText(
                    context,
                    "Please, select number of the unlocked comic images.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            urls = ArrayList()

            val url = newComicTotalImagesEditText.text.toString().split("?")[0] + "?__a=1"

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(response: Response?) {
                    val strResponse = response!!.body().string()
                    val jsonContact = JSONObject(strResponse)
                    val jsonQL = jsonContact["graphql"] as JSONObject
                    val jsonMedia = jsonQL["shortcode_media"] as JSONObject
                    val thumbnailUrl = jsonMedia["display_url"].toString()

                    if(jsonMedia.has("edge_sidecar_to_children")) {
                        val jsonCarousel = jsonMedia["edge_sidecar_to_children"] as JSONObject
                        val jsonEdges = jsonCarousel.getJSONArray("edges")
                        for(carouselImageCount in 0 until jsonEdges.length()){
                            val jsonNode = jsonEdges.getJSONObject(carouselImageCount)
                            val jsonImageData = jsonNode["node"] as JSONObject
                            val imageUrl = jsonImageData["display_url"].toString()
                            urls.add(imageUrl)
                        }
                    }else{
                        urls.add(thumbnailUrl)
                    }

                    activity!!.runOnUiThread {

                        comicData.totalUnlocked = newComicTotalUnlockedImagesEditText.text.toString().toInt()
                        comicData.instagramUrl = url

                        val builderUpload = AlertDialog.Builder(context)
                        builderUpload.setCancelable(false)
                        builderUpload.setView(R.layout.progress_dialog_loading)
                        dialogUpload = builderUpload.create()
                        dialogUpload.show()

                        comicData.urls = urls
                        comicData.price = "free"
                        comicData.category = "Comic"

                        comicData.title =
                            "comic/" + comicData.collection.toString() + "/" + comicData.artistName + "_" + Calendar.getInstance().timeInMillis

                        comicDoc.set(comicData).addOnCompleteListener {
                            dialogUpload.dismiss()
                            Toast.makeText(context, "Comic is Live!", Toast.LENGTH_SHORT).show()
                            val map = mapOf<String, Any>(
                                Pair("totalPosts", FieldValue.increment(1)),
                                Pair("posts", FieldValue.arrayUnion(comicData.id))
                            )
                            FirebaseFirestore.getInstance().collection("comicArtist")
                                .document(comicData.artistID!!)
                                .update(map)
                        }.addOnFailureListener {
                            dialogUpload.dismiss()
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                        }
                        dialogUpload.dismiss()

                    }

                }

            })

        }

        return view
    }

}