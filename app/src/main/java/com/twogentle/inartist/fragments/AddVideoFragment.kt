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

class AddVideoFragment : Fragment() {

    private lateinit var postData: Post
    private lateinit var postDoc: DocumentReference
    private lateinit var dialog : AlertDialog
    private lateinit var videoUrlEditText : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_video, container, false)

        val uploadButton = view.findViewById<MaterialCardView>(R.id.videoUploadButton)
         videoUrlEditText = view.findViewById(R.id.videoUrlEditText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
        dialog.show()

        var isArtistDone = false

        val artistSpinner = view.findViewById<Spinner>(R.id.postArtistSpinner)

        postData = Post()

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
                    if (isArtistDone) {
                        dialog.dismiss()
                    }
                }
            }

        uploadButton.setOnClickListener {
            when {
                videoUrlEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        context,
                        "Video Url Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            postData.datePosted = Calendar.getInstance().timeInMillis
            postData.date = Calendar.getInstance().timeInMillis
            postData.views = 0
            postData.saves = 0
            postData.shares = 0
            postData.views = 0
            postData.contentType = "video"
            postData.collection = "Video"
            postData.category = "Video"
            postData.userSaved = ArrayList()
            postData.random = 999999

            val firestore = FirebaseFirestore.getInstance()
            postDoc = firestore.collection("posts").document()
            postData.id = postDoc.id

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_dialog)
            dialog = builder.create()
            dialog.show()

            postData.title =
                "video/" + postData.artistName + "_" + Calendar.getInstance().timeInMillis

            if (videoUrlEditText.text.toString().contains("instagram")) {
                postData.instagramUrl = videoUrlEditText.text.toString().split("/?")[0]
                getInstaUrl(postData.instagramUrl!!)

            } else {

                if (videoUrlEditText.text.toString().contains(".gifv")) {
                    postData.url = videoUrlEditText.text.toString().split(".gifv")[0] + ".mp4"
                } else {
                    postData.url = videoUrlEditText.text.toString()
                }
                postData.thumbnail = postData.url!!.split(".mp4")[0] + ".jpg"
                postData.instagramUrl = ""

                postDoc.set(postData).addOnCompleteListener {
                    Toast.makeText(context, "Video is Live!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
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
                    Toast.makeText(context, "Error Getting Video", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onResponse(response: Response?) {
                val strResponse = response!!.body().string()
                val jsonContact = JSONObject(strResponse)
                val jsonQL = jsonContact["graphql"] as JSONObject
                val jsonMedia = jsonQL["shortcode_media"] as JSONObject
                val thumbnailUrl = jsonMedia["display_url"].toString()
                val videoUrl = jsonMedia["video_url"].toString()

                activity!!.runOnUiThread {
                    postData.url = videoUrl
                    postData.thumbnail = thumbnailUrl

                    postDoc.set(postData).addOnCompleteListener {
                        Toast.makeText(context, "Video is Live!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        videoUrlEditText.setText("")

                    }.addOnFailureListener {
                        Toast.makeText(context, "Error! ${it.message}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        videoUrlEditText.setText("")
                    }
                }

            }


        })
    }

}