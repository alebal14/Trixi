package com.example.trixi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.viewModels
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.follow_row.view.*
import kotlinx.android.synthetic.main.fragment_follow_list.*

class PopUpFollowWindow(private val follow: ArrayList<User>?, private val followingPet: ArrayList<Pet>?) :
        DialogFragment() {

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
        Toast.makeText(activity, "Click", Toast.LENGTH_SHORT).show()
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


        follow!!.forEach { follow ->
            adapterChat.add(FollowItem(follow, null))
        }

        followingPet?.forEach { followingPet ->
            adapterChat.add(FollowItem(null, followingPet))
        }

        recyclerView_popup_follow_list.adapter = adapterChat

    }
}





class FollowItem(private val followUser: User?, private val followingPet: Pet?) :
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

    }

    override fun getLayout(): Int {
        return R.layout.follow_row
    }

}