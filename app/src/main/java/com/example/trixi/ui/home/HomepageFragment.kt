package com.example.trixi.ui.home

//import com.example.trixi.apiService.RetrofitClient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.GetFromDbViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.PopUpCommentWindow
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*


class HomepageFragment : Fragment() {

    val model: GetFromDbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

//        val chatButton = view.findViewById(R.id.single_post_comment) as ImageButton
//        chatButton.setOnClickListener { view ->
//            val popupChat = PopUpChat()
//            val fm = fragmentManager
//            fm?.let {popupChat.show(fm, PopUpChat.TAG)}
//        }


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView(view)


    }


    private fun setupRecycleView(view: View) {


        if (PostToDb.loggedInUser != null) {
            val adapter = GroupAdapter<GroupieViewHolder>()
            adapter.clear()
            model.getFollowingsPostFromDb(PostToDb.loggedInUser!!.uid)
                .observe(viewLifecycleOwner) { posts ->
                    Log.d("uus", "total posts : ${posts.size}")
                    posts.forEach { post ->

                        Log.d("uus", "post Title : ${post.title!!}")
                        Log.d("uus", "post Description : ${post.description}")
                        model.getOneUserFromDb(post.ownerId).observe(viewLifecycleOwner
                        ) { postOwner ->
                            adapter.add(HomeItem(post, postOwner))
                        }
                    }
                }
            recyclerView_homepage.adapter = adapter

            adapter.setOnItemClickListener { item, view ->
                val homeItem = item as HomeItem
                val popUp = PopUpCommentWindow(homeItem.post.comments!!)
                val fm = fragmentManager
                if (fm != null) {
                    popUp.show(fm, PopUpCommentWindow.TAG)
                }
            }
        }


        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView_homepage);


    }

}

class HomeItem(val post: Post, val postOwner: User) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(RetrofitClient.BASE_URL + postOwner.imageUrl).transform(CropCircleTransformation()).fit().into(viewHolder.itemView.home_item_profileimg)

        viewHolder.itemView.home_item_profileName.text = postOwner.userName
        viewHolder.itemView.home_item_title.text = post.title
        viewHolder.itemView.home_item_description.text = post.description
        viewHolder.itemView.home_item_edit.isVisible = false
        viewHolder.itemView.home_item_chat_count.text = post.comments?.size.toString()
        viewHolder.itemView.home_item_like_count.text = post.likes?.size.toString()
        Picasso.get().load(RetrofitClient.BASE_URL + post.filePath).centerCrop().fit().into(viewHolder.itemView.home_item_media)

    }


    override fun getLayout(): Int {
        return R.layout.fragment_home_item;
    }

}

