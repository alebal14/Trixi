package com.example.trixi.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.BuildConfig
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UploadActivity : AppCompatActivity() {

    val db = PostToDb()

    lateinit var currentPhotoPath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private val REQUEST_IMAGE = 3
    private val REQUEST_VIDEO = 4
    private val REQUEST_PERMISSION = 100

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

        btn_open_gallery_picture.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            )
            startActivityForResult(intent, REQUEST_IMAGE)
        }

        btn_open_gallery_video.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(intent, REQUEST_VIDEO)
        }

        btn_open_camera_picture.setOnClickListener {
            openCameraPicture()
        }

        btn_open_camera_video.setOnClickListener{
            openCameraVideo()
        }


        button_cancel.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        button_post.setOnClickListener() {
            sendPost()
        }

        //SELECT
        val model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        if (upload_spinner_add_category != null) {
            model.getAllCategories().observe(this, { allCategory ->
                val spinnerAdapter = ArrayAdapter<Category>(
                        this,
                        android.R.layout.simple_spinner_item,
                        allCategory
                )
                upload_spinner_add_category.adapter = spinnerAdapter
            })

            upload_spinner_add_category.onItemSelectedListener = object :
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

        if (upload_spinner_add_pet != null) {
            var petList = model.getPetsByOwner(loggedInUserId)
            var petdefault = (Pet("0", null, "", "Select Pet", "", 0, "", "", null, ""))

            petList?.observe(this, { allPets ->
                    if (allPets!!.isEmpty()) {
                        upload_spinner_add_pet.visibility = View.GONE;
                    } else {

                        val spinnerAdapter = ArrayAdapter<Pet>(
                                this,
                                android.R.layout.simple_spinner_item,
                                allPets
                        )

                        spinnerAdapter.sort(compareBy { it.name })
                        spinnerAdapter.insert(petdefault, 0)
                        upload_spinner_add_pet.adapter = spinnerAdapter

                    }
            })

            upload_spinner_add_pet.onItemSelectedListener = object :
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

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION)
        }
    }

    private fun openCameraPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createCapturedPhoto()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                            this,
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun openCameraVideo() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createCapturedVideo()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }
                photoFile?.also {
                    val videoURI = FileProvider.getUriForFile(
                            this,
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
                }
            }
        }

    }

    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.GERMAN).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    @Throws(IOException::class)
    private fun createCapturedVideo(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.GERMAN).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("VIDEO_${timestamp}", ".mp4", storageDir).apply {
            currentPhotoPath = absolutePath
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

            selectedImage = data.getData()

            Toast.makeText(this, " selectimage: $selectedImage", Toast.LENGTH_SHORT).show()
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            var uris = Uri.parse(currentPhotoPath)
            selectedImage = uris

            val totheView = findViewById<View>(R.id.uploadImage) as ImageView

            var fileCap = File(selectedImage.toString())

            Picasso.get()
                    .load(Uri.fromFile(fileCap))
                    .centerCrop()
                    .fit()
                    .into(totheView);

        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, "Video Capture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVideo(){

    }

    private fun sendPhoto(){

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

       //get the file size
        val file_size = ( file.length().toString().toDouble() / 1024 / 1024 )

        // checks if picture size is more than 5 mb
       if (file_size > 5.0){
            Toast.makeText(this, "Picture is to big, max sixe: 5 Mb", Toast.LENGTH_SHORT).show()
            return
        }

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




