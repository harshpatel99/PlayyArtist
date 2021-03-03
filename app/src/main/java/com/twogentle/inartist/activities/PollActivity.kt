package com.twogentle.inartist.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
import com.twogentle.inartist.R
import com.twogentle.inartist.fragments.AddCategoryFragment
import com.twogentle.inartist.model.PointsHistory
import com.twogentle.inartist.model.Poll
import com.twogentle.inartist.model.Post
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class PollActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_A = 101
        const val REQUEST_CODE_B = 102

        const val POST_TYPE_POLL = 2
    }

    private lateinit var aImage: ImageView
    private lateinit var bImage: ImageView

    private var isAImageSelected = false
    private var isBImageSelected = false

    private lateinit var aData: Post
    private lateinit var bData: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll)

        aImage = findViewById(R.id.pollAImageView)
        bImage = findViewById(R.id.pollBImageView)
        val activate = findViewById<MaterialCardView>(R.id.activatePollCard)

        aImage.setOnClickListener {
            startActivityForResult(Intent(this, SelectPostActivity::class.java), REQUEST_CODE_A)
        }

        bImage.setOnClickListener {
            startActivityForResult(Intent(this, SelectPostActivity::class.java), REQUEST_CODE_B)
        }

        activate.setOnClickListener {
            if (!isAImageSelected) {
                Snackbar.make(activate, "Please Select First Image!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isBImageSelected) {
                Snackbar.make(activate, "Please Select Second Image!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_dialog)
            val dialog = builder.create()
            dialog.show()

            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("polls").document("active").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val poll = task.result!!.toObject(Poll::class.java) as Poll

                        val voteA = poll.voteA!!
                        val voteB = poll.voteB!!
                        val aPer = (voteA / (voteA + voteB).toDouble() * 100).toInt()
                        val bPer = 100 - aPer

                        when {
                            aPer > bPer -> {
                                for (user in poll.userA!!) {
                                    if (user.isNotEmpty()) {
                                        firestore.collection("users").document(user)
                                            .update("artPoints", FieldValue.increment(100))
                                        firestore.collection("users").document(user)
                                            .collection("pointsHistory").document()
                                            .set(
                                                PointsHistory(
                                                    PointsHistory.TYPE_POLL_WIN,
                                                    Calendar.getInstance().timeInMillis,
                                                    "+100"
                                                )
                                            )
                                    }
                                }
                            }
                            aPer < bPer -> {
                                for (user in poll.userB!!) {
                                    if (user.isNotEmpty()) {
                                        firestore.collection("users").document(user)
                                            .update("artPoints", FieldValue.increment(100))
                                        firestore.collection("users").document(user)
                                            .collection("pointsHistory").document()
                                            .set(
                                                PointsHistory(
                                                    PointsHistory.TYPE_POLL_WIN,
                                                    Calendar.getInstance().timeInMillis,
                                                    "+100"
                                                )
                                            )
                                    }
                                }
                            }
                            else -> {
                                for (user in poll.userA!!) {
                                    if (user.isNotEmpty()) {
                                        firestore.collection("users").document(user)
                                            .update("artPoints", FieldValue.increment(50))
                                        firestore.collection("users").document(user)
                                            .collection("pointsHistory").document()
                                            .set(
                                                PointsHistory(
                                                    PointsHistory.TYPE_POLL_TIE,
                                                    Calendar.getInstance().timeInMillis,
                                                    "+50"
                                                )
                                            )
                                    }
                                }
                                for (user in poll.userB!!) {
                                    if (user.isNotEmpty()) {
                                        firestore.collection("users").document(user)
                                            .update("artPoints", FieldValue.increment(50))
                                        firestore.collection("users").document(user)
                                            .collection("pointsHistory").document()
                                            .set(
                                                PointsHistory(
                                                    PointsHistory.TYPE_POLL_TIE,
                                                    Calendar.getInstance().timeInMillis,
                                                    "+50"
                                                )
                                            )
                                    }
                                }
                            }
                        }

                        firestore.collection("polls").document(poll.pollId!!)
                            .set(poll)
                            .addOnCompleteListener { innerTask ->
                                if (innerTask.isSuccessful) {
                                    val fireId = firestore.collection("polls")
                                        .document().id
                                    val newPoll = Poll(
                                        fireId,
                                        aData.id,
                                        bData.id,
                                        aData.url,
                                        bData.url,
                                        0,
                                        0,
                                        ArrayList(),
                                        ArrayList(),
                                        Calendar.getInstance().timeInMillis,
                                        POST_TYPE_POLL
                                    )
                                    firestore.collection("polls").document("active").set(newPoll)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                dialog.dismiss()
                                                Snackbar.make(
                                                    activate,
                                                    "New Poll is Live!",
                                                    Snackbar.LENGTH_SHORT
                                                ).show()
                                                finish()
                                            } else {
                                                dialog.dismiss()
                                                Snackbar.make(
                                                    activate,
                                                    "Error!",
                                                    Snackbar.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } else {
                                    dialog.dismiss()
                                    Snackbar.make(activate, "Error!", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        dialog.dismiss()
                        Snackbar.make(activate, "Error!", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_A -> {
                if (resultCode == Activity.RESULT_OK) {
                    aData = data!!.extras!!["post"] as Post
                    Glide.with(this)
                        .load(aData.url!!)
                        .centerCrop().placeholder(R.drawable.ic_round_blur_on_24).into(aImage)
                    isAImageSelected = true
                } else {
                    Toast.makeText(this, "Please Select Post!", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE_B -> {
                if (resultCode == Activity.RESULT_OK) {
                    bData = data!!.extras!!["post"] as Post
                    Glide.with(this)
                        .load(bData.url!!)
                        .centerCrop().placeholder(R.drawable.ic_round_blur_on_24).into(bImage)
                    isBImageSelected = true
                } else {
                    Toast.makeText(this, "Please Select Post!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}