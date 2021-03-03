package com.twogentle.inartist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.twogentle.inartist.MainActivity
import com.twogentle.inartist.R
import java.util.*

class SignInFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (auth.currentUser != null) {
            MainActivity.changeFragment(SelectOptionFragment(), activity!!.supportFragmentManager)
        }

        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        val emailSignInInputLayout =
            view.findViewById<TextInputLayout>(R.id.authSignInEmailInputLayout)
        val passwordSignInInputLayout =
            view.findViewById<TextInputLayout>(R.id.authSignInPasswordInputLayout)

        val emailSignInET = view.findViewById<TextInputEditText>(R.id.authSignInEmailEditText)
        val passwordSignInET =
            view.findViewById<TextInputEditText>(R.id.authSignInPasswordEditText)

        val progressBar = view.findViewById<View>(R.id.authSignInProgressBar)

        val signInButton =
            view.findViewById<MaterialCardView>(R.id.authSignInCardButton)

        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val email = emailSignInET.text.toString()
            val pass = passwordSignInET.text.toString()

            when {
                email.isEmpty() -> {
                    emailSignInInputLayout.error = "Error"
                    progressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                pass.isEmpty() -> {
                    passwordSignInInputLayout.error = "Error"
                    progressBar.visibility = View.GONE
                    return@setOnClickListener
                }
                pass.length < 8 -> {
                    passwordSignInInputLayout.error = "Password length should be > 6"
                    progressBar.visibility = View.GONE
                    return@setOnClickListener
                }
            }


            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressBar.visibility = View.GONE

                    val lastLogin = Calendar.getInstance().timeInMillis

                    FirebaseFirestore.getInstance().collection("users")
                        .document(it.result!!.user!!.uid)
                        .update("lastLogged", lastLogin)
                        .addOnSuccessListener {
                            MainActivity.changeFragment(
                                SelectOptionFragment(),
                                activity!!.supportFragmentManager
                            )
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                signInButton,
                                "Error getting in! Please try again",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Snackbar.make(
                        signInButton,
                        "Sign in failed! Please Try again later",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        return view
    }

}