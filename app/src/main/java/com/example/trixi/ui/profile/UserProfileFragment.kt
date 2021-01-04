package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "username > ${user?.userName}")
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        getPosts()
        getPets()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        if (user != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = user.userName
        }
        super.onCreateOptionsMenu(menu, inflater)
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