package com.example.trixi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var model: TrixiViewModel
    val loggedInUser = PostToDb.loggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_bio.hint = if(loggedInUser?.bio.isNullOrEmpty()) "Edit bio " else loggedInUser?.bio

    }






}