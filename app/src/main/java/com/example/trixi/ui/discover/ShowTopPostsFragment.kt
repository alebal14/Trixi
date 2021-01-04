package com.example.trixi.ui.discover

//import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_top_liked_post_item.*
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment() {
    private lateinit var model: TrixiViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        getData()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
    (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }





    private fun getData()  {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        var postList = model.getAllPosts()


        media_grid_top_posts.apply {
            media_grid_top_posts.layoutManager =
                StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
            adapter = DiscoverMediaGridAdapter(postList)
            media_grid_top_posts.adapter = adapter
        }
        Picasso.get().load(RetrofitClient.BASE_URL).fit().into(image_top_post)

        Log.d("posts", postList.toString())

    }

}