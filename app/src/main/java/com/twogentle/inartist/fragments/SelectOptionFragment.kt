package com.twogentle.inartist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.twogentle.inartist.MainActivity
import com.twogentle.inartist.R
import com.twogentle.inartist.activities.DeletePostActivity
import com.twogentle.inartist.activities.PollActivity

class SelectOptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_option, container, false)

        val collectionOption = view.findViewById<MaterialCardView>(R.id.optionNewCollection)
        val categoryOption = view.findViewById<MaterialCardView>(R.id.optionNewCategory)
        val artistOption = view.findViewById<MaterialCardView>(R.id.optionNewArtist)
        val postOption = view.findViewById<MaterialCardView>(R.id.optionNewPost)
        val deletePostOption = view.findViewById<MaterialCardView>(R.id.optionDeletePost)
        val pollOption = view.findViewById<MaterialCardView>(R.id.optionPoll)
        val comicArtistOption = view.findViewById<MaterialCardView>(R.id.optionNewComicArtist)
        val comicOption = view.findViewById<MaterialCardView>(R.id.optionNewComic)
        val videoOption = view.findViewById<MaterialCardView>(R.id.optionNewVideo)

        collectionOption.setOnClickListener {

            MainActivity.changeFragmentBack(
                AddCollectionFragment(),
                activity!!.supportFragmentManager
            )

        }

        categoryOption.setOnClickListener {

            MainActivity.changeFragmentBack(
                AddCategoryFragment(),
                activity!!.supportFragmentManager
            )

        }

        artistOption.setOnClickListener {

            MainActivity.changeFragmentBack(
                AddArtistFragment(),
                activity!!.supportFragmentManager
            )

        }

        comicArtistOption.setOnClickListener {
            MainActivity.changeFragmentBack(AddComicArtistFragment(), activity!!.supportFragmentManager)
        }

        comicOption.setOnClickListener {
            MainActivity.changeFragmentBack(AddComicFragment(), activity!!.supportFragmentManager)
        }

        postOption.setOnClickListener {
            MainActivity.changeFragmentBack(AddPostFragment(), activity!!.supportFragmentManager)
        }

        videoOption.setOnClickListener {
            MainActivity.changeFragmentBack(AddVideoFragment(), activity!!.supportFragmentManager)
        }

        deletePostOption.setOnClickListener {
            startActivity(Intent(context, DeletePostActivity::class.java))
        }

        pollOption.setOnClickListener {
            startActivity(Intent(context, PollActivity::class.java))
        }

        return view
    }
}