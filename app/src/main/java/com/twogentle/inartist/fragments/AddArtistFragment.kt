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
import com.twogentle.inartist.model.Artist

class AddArtistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_artist, container, false)

        val artistNameEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistNameEditText)
        val artistUsernameUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistUsernameEditText)
        val artistBioUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistBioEditText)
        val artistWebsiteUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistWebsiteEditText)
        val artistInstaUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistInstaEditText)
        val artistFacebookUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistFacebookEditText)
        val artistTwitterUrlEditText = view.findViewById<TextInputEditText>(R.id.dialogArtistTwitterEditText)
        val addArtistButton = view.findViewById<MaterialCardView>(R.id.artistAddCardButton)
        val artistProgressBar = view.findViewById<View>(R.id.artistProgressBar)

        addArtistButton.setOnClickListener {

            artistProgressBar.visibility = View.VISIBLE

            val artistName = artistNameEditText.text.toString()
            val artistInstaUrl = artistInstaUrlEditText.text.toString()
            val artistUsername = artistUsernameUrlEditText.text.toString()
            val artistBio = artistBioUrlEditText.text.toString()
            var artistWebsite = artistWebsiteUrlEditText.text.toString()
            var artistFacebook = artistFacebookUrlEditText.text.toString()
            var artistTwitter = artistTwitterUrlEditText.text.toString()

            when {
                artistName.isEmpty() -> {
                    artistNameEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                artistInstaUrl.isEmpty() -> {
                    artistInstaUrlEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                artistUsername.isEmpty() -> {
                    artistUsernameUrlEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                artistBio.isEmpty() -> {
                    artistBioUrlEditText.error = "Error"
                    artistProgressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                artistFacebook.isEmpty() -> {
                    artistFacebook = ""
                }
                artistTwitter.isEmpty() -> {
                    artistTwitter = ""
                }
                artistWebsite.isEmpty() -> {
                    artistWebsite = ""
                }
            }

            val hashMap = HashMap<String, String>()
            hashMap["facebook"] = artistFacebook
            hashMap["twitter"] = artistTwitter
            hashMap["instagram"] = artistInstaUrl
            hashMap["website"] = artistWebsite
            val doc = FirebaseFirestore.getInstance().collection("artist").document()
            val artist = Artist(doc.id, artistName,artistUsername,artistBio,"", hashMap, 0,"")
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