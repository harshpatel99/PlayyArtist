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
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.*
import com.twogentle.inartist.R
import com.twogentle.inartist.model.Collection
import org.json.JSONObject
import java.io.File

class AddCollectionFragment : Fragment() {

    lateinit var collectionData : Collection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_collection, container, false)

        val collectionNameEditText =
            view.findViewById<TextInputEditText>(R.id.dialogCollectionNameEditText)
        val collectionUrlEditText =
            view.findViewById<TextInputEditText>(R.id.dialogCollectionUrlEditText)
        val addCategoryButton = view.findViewById<MaterialCardView>(R.id.collectionAddCardButton)
        val collectionProgressBar = view.findViewById<View>(R.id.collectionProgressBar)

        collectionData = Collection()

        addCategoryButton.setOnClickListener {

            collectionProgressBar.visibility = View.VISIBLE

            val collectionName = collectionNameEditText.text.toString()
            val collectionUrl = collectionUrlEditText.text.toString()

            when {
                collectionName.isEmpty() -> {
                    collectionUrlEditText.error = "Error"
                    collectionProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                collectionName.isEmpty() -> {
                    collectionNameEditText.error = "Error"
                    collectionProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
            }

            collectionData.title = collectionName
            collectionData.imageUrl = collectionUrl

            FirebaseFirestore.getInstance().collection("collections").document()
                .set(collectionData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Collection Added Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Error : " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }

        return view
    }
}