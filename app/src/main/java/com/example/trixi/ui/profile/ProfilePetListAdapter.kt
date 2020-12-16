package com.example.trixi.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.entities.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_user_pet.view.*

class ProfilePetListAdapter (private var pets: ArrayList<Pet>)
    //private val listener:(Post) -> Unit)
    : RecyclerView.Adapter<ProfilePetListAdapter.ProfilePetListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePetListViewHolder {
            val gridView = (LayoutInflater.from(parent.context).inflate(
                //change this to pet recycler
                R.layout.profile_pet_list,
                parent, false
            ))
            return ProfilePetListViewHolder(gridView)
        }

        override fun onBindViewHolder(holder: ProfilePetListViewHolder, position: Int) {
            holder.bindView(pets[position]
                //,listener
            )
        }

        override fun getItemCount(): Int {
            return pets.size
        }

        class ProfilePetListViewHolder
            (view: View) : RecyclerView.ViewHolder(view) {
            val petProfileThumbnail: ImageView = itemView.user_profile_pet_image

            fun bindView(
                pet: Pet
                //listener: (Post) -> Unit) {
            ){
                Picasso.get().load(pet.imageUrl!!).into(petProfileThumbnail)
                //itemView.setOnClickListener { listener(post) }
            }

        }

    }