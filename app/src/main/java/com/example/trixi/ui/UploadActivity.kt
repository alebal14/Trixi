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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.ByteArrayOutputStream

class UploadActivity : AppCompatActivity() {

    val db = PostToDb()
    var selectedImage: Uri? = null
    lateinit var bitmap : Bitmap
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var encodedImage: String = ""
    var filePath = ""


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

        button_post.setOnClickListener(){
            sendImage()
        }


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
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            uploadImage.setImageBitmap(bitmap)

            cursor.close()

            postPath = mediaPath


        }
    }

    private fun sendImage(){


        //convert the image to bitmap
        val convertImageBitmap = BitmapFactory.decodeFile(postPath)

        val baos = ByteArrayOutputStream()
        //compressing the bitmap
        convertImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,   baos)

        //coberting the image to bytearray
        val imageByte = baos.toByteArray()

        //encoding the image
        encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT)

        //sending the image
        db.PostImageToDb(encodedImage)

        sendPost()
    }

    private fun sendPost(){
        val title = title_field.text.toString()
        val description = description_field.text.toString()
        val ownerId = PostToDb.loggedInUser?.uid.toString()



        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show()
            return
        }

        val post = Post("", title, description,"", ownerId, null, null)

        db.sendPostToDb(post)


    }

}