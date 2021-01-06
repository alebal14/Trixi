package com.example.trixi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
//import androidx.fragment.app.viewModels
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.android.synthetic.main.fragment_single_post.*

class PopUpCommentWindow(private val comments: List<Comment>?,var postId:String, var viewHolder: GroupieViewHolder?) :
    DialogFragment() {
    private lateinit var model: TrixiViewModel
    private val db = PostToDb()
    private val adapterChat = GroupAdapter<GroupieViewHolder>()


    companion object {
        const val TAG = "popUpChat"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_comment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCommentsView()

        send_comment.setOnClickListener {
            sendComment()

            //setUpCommentsView()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setUpCommentsView() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)


        comments!!.forEach { comment ->
            model.getOneUser(comment.userId)?.observe(viewLifecycleOwner, { commnetOwner ->
                Log.d("home1", "Comment owner ${commnetOwner.userName}")
                Log.d("home", "Comment  ${comment.comment}")

                adapterChat.add(CommentItem(comment, commnetOwner))
            })

        }

        recyclerView_popup_comment.adapter = adapterChat

    }

    private fun sendComment() {

        val commentText = enter_comment.text.toString()
        val postId = postId
        val userId = PostToDb.loggedInUser?.uid.toString()

        if (commentText.isEmpty()) {
            Toast.makeText(context, "Please write a comment", Toast.LENGTH_LONG).show()
            return
        }

        val commentObj = Comment(commentText, postId, userId, null)
        db.comment(commentObj)
        if(viewHolder!=null){
            viewHolder?.itemView?.home_item_chat_count!!.text = ((comments!!.size  + 1).toString())
        }
        enter_comment.text.clear()
        adapterChat.add(CommentItem(commentObj, PostToDb.loggedInUser))
        recyclerView_popup_comment.adapter = adapterChat

    }


}

class CommentItem(private val comment: Comment, private val commentOwner: User?) :
    Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_sender_name.text = commentOwner!!.userName
        viewHolder.itemView.comment_description.text = comment.comment
        Picasso.get().load(RetrofitClient.BASE_URL + commentOwner.imageUrl)
            .transform(CropCircleTransformation()).fit()
            .into(viewHolder.itemView.comment_profile_img)
    }

    override fun getLayout(): Int {
        return R.layout.comment_row
    }

}