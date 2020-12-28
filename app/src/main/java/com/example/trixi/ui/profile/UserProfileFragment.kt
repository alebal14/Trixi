package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.TrixiViewModel


class UserProfileFragment(val user: User?) : Fragment() {
    companion object {
        private val TAG = "profile"

    }

    private lateinit var model: TrixiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "username > ${user?.userName}")
        Log.d(TAG, "email: ${user?.email}")
        Log.d(TAG, "followings > ${user?.followingsUser?.size}")


        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        getPosts()
        getPets()

    }

    private fun getPosts() {
        user?.uid?.let {
            model.getPostsByOwner(it)?.observe(viewLifecycleOwner, Observer { posts ->
                Log.d(TAG, "size: posts : ${posts?.size}")
            })
        }
    }

    private fun getPets() {
        user?.uid?.let {
            model.getPetsByOwner(it)?.observe(viewLifecycleOwner, Observer { pets ->
                Log.d(TAG, "size: pets  : ${pets?.size}")
            })
        }
    }

}