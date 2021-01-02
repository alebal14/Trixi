package com.example.trixi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.home.HomeItem
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.android.synthetic.main.fragment_single_post.*


class singlePostFragment : Fragment() {

    private lateinit var model: TrixiViewModel

    val db = PostToDb()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_single_post, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPost()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        PostToDb.postedPost = null
    }

    private fun setupPost() {

        model = ViewModelProvider(this).get(TrixiViewModel::class.java)


            model.getLatestPost()?.observe(viewLifecycleOwner, Observer { post ->


                single_item_title.text = post.title.toString()
                single_item_description.text = post.description.toString()
                single_item_tags.text = post.categoryName.toString()
                if(post.fileType.toString() == "image") {
                    single_item_video.visibility = View.GONE;
                    single_item_image.visibility = View.VISIBLE;
                    Picasso.get().load(RetrofitClient.BASE_URL + post.filePath.toString()).centerCrop().fit().into(single_item_image)
                } else {
                    single_item_image.visibility = View.GONE;
                    single_item_video.visibility = View.VISIBLE;
                    single_item_video.setSource(RetrofitClient.BASE_URL + post.filePath.toString())
                }



            })

    }


}