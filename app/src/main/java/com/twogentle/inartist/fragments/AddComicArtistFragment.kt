package com.twogentle.inartist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.twogentle.inartist.R
import com.twogentle.inartist.model.ComicArtist

class AddComicArtistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_artist, container, false)

        val artistNameEditText =
            view.findViewById<TextInputEditText>(R.id.dialogArtistNameEditText)
        val artistInstaUrlEditText =
            view.findViewById<TextInputEditText>(R.id.dialogArtistInstaEditText)
        val addArtistButton = view.findViewById<MaterialCardView>(R.id.artistAddCardButton)
        val artistProgressBar = view.findViewById<View>(R.id.artistProgressBar)

        addArtistButton.setOnClickListener {

            artistProgressBar.visibility = View.VISIBLE

            val artistName = artistNameEditText.text.toString()
            val artistUrl = artistInstaUrlEditText.text.toString()

            when {
                artistName.isEmpty() -> {
                    artistNameEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                artistUrl.isEmpty() -> {
                    artistInstaUrlEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
            }

            val hashMap = HashMap<String, String>()
            hashMap["facebook"] = ""
            hashMap["instagram"] = artistUrl
            hashMap["twitter"] = ""
            val doc = FirebaseFirestore.getInstance().collection("comicArtist").document()
            val artist = ComicArtist(doc.id, artistName, hashMap, 0)
            doc.set(artist)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Artist Added Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        artistProgressBar.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Error : " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    artistProgressBar.visibility = View.GONE
                }

        }

        return view
    }
}