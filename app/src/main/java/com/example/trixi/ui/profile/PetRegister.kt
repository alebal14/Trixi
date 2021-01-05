package com.example.trixi.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.PetType
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.EmptyHomeFragment
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_pet_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class PetRegister : Fragment() {

    val db = PostToDb()
    val ownerId = PostToDb.loggedInUser?.uid.toString()
    var petName = ""
    var petAge = ""
    var petBreed = ""
    var petBio = ""
    var petTypeName = ""
    var gender = ""

   var  mContext : Context? = null;


    private val REQUEST_PERMISSION = 100

    var selectedFile: Uri? = null
    var filePath = ""
    var mediaPath: String? = null
    var postPath: String? = null

    var file: File? = null
    var file_validation = false


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
        return inflater.inflate(R.layout.fragment_pet_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        register_pet_image.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            )
            startActivityForResult(intent, 0)
        }

        if (register_spinner_pet_type != null) {
            model.getPetType().observe(viewLifecycleOwner, { allPetType ->
                val spinnerAdapter = ArrayAdapter<PetType>(
                    mContext!!,
                    android.R.layout.simple_spinner_item,
                    allPetType
                )
                register_spinner_pet_type.adapter = spinnerAdapter
            })

            register_spinner_pet_type.onItemSelectedListener = object :
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

        if(register_spinner_gender != null){
            val spinnerAdapter = ArrayAdapter.createFromResource(
                mContext!!,
                R.array.gender,
                android.R.layout.simple_spinner_item
            )
            register_spinner_gender.adapter = spinnerAdapter

            register_spinner_gender.onItemSelectedListener = object :
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

        register_add.setOnClickListener {
            createPet()
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("permissionRequest", "${permissions[i]} granted.")
                }
            }
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

        val totheView = view?.findViewById<View>(R.id.register_pet_image) as ImageView

        Picasso.get()
            .load(selectedFile)
            .centerCrop()
            .transform(CropCircleTransformation())
            .fit()
            .into(totheView);

        file = File(postPath)

        //get the file size
        val file_size = ( file!!.length().toString().toDouble() / 1024 / 1024 )

        // checks if picture size is more than 5 mb
        if (file_size > 5.0){
            Toast.makeText(activity, "Picture is to big, max sixe: 5 Mb", Toast.LENGTH_LONG).show()
            file_validation = false
        } else {
            file_validation = true
        }

    }

    private fun createPet(){

        petName = register_pet_name.text.toString()
        petAge = register_pet_age.text.toString()
        petBreed = register_breed.text.toString()
        petBio = register_pet_bio.text.toString()



        if (petName.isEmpty()) {
            Toast.makeText(activity, "Please enter your pet's name", Toast.LENGTH_SHORT).show()
            return
        }

        if( file_validation == true){
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imagenPerfil = MultipartBody.Part.createFormData("file", file?.name, requestFile);
            db.sendPetToDb(imagenPerfil, ownerId, petName, petAge, petBio, petBreed, petTypeName, gender)
        } else {
            Toast.makeText(activity, "Invalid File", Toast.LENGTH_LONG).show()
            return
        }


    }




}


