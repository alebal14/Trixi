package com.example.trixi.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.entities.Post

class ActivityAdapter(
    private val posts: ArrayList<Post>,
    fm: FragmentManager,
    viewLifecycleOwner: LifecycleOwner
):RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {


    class ActivityViewHolder(view:View):RecyclerView.ViewHolder(view),View.OnClickListener{
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}