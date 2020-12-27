package com.example.trixi.ui.home

import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.entities.Like
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.discover.ShowTopPostsFragment
import com.example.trixi.ui.fragments.PopUpCommentWindow
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

    companion object {
        private val db = PostToDb()

    }


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
        var numberOfLike: Int = post.likes!!.size
        viewHolder.itemView.home_item_like_count.text = numberOfLike.toString()

        handleLike(viewHolder, numberOfLike)
        handleClickOnComment(viewHolder)
        handleClickOnDiscovery(viewHolder)
        handleClickOnImgAndName(viewHolder)

    }

    private fun handleLike(viewHolder: GroupieViewHolder, numberOfLike: Int) {
        var numberOfLike1 = numberOfLike
        var likeHeart: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_like)

        var liked: Boolean = false

        post.likes?.forEach {
            if (it.userId == PostToDb.loggedInUser!!.uid) {
                liked = true
            }
        }
        if (liked) {
            likeHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            likeHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


        likeHeart.setOnClickListener {
            var like = Like(post.uid.toString(), PostToDb.loggedInUser?.uid.toString(), null)

            if (liked) {
                db.unlike(like)
                liked = false
                likeHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                numberOfLike1 -= 1
                viewHolder.itemView.home_item_like_count.text = numberOfLike1.toString()
            } else {
                db.like(like)
                liked = true
                likeHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
                numberOfLike1 += 1
                viewHolder.itemView.home_item_like_count.text = numberOfLike1.toString()
            }
        }
    }


    private fun handleClickOnImgAndName(viewHolder: GroupieViewHolder) {
        var profileName: TextView = viewHolder.itemView.findViewById(R.id.home_item_profileName)
        var profileImg: ImageView = viewHolder.itemView.findViewById(R.id.home_item_profileimg)

        profileImg.setOnClickListener {
            redirectToUser()
        }
        profileName.setOnClickListener {
            redirectToUser()
        }
    }

    private fun handleClickOnComment(viewHolder: GroupieViewHolder) {
        var commentIcon: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_chat)
        commentIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val reversedComments: List<Comment>? = post.comments;
                val popUp = PopUpCommentWindow(reversedComments)

                if (fm != null) {
                    popUp.show(fm, PopUpCommentWindow.TAG)
                }
            }
        })
    }

    private fun handleClickOnDiscovery(viewHolder: GroupieViewHolder) {
        var discoveryText: TextView = viewHolder.itemView.findViewById(R.id.home_item_discover)
        discoveryText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                fm.beginTransaction().replace(R.id.fragment_container, ShowTopPostsFragment())
                    .commit()

            }
        })

    }

    private fun redirectToUser() {
        val userProfileFragment = UserProfileFragment(post.owner)
        fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()

    }


    override fun getLayout(): Int {
        return R.layout.fragment_home_item;
    }


}


