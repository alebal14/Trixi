package com.example.trixi.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.PetType
import com.example.trixi.entities.Post
import com.example.trixi.repository.DeleteFromDb
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_edit_pet_profile.*
import kotlinx.android.synthetic.main.fragment_pet_register.*
import kotlinx.android.synthetic.main.fragment_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class EditPetProfile(private val pet : Pet)  : Fragment(){

    private val db = PostToDb()
    private val dbDelete = DeleteFromDb()
    var uid = ""
    var ownerId = PostToDb.loggedInUser?.uid.toString()
    var petName = ""
    var petAge = ""
    var petBreed = ""
    var petBio = ""
    var petTypeName = ""
    var gender = ""
    var newPhoto = false

    var mContext: Context? = null;

    var image: MultipartBody.Part? = null


    private val REQUEST_PERMISSION = 100

    var selectedFile: Uri? = null
    var filePath = ""
    var mediaPath: String? = null
    var postPath: String? = null

    var file: File? = null
    var file_validation = true
    lateinit var model: TrixiViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_pet_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        assingData()
        populateView()
        setUpSpinners()


        button_update_profile.setOnClickListener {
            updatePet() }
        button_delete_profile.setOnClickListener {
            showDeleteDialog() }

        edit_profile_image.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            )
            startActivityForResult(intent, 0)
        }

    }

    private fun assingData() {
        uid = pet.uid.toString()
        ownerId = pet.ownerId.toString()
        petName = pet.name.toString()
        petAge = pet.age.toString()
        petBreed = pet.breed.toString()
        petBio = pet.bio.toString()
        petTypeName = pet.petType.toString()
        gender = pet.gender.toString()
        filePath = pet.imageUrl.toString()
    }

    private fun populateView() {

        Picasso.get()
                .load(RetrofitClient.BASE_URL + filePath)
                .centerCrop()
                .transform(CropCircleTransformation())
                .fit()
                .into(edit_profile_image);


        edit_username.setText(petName)
        edit_description.setText(petBio)
        edit_pet_age.setText(petAge)
        edit_breed.setText(petBreed)


    }

    private fun setUpSpinners() {

        if (edit_pet_type != null) {
            model.getPetType().observe(viewLifecycleOwner, { allPetType ->
                val spinnerAdapter = ArrayAdapter<PetType>(
                    mContext!!,
                    android.R.layout.simple_spinner_item,
                    allPetType
                )
                spinnerAdapter.sort(compareBy { it.name.toLowerCase() })
                edit_pet_type.adapter = spinnerAdapter
                for (type in allPetType) {
                    if (type.name == petTypeName) {
                        var position = spinnerAdapter.getPosition(type)
                        edit_pet_type.setSelection(position)
                        break
                    }
                }
            })

            edit_pet_type.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val petType: PetType = parent.selectedItem as PetType
                    selectPetTypeData(petType)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        if (edit_pet_gender != null) {
            val spinnerAdapter = ArrayAdapter.createFromResource(
                mContext!!,
                R.array.gender,
                android.R.layout.simple_spinner_item
            )
            edit_pet_gender.adapter = spinnerAdapter
            var position = spinnerAdapter.getPosition(gender)
            edit_pet_gender.setSelection(position)

            edit_pet_gender.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    gender = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    private fun selectPetTypeData(petType: PetType) {
        petTypeName = petType.name
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION
            )
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedFile = data.getData()

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor =
                    activity?.contentResolver?.query(selectedFile!!, filePathColumn, null, null, null)
            assert(cursor != null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)

            cursor.close()

            postPath = mediaPath

            sendPhoto()

        }
    }

    private fun sendPhoto(){


        val totheView = view?.findViewById<View>(R.id.edit_profile_image) as ImageView

        Picasso.get()
                .load(selectedFile)
                .centerCrop()
                .transform(CropCircleTransformation())
                .fit()
                .into(totheView);

        file = File(postPath!!)

        //get the file size
        val file_size = ( file!!.length().toString().toDouble() / 1024 / 1024 )

        newPhoto = true;

        // checks if picture size is more than 5 mb
        if (file_size > 5.0){
            Toast.makeText(activity, "Picture is too big, max sixe: 5 Mb", Toast.LENGTH_LONG).show()
            file_validation = false
        } else {
            file_validation = true
        }

    }

  
    private fun updatePet() {
        petName = edit_username.text.toString()
        petAge = edit_pet_age.text.toString()
        petBreed = edit_breed.text.toString()
        petBio = edit_description.text.toString()


            if( file_validation == true){
                if(newPhoto){
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file!!)
                    image = MultipartBody.Part.createFormData("file", file?.name, requestFile);
                }
                db.sendPetToDb(image, uid, ownerId, petName, petAge, petBio, petBreed, petTypeName, gender)
            } else {
                Toast.makeText(activity, "Invalid File", Toast.LENGTH_LONG).show()
                return
            }


    }

    private fun showDeleteDialog() {

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Delete profile")
        builder.setMessage("Are you sure you want to delete this pet?")

        builder.setPositiveButton("Yes, I'm sure") { dialog, which ->
            deletePet()
            dialog.dismiss()
        }

        builder.setNegativeButton(
                "No!"
        ) { dialog, which -> // Do nothing
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    private fun deletePet() {
        dbDelete.deleteAPetFromDb(uid)
    }

}