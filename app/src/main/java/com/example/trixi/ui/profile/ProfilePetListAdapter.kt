package com.example.trixi.ui.profile

import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.MainActivity
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.profile_user_pet.view.*

class ProfilePetListAdapter(private var pets: ArrayList<Pet>)
//private val listener:(Pet) -> Unit)
    : RecyclerView.Adapter<ProfilePetListAdapter.ProfilePetListViewHolder>() {

    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun getItemCount() = pets.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePetListViewHolder {
        val gridView = (LayoutInflater.from(parent.context).inflate(
            R.layout.profile_user_pet,
            parent, false
        ))

        return ProfilePetListViewHolder(gridView)

    }

    override fun onBindViewHolder(holder: ProfilePetListViewHolder, position: Int) {
        holder.bindView(
            pets[position]
            //,listener
        )
    }

    class ProfilePetListViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val petProfileThumbnail = itemView.pet_list_profile_image
        val petName = itemView.pet_name

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Log.d("Recyclerview pet list", "click!")
        }

        fun bindView(
            pet: Pet
            //listener: (Post) -> Unit) {
        ) {
            Picasso.get().load(BASE_URL + pet.imageUrl!!).into(petProfileThumbnail)
            petName.text = pet.name
            //itemView.setOnClickListener { listener(post) }
        }
    }

}