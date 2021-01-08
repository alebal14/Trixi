package com.example.trixi.ui.explore

//import androidx.fragment.app.viewModels

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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


class ShowTopPostsFragment : Fragment(), View.OnClickListener {
    private lateinit var model: TrixiViewModel
    //private lateinit var linearLayoutManager: LinearLayoutManager
    var  mContext : Context? = null
    var isClicked : Boolean = false

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

        cat_all.setOnClickListener(this)
        cat_training.setOnClickListener(this)
        cat_tricks.setOnClickListener(this)
        cat_obedience.setOnClickListener(this)
        cat_feeding.setOnClickListener(this)
        cat_cute.setOnClickListener(this)
        cat_other.setOnClickListener(this)

        allPostsToAdapter()
        searchToAdapter()
        populateCatSpinner()
        selectItemInSpinner()
        catButtons()

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

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                if(position == 0){
                    allPostsToAdapter()
                }else{
                    model.getPostByType(petTypeName.name).observe(
                        viewLifecycleOwner,
                        Observer { p ->
                            media_grid_top_posts.apply {
                                media_grid_top_posts.layoutManager =
                                    StaggeredGridLayoutManager(
                                        2,
                                        StaggeredGridLayoutManager.VERTICAL
                                    )
                                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                                media_grid_top_posts.adapter =
                                    ExploreMediaGridAdapter(p as ArrayList<Post>)
                            }
                        })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    private fun catButtons(){
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

       /* cat_training.setOnClickListener {
                model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->

                    if (isNotClicked == false) {
                        cat_training.setBackgroundColor(getResources().getColor(R.color.colorLightGreen))
                        isNotClicked = true
                        setAdapter(post!!)
                        println("KOLLAR 1 " + post.size + isNotClicked)
                    } else {
                        cat_training.setBackgroundColor(Color.WHITE)
                        isNotClicked = false
                        val finalPost =
                            post!!.filter { it.categoryName!!.contains("Training") }.map { it }
                        setAdapter(finalPost)
                        println("KOLLAR 2 " + finalPost.size + isNotClicked)
                    }
                })
        }

        cat_tricks.setOnClickListener {
            model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->

                if (isNotClicked == false) {
                    cat_tricks.setBackgroundColor(getResources().getColor(R.color.colorLightGreen))
                    isNotClicked = true
                    setAdapter(post!!)
                    println("KOLLAR 1 " + post.size + isNotClicked)
                } else {
                    cat_tricks.setBackgroundColor(Color.WHITE)
                    isNotClicked = false
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Tricks") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 2 " + finalPost.size + isNotClicked)
                }
            })
        }*/


    }

    private fun setAdapter(adapterList: List<Post>){
        media_grid_top_posts.apply {
            media_grid_top_posts.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            media_grid_top_posts.adapter =
                ExploreMediaGridAdapter(adapterList as ArrayList<Post>)
        }
    }


    override fun onClick(v: View?) {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        var button = v

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
                R.id.cat_all -> {
                    allPostsToAdapter()
                    println("KOLLAR 2 " + post!!.size)
                }
                R.id.cat_training -> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Training") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 2 " + finalPost.size)
                }
                R.id.cat_tricks -> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Tricks") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 3 " + finalPost.size)
                }
                R.id.cat_obedience-> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Obedience") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 3 " + finalPost.size)
                }
                R.id.cat_feeding -> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Feeding") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 3 " + finalPost.size)
                }
                R.id.cat_cute -> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Cute") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 3 " + finalPost.size)
                }
                R.id.cat_other -> {
                    val finalPost =
                        post!!.filter { it.categoryName!!.contains("Other") }.map { it }
                    setAdapter(finalPost)
                    println("KOLLAR 3 " + finalPost.size)
                }
                else -> {
                }
            }

        })

    }


}