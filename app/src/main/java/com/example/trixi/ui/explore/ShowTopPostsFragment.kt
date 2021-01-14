package com.example.trixi.ui.explore

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.trixi.R
import com.example.trixi.entities.PetType
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.post.SinglePostFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*
import kotlinx.android.synthetic.main.fragment_top_liked_posts.pullToRefresh
import kotlinx.android.synthetic.main.fragment_upload.*


class ShowTopPostsFragment : Fragment(), View.OnClickListener {
    private lateinit var model: TrixiViewModel
    var  mContext : Context? = null
    var page = 1
    var limit = 20
    var scrollToLoad =  true

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
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        cat_spinner.setVisibility(View.GONE)



        cat_all.setOnClickListener(this)
        cat_training.setOnClickListener(this)
        cat_tricks.setOnClickListener(this)
        cat_obedience.setOnClickListener(this)
        cat_feeding.setOnClickListener(this)
        cat_cute.setOnClickListener(this)
        cat_other.setOnClickListener(this)
        cat_spinner_start.setOnClickListener(this)

        allPostsToAdapter(null)
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Explore"
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun ScrollToLoad(cat: Int?) {
        animationViewLoadingSpinner.visibility = View.GONE;
        top_scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {





                if (scrollToLoad && scrollY == v!!.getChildAt(0).measuredHeight - v!!.measuredHeight) {
                    page++
                    allPostsToAdapter(cat)
                }
            }
        })

        if(page == 1){
            pullToRefresh.isEnabled = false
            pullToRefresh.isRefreshing = false
        } else {
            pullToRefresh.isEnabled = true
        }
       
        pullToRefresh.setOnRefreshListener {
            if (page > 1) {
                page--
                allPostsToAdapter(cat)
            }
            if (pullToRefresh.isRefreshing) {
                pullToRefresh.isRefreshing = false;
            }
        }
    }

    private fun allPostsToAdapter(cat: Int?) {
         if (cat == R.id.cat_training || cat == R.id.cat_other || cat == R.id.cat_tricks || cat == R.id.cat_obedience || cat == R.id.cat_feeding || cat == R.id.cat_cute){
             scrollToLoad = true
            limit = 50
                model.getAllPostsWithQuery(page, limit)
                    ?.observe(viewLifecycleOwner, Observer { post ->
                        var finalPost = post
                        if (post != null) {
                            if (cat == R.id.cat_other) {
                                finalPost =
                                    post!!.filter { it.categoryName!!.contains("Other") }.map { it }
                                ScrollToLoad(R.id.cat_other)
                            }
                            if (cat == R.id.cat_tricks) {
                                finalPost =
                                    post!!.filter { it.categoryName!!.contains("Tricks") }
                                        .map { it }
                                ScrollToLoad(R.id.cat_training)
                            }
                            if (cat == R.id.cat_obedience) {
                                finalPost =
                                    post!!.filter { it.categoryName!!.contains("Obedience") }
                                        .map { it }
                                ScrollToLoad(R.id.cat_training)
                            }
                            if (cat == R.id.cat_feeding) {
                                finalPost =
                                    post!!.filter { it.categoryName!!.contains("Feeding") }
                                        .map { it }
                                ScrollToLoad(R.id.cat_training)
                            }
                            if (cat == R.id.cat_cute) {
                                finalPost =
                                    post!!.filter { it.categoryName!!.contains("Cute") }.map { it }
                                ScrollToLoad(R.id.cat_training)
                            }

                            media_grid_top_posts.apply {

                                media_grid_top_posts.layoutManager =
                                    StaggeredGridLayoutManager(
                                        2,
                                        StaggeredGridLayoutManager.VERTICAL
                                    )
                                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                                media_grid_top_posts.adapter =
                                    ExploreMediaGridAdapter(finalPost as ArrayList<Post>)
                                    { p ->
                                        redirectToSinglePost(p)
                                    }
                            }
                        } else {
                            top_scroll.isEnabled = false;
                            Toast.makeText(
                                activity,
                                "You reached the last Post",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

        }else {
            model.getAllPostsWithQuery(page, limit).observe(viewLifecycleOwner, Observer { post ->
                if (post != null) {
                    scrollToLoad = true
                    var sortedPosts = post!!.sortedByDescending { it.likes!!.size }.map { it }
                    ScrollToLoad(null)
                    media_grid_top_posts.apply {
                        media_grid_top_posts.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                        media_grid_top_posts.adapter =
                            ExploreMediaGridAdapter(sortedPosts as ArrayList<Post>)
                            { p ->
                                redirectToSinglePost(p)
                            }
                    }
                } else {
                    top_scroll.isEnabled = false;
                    Toast.makeText(activity, "You reached the last Post", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun searchToAdapter() {
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(newText: String?): Boolean {

                if (newText != null) {
                    scrollToLoad = false
                    
                    model.getPostBySearching(newText!!)
                        .observe(viewLifecycleOwner, Observer { post ->
                            setAdapter(post!!)
                        })

                    SearchClickx()
                }
                return true
            }
        })
    }

    private fun SearchClickx(){
    val closeButton: View? = search_bar.findViewById(androidx.appcompat.R.id.search_close_btn)
    val clearText: EditText? = search_bar.findViewById(androidx.appcompat.R.id.search_src_text)
    val mSearch: SearchView? = search_bar.findViewById(androidx.appcompat.R.id.search_bar)

        closeButton?.setOnClickListener {
            mSearch!!.setQuery("", false)
            mSearch!!.onActionViewCollapsed()
            clearText!!.setText("")
            allPostsToAdapter(null)
        }

    }

    private fun populateCatSpinner(){
        var petTypeDefault = (PetType("0", "", "select animal.."))

        model.getPetType()?.observe(viewLifecycleOwner, Observer { petType ->
            val spinnerAdapter = ArrayAdapter<PetType>(
                mContext!!,
                android.R.layout.simple_spinner_item, petType
            )

            spinnerAdapter.sort(compareBy { it.name })
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
                val petTypeName: PetType = parent!!.selectedItem as PetType

                    model.getPostByType(petTypeName.name).observe(
                        viewLifecycleOwner,
                        Observer { p ->
                            setAdapter(p!!)
                        })

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setAdapter(adapterList: List<Post>){
        media_grid_top_posts.apply {
            media_grid_top_posts.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            media_grid_top_posts.adapter =
                ExploreMediaGridAdapter(adapterList as ArrayList<Post>){ p -> redirectToSinglePost(p)
                }
        }
    }


    override fun onClick(v: View?) {
        var button = v
        scrollToLoad = true


        cat_spinner_start.isSelected = false
        cat_all.isSelected = false
        cat_training.isSelected = false
        cat_tricks.isSelected = false
        cat_obedience.isSelected = false
        cat_feeding.isSelected = false
        cat_cute.isSelected = false
        cat_other.isSelected = false


        button!!.isSelected = true


            when (v?.getId()) {
                R.id.cat_spinner_start -> {
                    cat_spinner_start.setVisibility(View.GONE)
                    cat_spinner.setVisibility(View.VISIBLE)
                    selectItemInSpinner()
                }
                R.id.cat_all -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(null)
                    populateCatSpinner()
                }
                R.id.cat_training -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_training)
                    populateCatSpinner()
                }
                R.id.cat_tricks -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_tricks)
                    populateCatSpinner()
                }
                R.id.cat_obedience -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_obedience)
                    populateCatSpinner()
                }
                R.id.cat_feeding -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_feeding)
                    populateCatSpinner()
                }
                R.id.cat_cute -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_cute)
                    populateCatSpinner()
                }
                R.id.cat_other -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    page = 1
                    allPostsToAdapter(R.id.cat_other)
                    populateCatSpinner()
                }
                else -> {
                    button!!.isSelected = false
                }
            }

    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post, null)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("TopPostFragment")!!.commit()

    }


}

