package com.example.trixi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.entities.Like
import com.example.trixi.entities.User
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.like_row.view.*
import kotlinx.android.synthetic.main.popup_like.*

class PopUpLikeWindow(private val likes: List<Like>?) : DialogFragment() {
    private lateinit var model: TrixiViewModel


    companion object {
        const val TAG = "popUpLike"

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
        setUpLikesView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setUpLikesView() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        val adapterLike = GroupAdapter<GroupieViewHolder>()
        likes!!.forEach { like ->
            model.getOneUser(like.userId)?.observe(viewLifecycleOwner, { liker ->
                Log.d("home", "liker ${liker.userName}")

                adapterLike.add(LikeItem(liker))
            })
        }
        recyclerView_popup_comment.adapter= adapterLike

    }


}

class LikeItem(private val liker: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_sender_name.text = liker.userName
        viewHolder.itemView.comment_description.isVisible= false
        Picasso.get().load(RetrofitClient.BASE_URL + liker.imageUrl)
            .transform(CropCircleTransformation()).fit()
            .into(viewHolder.itemView.comment_profile_img)
    }

    override fun getLayout(): Int {
        return R.layout.comment_row
    }

}