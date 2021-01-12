package com.example.trixi.ui.explore

//import androidx.fragment.app.viewModels

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.trixi.R
import com.example.trixi.entities.PetType
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.post.SinglePostFragment
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*


class ShowTopPostsFragment : Fragment(), View.OnClickListener {
    private lateinit var model: TrixiViewModel
    //private lateinit var linearLayoutManager: LinearLayoutManager
    var  mContext : Context? = null
    var page = 1
    var limit = 10




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

        cat_spinner.setVisibility(View.GONE)

        cat_all.setOnClickListener(this)
        cat_training.setOnClickListener(this)
        cat_tricks.setOnClickListener(this)
        cat_obedience.setOnClickListener(this)
        cat_feeding.setOnClickListener(this)
        cat_cute.setOnClickListener(this)
        cat_other.setOnClickListener(this)
        cat_spinner_start.setOnClickListener(this)

        allPostsToAdapter()
        searchToAdapter()
        populateCatSpinner()
        ScrollToLoad()


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

    private fun ScrollToLoad(){

        top_scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY == v!!.getChildAt(0).measuredHeight - v!!.measuredHeight){
                    page++
                    allPostsToAdapter()
                }
            }
        })

        pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            pullToRefresh.isEnabled = false;
            if(page > 1){
                page--
                allPostsToAdapter()
            }
            pullToRefresh.isEnabled = true;
        })
    }

    private fun allPostsToAdapter(){
        
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        model.getAllPostsWithQuery(page, limit)?.observe(viewLifecycleOwner, Observer { post ->
                var sortedPosts = post!!.sortedByDescending { it.likes!!.size }.map { it!! }

                media_grid_top_posts.apply {
                media_grid_top_posts.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                media_grid_top_posts.adapter = ExploreMediaGridAdapter(sortedPosts as ArrayList<Post>)
                { p ->
                    redirectToSinglePost(p)
                }
            }
        })
    }

    private fun searchToAdapter() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    model.getPostBySearching(newText!!)
                        .observe(viewLifecycleOwner, Observer { post ->
                            setAdapter(post!!)
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

            clearText!!.setText("")
            allPostsToAdapter()
        }

    }

    private fun populateCatSpinner(){
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
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
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

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
                ExploreMediaGridAdapter(adapterList as ArrayList<Post>){
                    p -> redirectToSinglePost(p)
                }
        }
    }


    override fun onClick(v: View?) {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        var button = v

        cat_spinner_start.isSelected = false
        cat_all.isSelected = false
        cat_training.isSelected = false
        cat_tricks.isSelected = false
        cat_obedience.isSelected = false
        cat_feeding.isSelected = false
        cat_cute.isSelected = false
        cat_other.isSelected = false

        button!!.isSelected = true

        model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->

            when (v?.getId()) {
                R.id.cat_spinner_start ->{
                    cat_spinner_start.setVisibility(View.GONE)
                    cat_spinner.setVisibility(View.VISIBLE)
                    selectItemInSpinner()
                }
                R.id.cat_all -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val sortedPosts = post!!.sortedByDescending { it.likes!!.size }.map { it!! }
                    setAdapter(sortedPosts)
                    populateCatSpinner()
                }
                R.id.cat_training -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Training") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                R.id.cat_tricks -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Tricks") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                R.id.cat_obedience-> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Obedience") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                R.id.cat_feeding -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Feeding") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                R.id.cat_cute -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Cute") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                R.id.cat_other -> {
                    cat_spinner.setVisibility(View.GONE)
                    cat_spinner_start.setVisibility(View.VISIBLE)
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Other") }.map { it }
                    setAdapter(finalPost)
                    populateCatSpinner()
                }
                else -> {
                    button!!.isSelected = false
                }
            }

        })

    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("TopPostFragment")!!.commit()

    }


}

