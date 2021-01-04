package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.entities.Pet

class PetProfileFragment(val pet: Pet?) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        if (pet != null) {
            Log.d("petProfile", "petName ${pet.name}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        if (pet != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = pet.name
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}