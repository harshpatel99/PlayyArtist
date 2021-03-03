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

class AddComicFragmentBackUp : Fragment() {

    /****private lateinit var filePath: Uri

    //private lateinit var postImageView: ImageView
    private lateinit var comicData: Comic
    private lateinit var comicDoc: DocumentReference
    private lateinit var dialogUpload: AlertDialog

    lateinit var urls: ArrayList<String>

    lateinit var selectImageView1: TextInputEditText
    lateinit var selectImageView2: TextInputEditText
    lateinit var selectImageView3: TextInputEditText
    lateinit var selectImageView4: TextInputEditText
    lateinit var selectImageView5: TextInputEditText
    lateinit var selectImageView6: TextInputEditText
    lateinit var selectImageView7: TextInputEditText
    lateinit var selectImageView8: TextInputEditText
    lateinit var selectImageView9: TextInputEditText
    lateinit var selectImageView10: TextInputEditText
    lateinit var selectImageView11: TextInputEditText
    lateinit var selectImageView12: TextInputEditText
    lateinit var selectImageView13: TextInputEditText
    lateinit var selectImageView14: TextInputEditText
    lateinit var selectImageView15: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_comic, container, false)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        val dialog = builder.create()
        dialog.show()

        /*var isPostTypeDone = false
        var isCategoryDone = false*/
        var isCollectionDone = false
        var isArtistDone = false

        val newComicTotalImagesEditText =
            view.findViewById<TextInputEditText>(R.id.newComicTotalImagesEditText)
        val newComicTotalUnlockedImagesEditText =
            view.findViewById<TextInputEditText>(R.id.newComicTotalUnlockedImagesEditText)
        val artistSpinner = view.findViewById<Spinner>(R.id.newComicArtistSpinner)
        val collectionSpinner = view.findViewById<Spinner>(R.id.newComicCollectionSpinner)
        val uploadButton = view.findViewById<MaterialCardView>(R.id.newComicUploadCard)


        val selectImageLayout1 = view.findViewById<View>(R.id.newComicSelectImageLayout1)
        val selectCountTextView1 =
            selectImageLayout1.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView1 = selectImageLayout1.findViewById(R.id.comicSelectImageView)

        val selectImageLayout2 = view.findViewById<View>(R.id.newComicSelectImageLayout2)
        val selectCountTextView2 =
            selectImageLayout2.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView2 = selectImageLayout2.findViewById(R.id.comicSelectImageView)

        val selectImageLayout3 = view.findViewById<View>(R.id.newComicSelectImageLayout3)
        val selectCountTextView3 =
            selectImageLayout3.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView3 = selectImageLayout3.findViewById(R.id.comicSelectImageView)

        val selectImageLayout4 = view.findViewById<View>(R.id.newComicSelectImageLayout4)
        val selectCountTextView4 =
            selectImageLayout4.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView4 = selectImageLayout4.findViewById(R.id.comicSelectImageView)

        val selectImageLayout5 = view.findViewById<View>(R.id.newComicSelectImageLayout5)
        val selectCountTextView5 =
            selectImageLayout5.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView5 = selectImageLayout5.findViewById(R.id.comicSelectImageView)

        val selectImageLayout6 = view.findViewById<View>(R.id.newComicSelectImageLayout6)
        val selectCountTextView6 =
            selectImageLayout6.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView6 = selectImageLayout6.findViewById(R.id.comicSelectImageView)

        val selectImageLayout7 = view.findViewById<View>(R.id.newComicSelectImageLayout7)
        val selectCountTextView7 =
            selectImageLayout7.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView7 = selectImageLayout7.findViewById(R.id.comicSelectImageView)

        val selectImageLayout8 = view.findViewById<View>(R.id.newComicSelectImageLayout8)
        val selectCountTextView8 =
            selectImageLayout8.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView8 = selectImageLayout8.findViewById(R.id.comicSelectImageView)

        val selectImageLayout9 = view.findViewById<View>(R.id.newComicSelectImageLayout9)
        val selectCountTextView9 =
            selectImageLayout9.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView9 = selectImageLayout9.findViewById(R.id.comicSelectImageView)

        val selectImageLayout10 = view.findViewById<View>(R.id.newComicSelectImageLayout10)
        val selectCountTextView10 =
            selectImageLayout10.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView10 = selectImageLayout10.findViewById(R.id.comicSelectImageView)

        val selectImageLayout11 = view.findViewById<View>(R.id.newComicSelectImageLayout11)
        val selectCountTextView11 =
            selectImageLayout11.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView11 = selectImageLayout11.findViewById(R.id.comicSelectImageView)

        val selectImageLayout12 = view.findViewById<View>(R.id.newComicSelectImageLayout12)
        val selectCountTextView12 =
            selectImageLayout12.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView12 = selectImageLayout12.findViewById(R.id.comicSelectImageView)

        val selectImageLayout13 = view.findViewById<View>(R.id.newComicSelectImageLayout13)
        val selectCountTextView13 =
            selectImageLayout13.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView13 = selectImageLayout13.findViewById(R.id.comicSelectImageView)

        val selectImageLayout14 = view.findViewById<View>(R.id.newComicSelectImageLayout14)
        val selectCountTextView14 =
            selectImageLayout14.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView14 = selectImageLayout14.findViewById(R.id.comicSelectImageView)

        val selectImageLayout15 = view.findViewById<View>(R.id.newComicSelectImageLayout15)
        val selectCountTextView15 =
            selectImageLayout15.findViewById<TextInputLayout>(R.id.comicSelectImageCountTextView)
        selectImageView15 = selectImageLayout15.findViewById(R.id.comicSelectImageView)

        selectCountTextView1.hint = "Comic Url 1"
        selectCountTextView2.hint = "Comic Url 2"
        selectCountTextView3.hint = "Comic Url 3"
        selectCountTextView4.hint = "Comic Url 4"
        selectCountTextView5.hint = "Comic Url 5"
        selectCountTextView6.hint = "Comic Url 6"
        selectCountTextView7.hint = "Comic Url 7"
        selectCountTextView8.hint = "Comic Url 8"
        selectCountTextView9.hint = "Comic Url 9"
        selectCountTextView10.hint = "Comic Url 10"
        selectCountTextView11.hint = "Comic Url 11"
        selectCountTextView12.hint = "Comic Url 12"
        selectCountTextView13.hint = "Comic Url 13"
        selectCountTextView14.hint = "Comic Url 14"
        selectCountTextView15.hint = "Comic Url 15"

        urls = ArrayList()

        newComicTotalImagesEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    p0.isNullOrEmpty() -> {
                        Toast.makeText(context, "Required", Toast.LENGTH_SHORT).show()
                    }
                    p0.toString().toInt() > 15 -> {
                        Toast.makeText(context, "Max 15 Pictures", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        if (p0.toString().toInt() >= 1) {
                            selectImageLayout1.visibility = View.VISIBLE
                        }else{
                            selectImageLayout1.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 2) {
                            selectImageLayout2.visibility = View.VISIBLE
                        } else{
                            selectImageLayout2.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 3) {
                            selectImageLayout3.visibility = View.VISIBLE
                        }else{
                            selectImageLayout3.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 4) {
                            selectImageLayout4.visibility = View.VISIBLE
                        }else{
                            selectImageLayout4.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 5) {
                            selectImageLayout5.visibility = View.VISIBLE
                        }else{
                            selectImageLayout5.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 6) {
                            selectImageLayout6.visibility = View.VISIBLE
                        }else{
                            selectImageLayout6.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 7) {
                            selectImageLayout7.visibility = View.VISIBLE
                        }else{
                            selectImageLayout7.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 8) {
                            selectImageLayout8.visibility = View.VISIBLE
                        }else{
                            selectImageLayout8.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 9) {
                            selectImageLayout9.visibility = View.VISIBLE
                        }else{
                            selectImageLayout9.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 10) {
                            selectImageLayout10.visibility = View.VISIBLE
                        }else{
                            selectImageLayout10.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 11) {
                            selectImageLayout11.visibility = View.VISIBLE
                        }else{
                            selectImageLayout11.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 12) {
                            selectImageLayout12.visibility = View.VISIBLE
                        }else{
                            selectImageLayout12.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 13) {
                            selectImageLayout13.visibility = View.VISIBLE
                        }else{
                            selectImageLayout13.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 14) {
                            selectImageLayout14.visibility = View.VISIBLE
                        }else{
                            selectImageLayout14.visibility = View.GONE
                        }
                        if (p0.toString().toInt() >= 15) {
                            selectImageLayout15.visibility = View.VISIBLE
                        }else{
                            selectImageLayout15.visibility = View.GONE
                        }

                    }
                }
            }
        })

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


        comicDoc = firestore.collection("comics").document()
        comicData.id = comicDoc.id

        uploadButton.setOnClickListener {

            if(newComicTotalImagesEditText.toString().isEmpty()){
                Toast.makeText(
                    context,
                    "Please, select number of the comic images.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val totalImages = newComicTotalImagesEditText.text.toString().toInt()

            if(totalImages == 0){
                Toast.makeText(
                    context,
                    "Please, select number of the comic images.",
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

            if (newComicTotalUnlockedImagesEditText.text.toString().toInt() > newComicTotalImagesEditText.text.toString().toInt()){
                Toast.makeText(
                    context,
                    "Unlocked Images cant be greater.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(totalImages >= 1){
                if(selectImageView1.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(0,selectImageView1.text.toString())
                }
            }

            if(totalImages >= 2){
                if(selectImageView2.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(1,selectImageView2.text.toString())
                }
            }

            if(totalImages >= 3){
                if(selectImageView3.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(2,selectImageView3.text.toString())
                }
            }

            if(totalImages >= 4){
                if(selectImageView2.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(3,selectImageView4.text.toString())
                }
            }

            if(totalImages >= 5){
                if(selectImageView5.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(4,selectImageView5.text.toString())
                }
            }

            if(totalImages >= 6){
                if(selectImageView6.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(5,selectImageView6.text.toString())
                }
            }

            if(totalImages >= 7){
                if(selectImageView7.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(6, selectImageView7.text.toString())
                }
            }

            if(totalImages >= 8){
                if(selectImageView8.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(7,selectImageView8.text.toString())
                }
            }

            if(totalImages >= 9){
                if(selectImageView9.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(8,selectImageView9.text.toString())
                }
            }

            if(totalImages >= 10){
                if(selectImageView10.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(9,selectImageView10.text.toString())
                }
            }

            if(totalImages >= 11){
                if(selectImageView11.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(10,selectImageView11.text.toString())
                }
            }

            if(totalImages >= 12){
                if(selectImageView12.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(11,selectImageView12.text.toString())
                }
            }

            if(totalImages >= 13){
                if(selectImageView13.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(12,selectImageView13.text.toString())
                }
            }

            if(totalImages >= 14){
                if(selectImageView14.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(13,selectImageView14.text.toString())
                }
            }
            if(totalImages >= 15){
                if(selectImageView15.text.toString().isEmpty()){
                    Toast.makeText(
                        context,
                        "Required.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }else{
                    urls.add(14,selectImageView15.text.toString())
                }
            }




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
                    }


                    val jsonOwner = jsonMedia["owner"] as JSONObject
                    val username = jsonOwner["username"].toString()
                    val instagramUrl = "https://www.instagram.com/$username"

                    activity!!.runOnUiThread {

                        comicData.totalUnlocked = newComicTotalUnlockedImagesEditText.text.toString().toInt()

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
    }***/
}