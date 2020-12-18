package com.example.trixi.ui.home

//import com.example.trixi.apiService.RetrofitClient

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.DataViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.EmptyHomeFragment
import com.example.trixi.ui.fragments.PopUpCommentWindow
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_item.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class HomepageFragment : Fragment() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    val model: DataViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)





//        PostToDb.loggedInUser?.uid?.let {
//            model.getFollowingsPostsData(it)
//                .observe(viewLifecycleOwner) { postsF ->
//                    Log.d("post", "following all posts : ${postsF.size}")
//                }
//        }

        model.getAllPostsData()
            .observe(viewLifecycleOwner) { postsA ->
                Log.d("post", " all posts : ${postsA.size}")
            }

        setupRecycleView()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Trixi")
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setupRecycleView() {


        val adapter = GroupAdapter<GroupieViewHolder>()
        val fm = fragmentManager
        adapter.clear()
        PostToDb.loggedInUser!!.uid?.let {
            model.getFollowingsPostFromDb(it).observe(viewLifecycleOwner) { posts ->
                if (posts.isEmpty()) {
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.fragment_container, EmptyHomeFragment())
                        commit()
                    }
                } else {
                    Log.d("post", "followings posts : ${posts.size}")
                    posts.forEach { post ->
                        model.getOneUserFromDb(post.ownerId)
                            .observe(viewLifecycleOwner) { postOwner ->
                                adapter.add(HomeItem(post, postOwner, fm!!))
                            }
                    }
                    recyclerView_homepage.adapter = adapter
                }

            }
        }


        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView_homepage);


    }


    class HomeItem(val post: Post, val postOwner: User, val fm: FragmentManager) :
        Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            Picasso.get().load(RetrofitClient.BASE_URL + postOwner.imageUrl)
                .transform(CropCircleTransformation()).fit()
                .into(viewHolder.itemView.home_item_profileimg)

            viewHolder.itemView.home_item_profileName.text = postOwner.userName
            viewHolder.itemView.home_item_title.text = post.title
            viewHolder.itemView.home_item_description.text = post.description
            viewHolder.itemView.home_item_edit.isVisible = false
            viewHolder.itemView.home_item_chat_count.text = post.comments?.size.toString()
            viewHolder.itemView.home_item_like_count.text = post.likes?.size.toString()
            Picasso.get().load(RetrofitClient.BASE_URL + post.filePath).centerCrop().fit()
                .into(viewHolder.itemView.home_item_media)

            var commentIcon: ImageButton = viewHolder.itemView.findViewById(R.id.home_item_chat)
            commentIcon.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val popUp = PopUpCommentWindow(post.comments)

                    if (fm != null) {
                        popUp.show(fm, PopUpCommentWindow.TAG)
                    }
                }

            })

            var discoveryText :TextView = viewHolder.itemView.findViewById(R.id.home_item_discover)
            discoveryText.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    fm.beginTransaction().replace(R.id.fragment_container, EmptyHomeFragment()).commit()

                }
            })

        }


        override fun getLayout(): Int {
            return R.layout.fragment_home_item;
        }

    }
}

