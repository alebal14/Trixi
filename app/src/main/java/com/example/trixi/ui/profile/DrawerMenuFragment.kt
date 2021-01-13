package com.example.trixi.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import kotlinx.android.synthetic.main.fragment_drawer_menu.*


class DrawerMenuFragment : Fragment() {

    var toggleHamMenu: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drawer_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClickListeners()
    }

    private fun addClickListeners() {
        create_pet.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, PetRegister()).addToBackStack("creatPetFragment")!!.
                commit()
            }
        }

        edit_profile.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, EditProfileFragment())
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