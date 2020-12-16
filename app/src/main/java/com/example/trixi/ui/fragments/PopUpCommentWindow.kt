package com.example.trixi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.trixi.R
import com.example.trixi.entities.Comment
import com.example.trixi.entities.User
import com.example.trixi.repository.DataViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.fragment_comment.*

class PopUpCommentWindow(val comments: List<Comment>) : DialogFragment() {
    val model: DataViewModel by viewModels()

    companion object {
        const val TAG = "popUpChat"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_comment, container, false)
        return v


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCommentsView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setUpCommentsView() {
        val adapterChat = GroupAdapter<GroupieViewHolder>()
        comments.forEach { comment ->
            model.getOneUserFromDb(comment.userId).observe(this, { commnetOwner ->
                Log.d("uus", "Comment owner ${commnetOwner.userName}")
                Log.d("uus", "Comment  ${comment.comment}")

                adapterChat.add(CommentItem(comment,commnetOwner))
            })

        }

        recyclerView_popup_comment.adapter= adapterChat

    }


}

class CommentItem(val comment: Comment, val commentOwner: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_sender_name.text = commentOwner.userName
        viewHolder.itemView.comment_description.text = comment.comment
    }

    override fun getLayout(): Int {
        return R.layout.comment_row
    }

}