package com.example.trixi.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.apiService.RetrofitClient.Companion.context
import com.example.trixi.entities.Like
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.PopUpCommentWindow
import com.example.trixi.ui.profile.PetProfileFragment
import com.example.trixi.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_home_item.view.*

class HomeAdapter(
    private var posts: ArrayList<Post>,
    private val fm: FragmentManager,
    private val viewLifeCycleOwner: LifecycleOwner,
    private var activeButton: String,
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val linearView = (LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_home_item, parent, false))
        return HomeViewHolder(linearView, activeButton)
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        holder.bindPost(posts[position], fm,/*listener,*/viewLifeCycleOwner)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class HomeViewHolder(view: View, active: String) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val activeButton = active

        override fun onClick(p0: View?) {
        }

        fun bindPost(
            post: Post,
            fm: FragmentManager, /*listener: (Post) -> Unit,*/
            viewLifeCycleOwner: LifecycleOwner,
        ) {

            val db = PostToDb()
            val model = TrixiViewModel()

            populateImgOrVideo(post)

            model.getOneUser(post.ownerId!!)
                ?.observe(viewLifeCycleOwner, Observer { postOwner ->
                    if (postOwner != null) {
                        populateUserInfo(postOwner, fm)

                    } else {
                        model.getOnePet(post.ownerId!!)
                            ?.observe(viewLifeCycleOwner, Observer { petIsOwner ->
                                populatePetInfo(petIsOwner, fm)
                            })
                    }
                })
            itemView.home_item_title.text = post.title
            itemView.home_item_description.text = post.description
            itemView.home_item_edit.isVisible = false
            val numberOfComments: Int = post.comments!!.size
            itemView.home_item_chat_count.text = numberOfComments.toString()
            val numberOfLike: Int = post.likes!!.size
            itemView.home_item_like_count.text = numberOfLike.toString()
            itemView.home_item_tags.text = post.categoryName

            var activeTextView: TextView
            if (activeButton.equals("discover")) {
                activeTextView = itemView.home_item_following
            } else activeTextView = itemView.home_item_discover

            activeTextView.setTextColor(
                ContextCompat.getColor(context, R.color.gray)
            )

            handleLike(numberOfLike, post, db)
            handleClickOnComment(post, fm)
            handleClickOnDiscovery(fm)
            handleClickOnFollowing(fm)
        }

        private fun populateImgOrVideo(post: Post) {
            if (post.fileType.toString() == "image") {
                itemView.home_item_media.visibility = View.VISIBLE
                itemView.home_item_video.visibility = View.GONE
                Picasso.get().load(RetrofitClient.BASE_URL + post.filePath).centerCrop().fit()
                    .into(itemView.home_item_media)
            } else {
                itemView.home_item_media.visibility = View.GONE
                itemView.home_item_video.visibility = View.VISIBLE
                itemView.home_item_video.setSource(RetrofitClient.BASE_URL + post.filePath.toString())
            }
        }

        private fun populatePetInfo(
            petIsOwner: Pet,
            fm: FragmentManager,
        ) {
            Picasso.get()
                .load(RetrofitClient.BASE_URL + (petIsOwner?.imageUrl /*?: post.ownerIsPet?.imageUrl*/))
                .transform(CropCircleTransformation()).fit()
                .centerCrop().into(itemView.home_item_profileimg)

            itemView.home_item_profileName.text = petIsOwner.name
            redirectToPet(petIsOwner, fm)
        }

        private fun populateUserInfo(
            postOwner: User,
            fm: FragmentManager,
        ) {
            Picasso.get()
                .load(RetrofitClient.BASE_URL + (postOwner?.imageUrl /*?: post.ownerIsPet?.imageUrl*/))
                .transform(CropCircleTransformation()).fit()
                .centerCrop().into(itemView.home_item_profileimg)

            itemView.home_item_profileName.text = postOwner.userName
            redirectToUser(postOwner, fm)
        }

        private fun redirectToUser(user: User, fm: FragmentManager) {
            val profileName: TextView = itemView.findViewById(R.id.home_item_profileName)
            val profileImg: ImageView = itemView.findViewById(R.id.home_item_profileimg)

            profileImg.setOnClickListener {
                val userProfileFragment = UserProfileFragment(user)
                fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()
            }
            profileName.setOnClickListener {
                val userProfileFragment = UserProfileFragment(user)
                fm.beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit()
            }
        }

        private fun redirectToPet(pet: Pet, fm: FragmentManager) {
            val profileName: TextView = itemView.findViewById(R.id.home_item_profileName)
            val profileImg: ImageView = itemView.findViewById(R.id.home_item_profileimg)

            profileImg.setOnClickListener {
                val petProfileFragment = PetProfileFragment(pet)
                fm.beginTransaction().replace(R.id.fragment_container, petProfileFragment).commit()

            }
            profileName.setOnClickListener {
                val petProfileFragment = PetProfileFragment(pet)
                fm.beginTransaction().replace(R.id.fragment_container, petProfileFragment).commit()
            }
        }

        private fun handleClickOnFollowing(fm: FragmentManager) {
            val followingText: TextView = itemView.findViewById(R.id.home_item_following)
            followingText.setOnClickListener {
                fm.beginTransaction().replace(R.id.fragment_container, HomepageFragment())
                    .commit()
            }
        }

        private fun handleClickOnDiscovery(fm: FragmentManager) {
            val discoveryText: TextView = itemView.findViewById(R.id.home_item_discover)
            discoveryText.setOnClickListener {
                fm.beginTransaction().replace(R.id.fragment_container, DiscoverFragment())
                    .commit()
            }
        }

        private fun handleClickOnComment(post: Post, fm: FragmentManager) {
            val commentIcon: ImageButton = itemView.findViewById(R.id.home_item_chat)
            commentIcon.setOnClickListener {
                val popUp = PopUpCommentWindow(post.comments, post.uid.toString(), null)
                popUp.show(fm, PopUpCommentWindow.TAG)
            }
        }

        private fun handleLike(numberOfLike: Int, post: Post, db: PostToDb) {
            var numberOfLike1 = numberOfLike
            val likeHeart: ImageButton = itemView.findViewById(R.id.home_item_like)

            var liked = false

            post.likes?.forEach {
                if (it.userId == PostToDb.loggedInUser!!.uid) {
                    liked = true
                }
            }
            if (liked) {
                likeHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                likeHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

            likeHeart.setOnClickListener {
                val like = Like(post.uid.toString(), PostToDb.loggedInUser?.uid.toString(), null)

                if (liked) {
                    db.unlike(like)
                    liked = false
                    likeHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    numberOfLike1 -= 1
                    itemView.home_item_like_count.text = numberOfLike1.toString()
                } else {
                    db.like(like)
                    liked = true
                    likeHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
                    numberOfLike1 += 1
                    itemView.home_item_like_count.text = numberOfLike1.toString()
                }
            }
        }
    }

}