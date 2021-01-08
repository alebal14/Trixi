package com.example.trixi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.profile.PetRegister
import kotlinx.android.synthetic.main.fragment_drawer_menu.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DrawerMenuFragment : Fragment() {

    var toggleHamMenu:Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_drawer_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        create_pet.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, PetRegister()).addToBackStack("creatPetFragment")!!.
                commit()
            }
        }

            logout.setOnClickListener {
                println("LOGOUT")
                val post = PostToDb()
                post.logOutUser(context)
            }
        }



}