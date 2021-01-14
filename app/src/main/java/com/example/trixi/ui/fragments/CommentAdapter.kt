package com.example.trixi.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.comment_row.view.*

class CommentAdapter(
    private var comments: ArrayList<Comment>?,
    private  val fm: FragmentManager,
    private val viewLifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<CommentAdapter.CommentViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewholder {
        val commentView = (LayoutInflater.from(parent.context).inflate(
            R.layout.comment_row,
            parent, false
        ))
        return CommentViewholder(commentView)
    }

    override fun onBindViewHolder(holder: CommentViewholder, position: Int) {
        if (!comments.isNullOrEmpty()){
            holder.bindComment(comments!![position],viewLifecycleOwner)
        }

    }

    override fun getItemCount(): Int {
        return comments!!.size
    }



    class CommentViewholder(view: View): RecyclerView.ViewHolder(view){

        val model = TrixiViewModel()
        fun bindComment(comment: Comment, viewLifecycleOwner: LifecycleOwner) {
            itemView.comment_description.text = comment.comment
            model.getOneUser(comment.userId)?.observe(viewLifecycleOwner,{commentOwner ->
                itemView.comment_sender_name.text = commentOwner!!.userName
                Picasso.get().load(RetrofitClient.BASE_URL + commentOwner.imageUrl)
                    .transform(CropCircleTransformation()).fit()
                    .into(itemView.comment_profile_img)

            })

        }

    }
}
