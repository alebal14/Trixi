package com.example.trixi.ui.edit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Category
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_upload.*

class EditPostFragment(private val post: Post) : Fragment() {
    private lateinit var model: TrixiViewModel
    var mContext: Context? = null;

    val db = PostToDb()
    val loggedInUserId = PostToDb.loggedInUser?.uid.toString()
    var uid = ""
    var title = ""
    var categoryName = ""
    var description = ""
    var ownerId = ""
    var fileType = ""
    var filePath = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Edit post"
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        camera_gallery_buttons_container.visibility = View.GONE
        uploadVideo.visibility = View.GONE
        button_post.visibility = View.GONE
        Log.d("Edit", "post id: ${post.uid}")
        Log.d("Edit", "post categoryname: ${post.categoryName}")

        assignData()
        populatePost()
        addCategoryToSpinnerAndSelect()
        addPetToSpinnerAndSelect()

        button_update_post.setOnClickListener { updatePost() }
        button_delete_post.setOnClickListener { deletePost() }


    }

    private fun assignData() {
        uid = post.uid.toString()
        title = post.title.toString()
        description = post.description.toString()
        categoryName = post.categoryName.toString()
        ownerId = post.ownerId.toString()
        fileType = post.fileType.toString()
        filePath = post.filePath.toString()

    }

    private fun populatePost() {
        if (fileType == "image") {
            uploadImage.visibility = View.VISIBLE;
            uploadVideo.visibility = View.GONE;
            Picasso.get()
                .load(RetrofitClient.BASE_URL + filePath)
                .centerCrop()
                .fit()
                .into(uploadImage);
        } else {
            uploadImage.visibility = View.GONE;
            uploadVideo.visibility = View.VISIBLE;
            uploadVideo.setSource(RetrofitClient.BASE_URL + filePath)

        }

        title_field.setText(title)
        description_field.setText(description)


    }

    private fun addPetToSpinnerAndSelect() {
        if (upload_spinner_add_pet != null) {
            var petList = model.getPetsByOwner(loggedInUserId)
            var petdefault = (Pet("0", null, "", "Select Pet", "", "", "", "", "", null, ""))

            petList?.observe(viewLifecycleOwner, { allPets ->
                if (allPets!!.isEmpty()) {
                    upload_spinner_add_pet.visibility = View.GONE;
                    ownerId = loggedInUserId
                } else {

                    val spinnerAdapter = ArrayAdapter<Pet>(
                        mContext!!,
                        android.R.layout.simple_spinner_item,
                        allPets
                    )
                    spinnerAdapter.sort(compareBy { it.name })
                    spinnerAdapter.insert(petdefault, 0)
                    upload_spinner_add_pet.adapter = spinnerAdapter
                    for (pet in allPets) {
                        if (pet.uid == ownerId) {
                            var position = spinnerAdapter.getPosition(pet)
                            upload_spinner_add_pet.setSelection(position)
                            break
                        }
                    }
                }
            })

            upload_spinner_add_pet.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val pet: Pet = parent.selectedItem as Pet
                    ownerId = if (position == 0) {
                        loggedInUserId;
                    } else {
                        pet.uid
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }

            }
        }
    }

    private fun addCategoryToSpinnerAndSelect() {


        if (upload_spinner_add_category != null) {
            var defaultCat = Category("0", "Select a category")
            model.getAllCategories().observe(viewLifecycleOwner, { allCategory ->
                val spinnerAdapter = ArrayAdapter<Category>(
                    mContext!!,
                    android.R.layout.simple_spinner_item,
                    allCategory
                )
                upload_spinner_add_category.adapter = spinnerAdapter
                for (category in allCategory) {
                    if (category.name == categoryName) {
                        var position = spinnerAdapter.getPosition(category)
                        upload_spinner_add_category.setSelection(position)
                        break
                    }
                }
            })

            upload_spinner_add_category.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val category: Category = parent.selectedItem as Category
                    categoryName = category.name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }

            }
        }
    }

    private fun updatePost() {
            title = title_field.text.toString()
            description = description_field.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(activity, "Please enter a title", Toast.LENGTH_SHORT).show()
                return
            }

            val updatedPost =
                Post(uid, title, description, "", "", ownerId, categoryName, null, null)
            db.updatePost(updatedPost)

    }

    private fun deletePost() {
    }
}