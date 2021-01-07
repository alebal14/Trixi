package com.example.trixi.ui.explore

//import androidx.fragment.app.viewModels
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.PetType
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*


class ShowTopPostsFragment : Fragment() {
    private lateinit var model: TrixiViewModel
    //private lateinit var linearLayoutManager: LinearLayoutManager
    var  mContext : Context? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onViewCreated(view, savedInstanceState)
        mContext = context
        allPostsToAdapter()
        searchToAdapter()
        populateCatSpinner()

        search_bar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                search_bar.setIconified(false)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Explorer"
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun allPostsToAdapter(){
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->
            Log.d("post_size_f", post?.size.toString())

            media_grid_top_posts.apply {
                media_grid_top_posts.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                media_grid_top_posts.adapter = ExploreMediaGridAdapter(post as ArrayList<Post>)
            }
        })
    }

    private fun searchToAdapter() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    model.getPostBySearching(newText!!)
                        .observe(viewLifecycleOwner, Observer { post ->

                            media_grid_top_posts.apply {
                                media_grid_top_posts.layoutManager =
                                    StaggeredGridLayoutManager(
                                        2,
                                        StaggeredGridLayoutManager.VERTICAL
                                    )
                                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                                media_grid_top_posts.adapter =
                                    ExploreMediaGridAdapter(post as ArrayList<Post>)
                            }
                        })
                    SearchClickx()
                }
                return true
            }
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun SearchClickx(){
    val closeButton: View? = search_bar.findViewById(androidx.appcompat.R.id.search_close_btn)
    val clearText: EditText? = search_bar.findViewById(androidx.appcompat.R.id.search_src_text)

    val mSearch: SearchView? = search_bar.findViewById(androidx.appcompat.R.id.search_bar)

        closeButton?.setOnClickListener {
            println("ONCLICK!!")
            mSearch!!.setQuery("", false)
            mSearch!!.onActionViewCollapsed()

            //clearText!!.setText("")
            allPostsToAdapter()
        }

    }

    private fun populateCatSpinner(){
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        var petTypeDefault = (PetType("0", "", "PetType"))

        model.getPetType()?.observe(viewLifecycleOwner, Observer { petType ->
            val spinnerAdapter = ArrayAdapter<PetType>(
                mContext!!,
                android.R.layout.simple_spinner_item, petType
                )

            spinnerAdapter.sort(compareBy{it.name})
            spinnerAdapter.insert(petTypeDefault, 0)
            cat_spinner.adapter = spinnerAdapter
        })
    }

    private fun selectItemInSpinner(){
        cat_spinner.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //get all post by petType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }



}