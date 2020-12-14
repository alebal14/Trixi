package com.example.trixi.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Pet
import com.example.trixi.entities.User
import com.example.trixi.ui.home.HomeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

private const val PROFILEMODEL = "model"

class ProfileFragment : Fragment() {

    private lateinit var name: String
    private lateinit var description: String
    private lateinit var profile_url: String
    private lateinit var following: String
    private lateinit var followers: String
    private lateinit var owner: String
    private lateinit var listener: OnProfileSelected

    fun newInstance(userModel: User): ProfileFragment {
        val args = Bundle()
        args.putSerializable(PROFILEMODEL, userModel)
        val fragment = ProfileFragment()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(petModel: Pet): ProfileFragment {
        val args = Bundle()
        args.putSerializable(PROFILEMODEL, petModel)
        val fragment = ProfileFragment()
        fragment.arguments = args
        return fragment
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }


    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

//     override fun onAttach(context: Context?) {
//        super(onAttach(context))
//        if (context != null) {
//            val resources = context.resources
//            name = getString(R.string.profile_name)
//            description = getString(R.string.bio_text)
//            owner = getString(R.string.pet_owner)
//            followers = getString(R.string.number_of_followers)
//            following = getString(R.string.number_of_following)
//
//            val typedArray = resources.obtainTypedArray(R.array.images)
//            val imageCount = name.size
//            val imageResIds = IntArray(imageCount)
//            for (i in 0 until imageCount) {
//                imageResIds[i] = typedArray.getResourceId(i, 0)
//            }
//            typedArray.recycle()
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView(view)
    }

    private fun setRecycleView(view: View) {

        val adapter = GroupAdapter<com.xwray.groupie.GroupieViewHolder>()

        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())

        media_grid.adapter = adapter;

        adapter.apply {
            val gridLayoutManager = GridLayoutManager(
                context,
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    class MediaItem() : Item() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            //viewHolder.itemView.background = post.path
        }

        override fun getLayout() : Int = R.layout.profile_media_thumbnail
    }

    private fun fetchUserPosts() {
        //TODO: fetch the posts for the user and show in grid,

    }

    interface OnProfileSelected {
        fun onProfileSelected(profileModel: ProfileViewModel)
    }


}



