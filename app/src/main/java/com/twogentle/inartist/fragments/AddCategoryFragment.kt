package com.twogentle.inartist.fragments

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
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
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.*
import com.twogentle.inartist.R
import com.twogentle.inartist.model.Category
import com.twogentle.inartist.model.Collection
import org.json.JSONObject
import java.io.File

class AddCategoryFragment : Fragment() {

    /*companion object {
        const val RESULT_BROWSE_IMAGE = 101

        //const val CLIENT_ID = "606b3ca2eff3250"
        const val AUTHORIZATION_URL =
            "https://api.imgur.com/oauth2/authorize?client_id=606b3ca2eff3250&response_type=token&state=APPLICATION_STATE"
        const val REDIRECT_URL = "com.twogentle.inartist://callback"
        val MEDIA_TYPE_PNG: MediaType = MediaType.parse("image/png")

        var accessToken = ""
        var refreshToken = ""
    }*/

    lateinit var categoryProgressBar: View

    lateinit var categoryData: Category


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_category, container, false)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_dialog)
        val dialog = builder.create()
        dialog.show()

        val categoryNameEditText =
            view.findViewById<TextInputEditText>(R.id.dialogCategoryNameEditText)
        val categoryUrlEditText =
            view.findViewById<TextInputEditText>(R.id.dialogCategoryUrlEditText)
        val addCategoryButton = view.findViewById<MaterialCardView>(R.id.categoryAddCardButton)
        categoryProgressBar = view.findViewById<View>(R.id.categoryProgressBar)
        //val browseButton = view.findViewById<MaterialButton>(R.id.categoryBrowseButton)
        //val imgurWebView = view.findViewById<WebView>(R.id.categoryAuthenticationWebView)
        val collectionSpinner = view.findViewById<Spinner>(R.id.dialogCategoryCollectionSpinner)
        //imgurWebView.loadUrl(AUTHORIZATION_URL)
        //imgurWebView.settings.javaScriptEnabled = true

        var isCollectionDone: Boolean

        val collectionData = Collection()
        categoryData = Category()

        val collectionArray = ArrayList<String>()
        FirebaseFirestore.getInstance().collection("collections").get()
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
                                collectionData.title = collectionArray[position]
                            }
                        }
                    isCollectionDone = true
                    if (isCollectionDone) {
                        dialog.dismiss()
                    }
                }
            }

        /*imgurWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                if (url!!.contains(REDIRECT_URL)) {
                    imgurWebView.visibility = View.GONE
                    splitUrl(url)
                } else {
                    view!!.loadUrl(url)
                }
                return true
            }
        }*/

        addCategoryButton.setOnClickListener {

            categoryProgressBar.visibility = View.VISIBLE

            val categoryName = categoryNameEditText.text.toString()
            val categoryUrl = categoryUrlEditText.text.toString()

            when {
                categoryUrl.isEmpty() -> {
                    categoryUrlEditText.error = "Error"
                    categoryProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                categoryName.isEmpty() -> {
                    categoryNameEditText.error = "Error"
                    categoryProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
            }

            categoryData.collection = collectionData.title
            categoryData.title = categoryName
            categoryData.imageUrl = categoryUrl

            FirebaseFirestore.getInstance().collection("categories").document()
                .set(categoryData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Category Added Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        categoryProgressBar.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Error : " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            categoryProgressBar.visibility = View.GONE

            /*val builderUpload = AlertDialog.Builder(context)
            builderUpload.setCancelable(false)
            builderUpload.setView(R.layout.progress_dialog_loading)
            dialogUpload = builderUpload.create()
            dialogUpload.show()


            if (isSelected && accessToken.isNotEmpty()) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    UploadToImgurTask().execute(getPathFromUriBelowOreo(context!!,filePath))
                }else{
                    UploadToImgurTask().execute(getPathFromUri(filePath))
                }

            }*/

        }

        /*browseButton.setOnClickListener {

            val intent = Intent()
            intent.type = "image*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Post Image"),
                RESULT_BROWSE_IMAGE
            )
        }*/

        return view
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_BROWSE_IMAGE && data != null && data.data != null) {

            filePath = data.data!!
            isSelected = true

            Glide.with(this).load(filePath).centerCrop().into(categoryImageView)
            categoryImageView.visibility = View.VISIBLE

        } else {
            Toast.makeText(context, "Error selecting image", Toast.LENGTH_SHORT).show()
            return
        }
    }*/

    /*private inner class UploadToImgurTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg p0: String?): Boolean {

            val uploadUrl = "https://api.imgur.com/3/image"

            val client = OkHttpClient()

            val requestBody = MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"title\""),
                    RequestBody.create(null, "Post Image")
                )
                .addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"image\""),
                    RequestBody.create(MEDIA_TYPE_PNG, File(p0[0]!!))
                )
                .build()

            val request = Request.Builder()
                .addHeader("Authorization", "Bearer $accessToken")
                .url(uploadUrl)
                .post(requestBody)
                .build()

            var isUploaded = false
            val response = client.newCall(request).execute()

            /*Handler().postDelayed({
                if (!isUploaded) {
                    Toast.makeText(context, "Upload timeout, Please try again.", Toast.LENGTH_LONG)
                        .show()
                    categoryProgressBar.visibility = View.GONE
                    dialogUpload.dismiss()
                }
            }, 10000)*/

            val stringResponse = response.body().string()
            val link = (JSONObject(stringResponse)["data"] as JSONObject)["link"].toString()
            if (link.isNotEmpty()) {
                isUploaded = true
                categoryData.imageUrl = link
            }



            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
                FirebaseFirestore.getInstance().collection("categories").document()
                    .set(categoryData)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Category Added Successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            categoryProgressBar.visibility = View.GONE
                            dialogUpload.dismiss()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Error : " + it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                categoryProgressBar.visibility = View.GONE
                dialogUpload.dismiss()
            }
        }
    }

    fun splitUrl(url: String) {
        Log.w("url", url)
        val outerSplit = url.split("#")[1].split("&")
        var index = 0

        for (s in outerSplit) {
            val innerSplit = s.split("=")
            when (index) {
                0 -> accessToken = innerSplit[1]
                3 -> refreshToken = innerSplit[1]
            }
            index += 1
        }

    }

    private fun getPathFromUri(uri: Uri): String {
        var filePath = ""
        val wholeId = DocumentsContract.getDocumentId(uri)
        val id = wholeId.split(":")[1]
        val columns = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = context!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, sel,
            arrayOf(id), null
        )
        val columnIndex = cursor!!.getColumnIndex(columns[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    private fun getPathFromUriBelowOreo(context: Context,uri: Uri): String {
        var uriIn = uri
        var selection = ""
        var selectionArgs = arrayOf<String>()
        if (DocumentsContract.isDocumentUri(context!!.applicationContext, uri)) {
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                isDownloadsDocument(uri) -> {
                    val id = DocumentsContract.getDocumentId(uri)
                    uriIn = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),id.toLong())
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    if("image" == type){
                        uriIn = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }
        if("content" == uri.scheme){
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uriIn,projection,selection,selectionArgs,null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if(cursor.moveToFirst()){
                return cursor.getString(column_index)
            }
        }else if("file" == uri.scheme){
            return uri.path!!
        }
        return ""
    }

    private fun isExternalStorageDocument(uri: Uri):Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri):Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri):Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }*/

}