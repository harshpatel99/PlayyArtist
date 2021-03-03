package com.twogentle.inartist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twogentle.inartist.fragments.AddPostFragment
import com.twogentle.inartist.fragments.SignInFragment

class MainActivity : AppCompatActivity() {

    companion object{
        fun changeFragment(fragment: Fragment,supportFragmentManager : FragmentManager) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, fragment)
            transaction.commit()
        }

        fun changeFragmentBack(fragment: Fragment,supportFragmentManager : FragmentManager) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeFragment(SignInFragment(),supportFragmentManager)

    }

}