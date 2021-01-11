package com.example.trixi.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Like
import com.example.trixi.entities.Pet
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.ui.fragments.PopUpCommentWindow
import com.example.trixi.ui.profile.PetProfileFragment
import com.example.trixi.ui.profile.UserProfileFragment
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_single_post.*


class SinglePostFragment(private val post1: Post?) : Fragment() {
    private lateinit var model: TrixiViewModel
    var numberOfLike: Int = 0

    companion object {
        private val db = PostToDb()
    }


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
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        if (post1 != null) {
            model.aPostById(post1.uid.toString()).observe(viewLifecycleOwner, {
                numberOfLike = it.likes!!.size
                populatePost(it)
                handleClickOnComment(it)
                handleClickOnLike(it)
                //handleClickOnProfile(it)
            })
        }

    }

    private fun handleClickOnUser(user: User) {
        single_item_profileimg.setOnClickListener {
            redirectToUserProfile(user)
        }
        single_item_profileName.setOnClickListener {
            redirectToUserProfile(user)
        }
    }

    private fun redirectToUserProfile(user: User) {
        val userProfile = UserProfileFragment(user)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, userProfile)?.addToBackStack("userFragment")!!.commit()
    }

    private fun handleCLickOnPet(pet: Pet) {
        single_item_profileimg.setOnClickListener {
            redirectToPetProfile(pet)
        }
        single_item_profileName.setOnClickListener {
            redirectToPetProfile(pet)
        }

    }

    private fun redirectToPetProfile(pet: Pet) {
        val petProfile = PetProfileFragment(pet)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, petProfile)?.addToBackStack("petFragment")!!.commit()
    }

    private fun populatePost(post: Post) {

        setVisibilityByOwner(post)

        single_item_title.text = post?.title.toString()
        single_item_description.text = post?.description.toString()
        single_item_tags.text = post?.categoryName.toString()
        single_item_chat_count.text = post?.comments?.size.toString()
        single_item_like_count.text = numberOfLike.toString()

        if (post?.fileType.toString() == "image") {
            single_item_video.visibility = View.GONE;
            single_item_image.visibility = View.VISIBLE;
            Picasso.get().load(RetrofitClient.BASE_URL + post?.filePath.toString()).centerCrop()
                .fit().into(single_item_image)
        } else {
            single_item_image.visibility = View.GONE;
            single_item_video.visibility = View.VISIBLE;
            single_item_video.setSource(RetrofitClient.BASE_URL + post?.filePath.toString())
        }

    }

    private fun setVisibilityByOwner(post: Post) {
        if (post.ownerId == PostToDb.loggedInUser?.uid) { // logged-in user's post
            single_item_profileName.visibility = View.GONE
            single_item_profileimg.visibility = View.GONE
            single_item_profile.visibility = View.GONE
            single_item_report.visibility = View.GONE
            handleClickOnEdit(post)
        } else { // other users post
            single_item_edit.visibility = View.GONE
            post.ownerId?.let {
                model.getOneUser(it)?.observe(viewLifecycleOwner, { user ->
                    if (user != null) {
                        single_item_profile.visibility = View.GONE
                        single_item_profileName.text = user.userName
                        Picasso.get().load(RetrofitClient.BASE_URL + user.imageUrl)
                            .transform(CropCircleTransformation()).fit()
                            .placeholder(R.drawable.sample)
                            .error(R.drawable.sample)
                            .centerCrop().into(single_item_profileimg)
                        handleClickOnUser(user)
                    } else {
                        model.getOnePet(it)?.observe(viewLifecycleOwner, { pet ->
                            if (pet != null) {
                                single_item_profileName.text = pet.name
                                Picasso.get().load(RetrofitClient.BASE_URL + pet.imageUrl)
                                    .transform(CropCircleTransformation()).fit()
                                    .placeholder(R.drawable.sample)
                                    .error(R.drawable.sample)
                                    .centerCrop().into(single_item_profileimg)
                                if (pet.ownerId == PostToDb.loggedInUser?.uid) {
                                    single_item_edit.visibility = View.VISIBLE
                                    single_item_report.visibility = View.GONE
                                    handleClickOnEdit(post)
                                }
                                handleCLickOnPet(pet)
                            }

                        })
                    }

                })
            }
        }
    }

    private fun handleClickOnEdit(post: Post) {
        single_item_edit.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_container, EditPostFragment(post))?.commit()
        }
    }


    private fun handleClickOnComment(post: Post) {
        single_item_chat.setOnClickListener {
            model.aPostById(post.uid.toString()).observe(viewLifecycleOwner, {
                val popUp = PopUpCommentWindow(it?.comments, it?.uid.toString(), null)
                popUp.show(activity?.supportFragmentManager!!, PopUpCommentWindow.TAG)
                single_item_chat_count.text = it?.comments?.size.toString()

            })

        }

    }

    private fun handleClickOnLike(post: Post) {
        var liked = false

        if (post != null) {
            post.likes?.forEach {
                if (it.userId == PostToDb.loggedInUser!!.uid) {
                    liked = true
                }
            }
        }
        if (liked) {
            single_item_like.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            single_item_like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }



        single_item_like.setOnClickListener {
            val like = Like(post?.uid.toString(), PostToDb.loggedInUser?.uid.toString(), null)

            if (liked) {
                db.unlike(like)
                liked = false
                single_item_like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                numberOfLike -= 1
                single_item_like_count.text = numberOfLike.toString()
            } else {
                db.like(like)
                liked = true
                single_item_like.setImageResource(R.drawable.ic_baseline_favorite_24)
                numberOfLike += 1
                single_item_like_count.text = numberOfLike.toString()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        PostToDb.postedPost = null
    }


}