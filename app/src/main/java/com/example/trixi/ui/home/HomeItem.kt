package com.example.trixi.ui.home

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.ui.discover.ShowTopPostsFragment
import com.example.trixi.ui.fragments.PopUpCommentWindow
import com.example.trixi.ui.fragments.PopUpLikeWindow
import com.example.trixi.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_home_item.view.*


class HomeItem(
    val post: Post,
    val fm: FragmentManager
) : Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(RetrofitClient.BASE_URL + post.filePath).centerCrop().fit()
            .into(viewHolder.itemView.home_item_media)


        Picasso.get().load(RetrofitClient.BASE_URL + post.owner!!.imageUrl)
            .transform(CropCircleTransformation()).fit()
            .into(viewHolder.itemView.home_item_profileimg)

        viewHolder.itemView.home_item_profileName.text = post.owner!!.userName
        viewHolder.itemView.home_item_title.text = post.title
        viewHolder.itemView.home_item_description.text = post.description
        viewHolder.itemView.home_item_edit.isVisible = false
        viewHolder.itemView.home_item_chat_count.text = post.comments?.size.toString()
        viewHolder.itemView.home_item_like_count.text = post.likes?.size.toString()

        Log.d("home"," likes ${post.likes}")


        var commentIcon: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_chat)
        commentIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val popUp = PopUpCommentWindow(post.comments)

                if (fm != null) {
                    popUp.show(fm, PopUpCommentWindow.TAG)
                }
            }
        })


//        var likeHeart:ImageButton = viewHolder.itemView.findViewById(R.id.home_item_like)
//        likeHeart.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p1: View?) {
//                val popUp = PopUpLikeWindow(post.likes)
//
//                if (fm != null) {
//                    popUp.show(fm, PopUpLikeWindow.TAG)
//                }
//            }
//        })

        var profileName :TextView = viewHolder.itemView.findViewById(R.id.home_item_profileName)
        var profileImg:ImageView = viewHolder.itemView.findViewById(R.id.home_item_profileimg)

        profileImg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("userId", post.owner)
                val userProfileFragment = UserProfileFragment()
                userProfileFragment.arguments = bundle
                fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()
            }
        })



        var discoveryText: TextView = viewHolder.itemView.findViewById(R.id.home_item_discover)
        discoveryText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                fm.beginTransaction().replace(R.id.fragment_container, ShowTopPostsFragment())
                    .commit()

            }
        })

    }


    override fun getLayout(): Int {
        return R.layout.fragment_home_item;
    }


}


