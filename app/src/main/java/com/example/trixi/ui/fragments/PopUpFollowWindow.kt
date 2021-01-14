package com.example.trixi.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.User
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.profile.PetProfileFragment
import com.example.trixi.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.follow_row.view.*
import kotlinx.android.synthetic.main.fragment_follow_list.*



class PopUpFollowWindow(private val fm: FragmentManager, private val headerText: String, private val follow: ArrayList<User>?, private val followingPet: ArrayList<Pet>?) :
        DialogFragment() {

    private lateinit var model: TrixiViewModel
    private val adapterChat = GroupAdapter<GroupieViewHolder>()

    companion object {
        const val TAG = "popUpFollow"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_follow_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header_text.text = headerText
        setUpFollowView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setUpFollowView() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        follow!!.forEach { follow ->
            follow.uid?.let {
                model.getOneUser(it)?.observe(viewLifecycleOwner, { getFollowUser ->
                    adapterChat.add(FollowItem( this,fm, getFollowUser, null))
                })
            }
        }

        followingPet?.forEach { followingPet ->
            model.getOnePet(followingPet.uid)?.observe(viewLifecycleOwner, { getFollowingPet ->
                adapterChat.add(FollowItem(this, fm, null, getFollowingPet))
            })

        }

        recyclerView_popup_follow_list.adapter = adapterChat

    }
}

class FollowItem(private  val df: DialogFragment, private val fm: FragmentManager, private val followUser: User?, private val followingPet: Pet?) :
    Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(followUser == null){
            viewHolder.itemView.follow_user_name.text = followingPet!!.name
            Picasso.get().load(RetrofitClient.BASE_URL + followingPet.imageUrl)
                    .transform(CropCircleTransformation()).fit()
                    .into(viewHolder.itemView.follow_user_image)
        }
        if(followingPet == null){
            viewHolder.itemView.follow_user_name.text = followUser!!.userName
            Picasso.get().load(RetrofitClient.BASE_URL + followUser.imageUrl)
                    .transform(CropCircleTransformation()).fit()
                    .into(viewHolder.itemView.follow_user_image)
        }

        viewHolder.itemView.follow_user_name.setOnClickListener {
            df.dismiss()
            redirectToUserOrPet()
        }
        viewHolder.itemView.setOnClickListener {
            df.dismiss()
            redirectToUserOrPet()
        }

    }

    private fun redirectToUserOrPet() {
        if(followingPet == null){
            val userProfileFragment = UserProfileFragment(followUser)
            fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()
        }else {
            val petProfileFragment = PetProfileFragment(followingPet)
            fm.beginTransaction().replace(R.id.fragment_container, petProfileFragment).commit()
        }
    }


    override fun getLayout(): Int {
        return R.layout.follow_row
    }

}