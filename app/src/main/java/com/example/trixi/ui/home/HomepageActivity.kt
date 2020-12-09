package com.example.trixi.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trixi.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_home.*


class HomepageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val adapter = GroupAdapter<GroupieViewHolder> ()

        adapter.add(HomeItem())
        adapter.add(HomeItem())
        adapter.add(HomeItem())
        adapter.add(HomeItem())

        recyclerView_homepage.adapter = adapter;
    }
}

    class HomeItem : Item<GroupieViewHolder> (){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        }


    override fun getLayout(): Int {
        return R.layout.home_item;
    }


    }