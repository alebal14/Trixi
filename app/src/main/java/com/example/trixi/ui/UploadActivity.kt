package com.example.trixi.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.MainActivity
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Category
import com.example.trixi.entities.Pet
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class UploadActivity : AppCompatActivity() {

    val db = PostToDb()




    val loggedInUserId = PostToDb.loggedInUser?.uid.toString()
    var ownerId: String = ""
    var categoryName:String = ""

    var selectedImage: Uri? = null
    var filePath = ""
    private var mediaPath: String? = null
    private var postPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        RetrofitClient.context = this

        uploadImage.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, 0)
        }

        button_cancel.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        button_post.setOnClickListener() {
            sendPost()
        }

        val model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        var categorySpinner = findViewById<Spinner>(R.id.upload_spinner_add_category)
        var petSpinner =  findViewById<Spinner>(R.id.upload_spinner_add_pet)

        if (categorySpinner != null) {
            model.getAllCategories().observe(this, { allCategory ->
                val spinnerAdapter = ArrayAdapter<Category>(
                    this,
                    android.R.layout.simple_spinner_item,
                    allCategory
                )
                categorySpinner.adapter = spinnerAdapter
            })

            categorySpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val category: Category = parent.selectedItem as Category
                    selectCategoryData(category)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        if (petSpinner != null) {


            var petList = model.getPetsByOwner(loggedInUserId)

            var petdefault = (Pet("0", null, "", "Select Pet", "", 0, "", "", null, "" ))


            Log.d("petList", "${petList.toString()}")

                petList?.observe(this, { allPets ->
                    if (allPets!!.isEmpty()) {
                        petSpinner.visibility = View.GONE;
                    } else {

                        val spinnerAdapter = ArrayAdapter<Pet>(
                            this,
                            android.R.layout.simple_spinner_item,
                            allPets
                        )

                        spinnerAdapter.sort(compareBy { it.name })
                        spinnerAdapter.insert(petdefault,0)
                        petSpinner.adapter = spinnerAdapter

                    }
                })


            petSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val pet: Pet = parent.selectedItem as Pet
                    if (position == 0) {
                        ownerId = loggedInUserId;
                    } else {
                        selectPetData(pet)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }

                }

            }
    }


    private fun selectCategoryData(category: Category) {
        categoryName = category.name
    }

    private fun selectPetData(pet: Pet) {
        ownerId = pet.uid
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.empty_menu, menu)
        supportActionBar!!.setTitle("Upload Post")
        return super.onCreateOptionsMenu(menu)
    }

    private fun hasWriteExternalStoragePermission() =
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if(!hasWriteExternalStoragePermission()){
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
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

            var selectedImage = data.getData()
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            assert(cursor != null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            // Set the Image in ImageView for Previewing the Media

            val totheView = findViewById<View>(R.id.uploadImage) as ImageView

            Picasso.get().load(selectedImage).centerCrop().fit().into(totheView)

            cursor.close()

            postPath = mediaPath

        }
    }



    private fun sendPost(){
        val title = title_field.text.toString()
        val description = description_field.text.toString()



        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        if( selectedImage == null){
            Toast.makeText(this, "Please select a image", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(postPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);




       db.sendPostToDb(imagenPerfil, description, ownerId, title, categoryName)

        //toAnotherActivity()


    }

    private fun toAnotherActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("EXTRA", "openSingle");

        this.startActivity(intent)
    }

}




