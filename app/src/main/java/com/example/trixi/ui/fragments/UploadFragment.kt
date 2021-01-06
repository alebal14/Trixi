package com.example.trixi.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.BuildConfig
import com.example.trixi.R
import com.example.trixi.entities.Category
import com.example.trixi.entities.Pet
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UploadFragment() : Fragment() {

    var  mContext : Context? = null;

    val db = PostToDb()

    lateinit var currentFilePath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private val REQUEST_IMAGE = 3
    private val REQUEST_VIDEO = 4
    private val REQUEST_PERMISSION = 100

    val loggedInUserId = PostToDb.loggedInUser?.uid.toString()
    var ownerId: String = ""
    var categoryName:String = ""

    var selectedFile: Uri? = null
    var filePath = ""
    var mediaPath: String? = null
    var postPath: String? = null

    var fileType: String = ""


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

    return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Upload"
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        uploadVideo.visibility = View.GONE;

        btn_open_gallery_picture.setOnClickListener {
            checkGalleryPermission()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            )
            startActivityForResult(intent, REQUEST_IMAGE)
        }

        btn_open_gallery_video.setOnClickListener {
            checkGalleryPermission()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(intent, REQUEST_VIDEO)
        }

        btn_open_camera_picture.setOnClickListener {
           checkCameraPermission()
            openCameraPicture()
        }

        btn_open_camera_video.setOnClickListener{
           checkCameraPermission()
            openCameraVideo()
        }



        button_post.setOnClickListener() {
            sendPost()
        }

        //SELECT
        val model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        if (upload_spinner_add_category != null) {
            model.getAllCategories().observe(viewLifecycleOwner, { allCategory ->
                val spinnerAdapter = ArrayAdapter<Category>(
                    mContext!!,
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

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.CAMERA

            )
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    private fun checkGalleryPermission() {
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

    private fun openCameraPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(mContext!!.packageManager)?.also {
                val photoFile: File? = try {
                    createCapturedPhoto()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        mContext!!,
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
            intent.resolveActivity(mContext!!.packageManager)?.also {
                val photoFile: File? = try {
                    createCapturedVideo()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }
                photoFile?.also {
                    val videoURI = FileProvider.getUriForFile(
                        mContext!!,
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
        val storageDir = mContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentFilePath = absolutePath
        }
    }

    @Throws(IOException::class)
    private fun createCapturedVideo(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.GERMAN).format(Date())
        val storageDir = mContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("VIDEO_${timestamp}", ".mp4", storageDir).apply {
            currentFilePath = absolutePath
        }
    }

    private fun selectCategoryData(category: Category) {
        categoryName = category.name
    }

    private fun selectPetData(pet: Pet) {
        ownerId = pet.uid
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            selectedFile = data.getData()

            getCursor()

            sendPhoto()

        }

        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK && data != null) {

            selectedFile = data.getData()

            getCursor()

            sendVideo()

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            var uris = Uri.parse(currentFilePath)

            var fileCap = File(uris.toString())

            selectedFile = Uri.fromFile(fileCap)

            postPath = currentFilePath

            sendPhoto()


        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {

            postPath = currentFilePath

            sendVideo()
        }
    }

    private fun getCursor() {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity?.contentResolver?.query(selectedFile!!, filePathColumn, null, null, null)
        assert(cursor != null)
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        mediaPath = cursor.getString(columnIndex)

        cursor.close()

        postPath = mediaPath
    }

    private fun sendPhoto(){

        val totheView = view?.findViewById<View>(R.id.uploadImage) as ImageView

        Picasso.get()
            .load(selectedFile)
            .centerCrop()
            .fit()
            .into(totheView);

        uploadImage.visibility = View.VISIBLE;
        uploadVideo.visibility = View.GONE;

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

        fileType = "image"
    }

    private fun sendVideo(){
        uploadImage.visibility = View.GONE;
        uploadVideo.visibility = View.VISIBLE;

        uploadVideo.setSource(postPath)

        file = File(postPath)

        //get the file size
        val file_size = ( file?.length().toString().toDouble() / 1024 / 1024 )

        // checks if picture size is more than 100 mb
        if (file_size > 100.0){
            Toast.makeText(activity, "Video is to big, max sixe: 100 Mb", Toast.LENGTH_LONG).show()
            file_validation = false
        } else {
            file_validation = true
        }

        fileType = "video"

    }


    private fun sendPost(){
        val title = title_field.text.toString()
        val description = description_field.text.toString()


        if (title.isEmpty()) {
            Toast.makeText(activity, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        if( selectedFile == null){
            Toast.makeText(activity, "Please select a image", Toast.LENGTH_SHORT).show()
            return
        }


        if( file_validation == true){
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imagenPerfil = MultipartBody.Part.createFormData("file", file?.name, requestFile);
            db.sendPostToDb(imagenPerfil, fileType, description, ownerId, title, categoryName)
        } else {
            Toast.makeText(activity, "Invalid File", Toast.LENGTH_LONG).show()
            return
        }

    }

}








