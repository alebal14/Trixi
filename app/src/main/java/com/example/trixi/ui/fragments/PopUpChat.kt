package com.example.trixi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.trixi.R
import com.example.trixi.entities.Comment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class PopUpChat(val comments: List<Comment>) : DialogFragment(){
    companion object{
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


    }


}

class CommentItem(val comment:Comment) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        TODO("Not yet implemented")
    }

}