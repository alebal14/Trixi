package com.example.trixi.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trixi.MainActivity
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class UploadActivity : AppCompatActivity() {

    val db = PostToDb()
    var selectedImage: Uri? = null
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var encodedImage: String = ""
    var filePath = ""

    private val mainActivity: MainActivity? = null


    private val mMediaUri: Uri? = null

    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private var mImageFileLocation = ""
    private lateinit var pDialog: ProgressDialog
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
            val intent = Intent (this, MainActivity::class.java)
            this.startActivity(intent)
        }

        button_post.setOnClickListener(){
            sendPost()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.empty_menu, menu)
        supportActionBar!!.setTitle("Upload Post")
        return super.onCreateOptionsMenu(menu)
    }

    private fun hasWriteExternalStoragePermission() =
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

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

            //Setting the image on frontend
            //bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            //uploadImage.setImageBitmap(bitmap)

            val totheView = findViewById<View>(R.id.uploadImage) as ImageView

            Picasso.get().load(selectedImage).centerCrop().fit().into(totheView)

            cursor.close()

            postPath = mediaPath


        }
    }

    /*private fun sendImage(){


        //convert the image to bitmap
        val convertImageBitmap = BitmapFactory.decodeFile(postPath)

        val baos = ByteArrayOutputStream()
        //compressing the bitmap
        convertImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,   baos)

        //converting the image to bytearray
        val imageByte = baos.toByteArray()

        //encoding the image
        encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT)

        //sending the image
        //db.PostImageToDb(encodedImage)
        Thread.sleep(1_000)
        sendPost()
    }*/

    private fun sendPost(){
        val title = title_field.text.toString()
        val description = description_field.text.toString()
        val ownerId = PostToDb.loggedInUser?.uid.toString()



        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show()
            return
        }

        val file = File(postPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

       // val post = Post("", title, description, null, ownerId, null, null)

        db.sendPostToDb(imagenPerfil, description, ownerId, title )

        //toAnotherActivity()


    }

    private fun toAnotherActivity(){
        val intent = Intent (this, MainActivity::class.java)
        intent.putExtra("EXTRA", "openSingle");

        this.startActivity(intent)
    }

}