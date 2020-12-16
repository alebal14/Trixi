package com.example.trixi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.repository.PostToDb
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.android.synthetic.main.fragment_single_post.*


class singlePostFragment : Fragment() {



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
        //val title = requireArguments().getString("title")
        //val filePath = requireArguments().getString("url")
        //val descripton = requireArguments().getString("description")


        val post = PostToDb.latestPost

        single_item_title.text = post?.title.toString()
        single_item_description.text = post?.description.toString()


        Picasso.get().load(RetrofitClient.BASE_URL + post?.filePath.toString()).centerCrop().fit().into(single_item_media)

    }


}