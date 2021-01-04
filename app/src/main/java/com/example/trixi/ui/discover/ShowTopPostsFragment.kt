package com.example.trixi.ui.discover

//import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment() {
    private lateinit var model: TrixiViewModel
    //private lateinit var linearLayoutManager: LinearLayoutManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        setupDiscoverFragment()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
    (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun setupDiscoverFragment() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        var postList = model.getAllPosts()
        Log.d("post", "in the method setupDiscoverFragment")


        val mutablePosts : MutableLiveData<List<Post>?> = model.getAllPosts()
        val listOfPost = mutableListOf<Post>()
        listOfPost.addAll(mutablePosts.value!!)
        //list.addAll(newResults)
        //results.value = list



                Log.d("post", "in getting all posts")
                //Log.d("post toString", post.toString())

                if (listOfPost!!.isNotEmpty()) {
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                                media_grid_top_posts.layoutManager

                        media_grid_top_posts.adapter = DiscoverMediaGridAdapter(listOfPost)
                        //Picasso.get().load(RetrofitClient.BASE_URL).fit().into(image_top_post)
                        Log.d("post", listOfPost.toString())
                    }
                } else {
                    Log.d("post", "no posts")
                }


                Log.d("posts", postList.toString())



        }


}