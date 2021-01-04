package com.example.trixi.ui.home

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
import com.example.trixi.ui.profile.PetProfileFragment
import com.example.trixi.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_home_item.view.*


class HomeItem(
    val post: Post,
    private val fm: FragmentManager
) : Item<GroupieViewHolder>() {

    companion object {
        private val db = PostToDb()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        if(post.fileType.toString() == "image"){
            viewHolder.itemView.home_item_media.visibility= View.VISIBLE
            viewHolder.itemView.home_item_video.visibility= View.GONE
            Picasso.get().load(RetrofitClient.BASE_URL + post.filePath).centerCrop().fit()
                .into(viewHolder.itemView.home_item_media)
        }else{
            viewHolder.itemView.home_item_media.visibility= View.GONE
            viewHolder.itemView.home_item_video.visibility= View.VISIBLE
            viewHolder.itemView.home_item_video.setSource(RetrofitClient.BASE_URL + post.filePath.toString())

        }


//        Picasso.get().load(RetrofitClient.BASE_URL + (post.owner?.imageUrl ?: ))
//            .transform(CropCircleTransformation()).fit()
//            .into(viewHolder.itemView.home_item_profileimg)
        val profileImgHolder =viewHolder.itemView.home_item_profileimg
        Picasso.get().load(RetrofitClient.BASE_URL + (post.owner?.imageUrl ?: post.ownerIsPet?.imageUrl))
            .transform(CropCircleTransformation()).fit()
            .placeholder(R.drawable.sample)
            .error(R.drawable.sample)
            .centerCrop().into(profileImgHolder)


        viewHolder.itemView.home_item_profileName.text = post.owner?.userName ?: post.ownerIsPet?.name
        viewHolder.itemView.home_item_title.text = post.title
        viewHolder.itemView.home_item_description.text = post.description
        viewHolder.itemView.home_item_edit.isVisible = false
        val numberOfComments:Int = post.comments!!.size
        viewHolder.itemView.home_item_chat_count.text = numberOfComments.toString()
        val numberOfLike: Int = post.likes!!.size
        viewHolder.itemView.home_item_like_count.text = numberOfLike.toString()

        handleLike(viewHolder, numberOfLike)
        handleClickOnComment(viewHolder)
        handleClickOnDiscovery(viewHolder)
        handleClickOnImgAndName(viewHolder)

    }

    private fun handleLike(viewHolder: GroupieViewHolder, numberOfLike: Int) {
        var numberOfLike1 = numberOfLike
        val likeHeart: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_like)

        var liked = false

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
            val like = Like(post.uid.toString(), PostToDb.loggedInUser?.uid.toString(), null)

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
        val profileName: TextView = viewHolder.itemView.findViewById(R.id.home_item_profileName)
        val profileImg: ImageView = viewHolder.itemView.findViewById(R.id.home_item_profileimg)

        profileImg.setOnClickListener {
            redirectToUserOrPet()
        }
        profileName.setOnClickListener {
            redirectToUserOrPet()
        }
    }

    private fun redirectToUserOrPet() {
        if(post.owner !=null){
            val userProfileFragment = UserProfileFragment(post.owner)
            fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()
        }else {
            val petProfileFragment = PetProfileFragment(post.ownerIsPet)
            fm.beginTransaction().replace(R.id.fragment_container, petProfileFragment).commit()

        }

    }

    private fun handleClickOnComment(viewHolder: GroupieViewHolder) {
        val commentIcon: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_chat)
        commentIcon.setOnClickListener {
            val popUp = PopUpCommentWindow(post.comments, post.uid.toString(),viewHolder)
            popUp.show(fm, PopUpCommentWindow.TAG)
        }
    }

    private fun handleClickOnDiscovery(viewHolder: GroupieViewHolder) {
        val discoveryText: TextView = viewHolder.itemView.findViewById(R.id.home_item_discover)
        discoveryText.setOnClickListener {
            fm.beginTransaction().replace(R.id.fragment_container, ShowTopPostsFragment())
                .commit()
        }

    }


    override fun getLayout(): Int {
        return R.layout.fragment_home_item;
    }


}


