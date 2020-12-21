package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.entities.User


class UserProfileFragment : Fragment(){
    var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View= inflater.inflate(R.layout.fragment_profile, container, false)

        val b: Bundle? = this.arguments
        user = b?.getParcelable<User>("userId")
        Log.d("profile","${user?.imageUrl}")
        return v;

    }

}