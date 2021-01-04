package com.example.trixi.ui.register

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.login.LoginActivity
import com.example.trixi.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class RegisterActivity : AppCompatActivity(){

    val post = PostToDb()
    var selectedImage: Uri? = null
    lateinit var bitmap : Bitmap
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var encodedImage: String = ""
    var filePath = ""
    private var mediaPath: String? = null
    private var postPath: String? = null
    var userExist: Boolean = false
    val model: TrixiViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_already_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        checkInput()

        register_profile_image.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, 0)
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

            selectedImage = data.getData()
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            assert(cursor != null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            // Set the Image in ImageView for Previewing the Media

            //Setting the image on frontend
            //bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            //register_profile_image.setImageBitmap(bitmap)

            val container = findViewById<View>(R.id.register_profile_image) as ImageView

            Picasso.get().load(selectedImage).transform(CropCircleTransformation()).centerCrop().fit().into(container)

            cursor.close()

            postPath = mediaPath
        }
    }

   /* private fun saveProfileImage(){

        //convert the image to bitmap
        val convertImageBitmap = BitmapFactory.decodeFile(postPath)

        val baos = ByteArrayOutputStream()
        //compressing the bitmap
        convertImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        //coberting the image to bytearray
        val imageByte = baos.toByteArray()

        //encoding the image
        encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT)

        //sending the image
    }*/

    private fun checkInput(){

        register_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("KOMIGEN")
                if(s?.length != 0) {
                    checkIfUserExist(s.toString())
                    println("ISTRUE" + userExist)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        register_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("KOMIGEN")
                if (s?.length != 0) {
                    checkIfUserExist(s.toString())

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        if(userExist == false){
            button_register.setOnClickListener {
                registerUser()
            }
        }
    }

    private fun registerUser(){
        val userName = register_username.text.toString()
        val email = register_email.text.toString()
        val password = register_password.text.toString()

        if (selectedImage == null){
            Toast.makeText(this, "Please select profile image", Toast.LENGTH_LONG).show()
            return
        }

        val file = File(postPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username/email/password", Toast.LENGTH_LONG).show()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Wrong email-Format, try again", Toast.LENGTH_LONG).show()
            return
        }

        post.PostRegisterUserToDb(imagenPerfil,userName, email, password, this)
    }


    private fun checkIfUserExist(userName: String){
        model.getAllUsers()?.observe(this, Observer { user ->
            Log.d("reg", "size: users : ${user?.size}")
            user?.forEach {
                if (userName.contains("@")) {
                    if (userName == it.email) {
                        Toast.makeText(this, "Email already exist", Toast.LENGTH_LONG).show()
                         userExist = true
                    }
                    else{
                        userExist = false
                    }
                }
                if (userName == it.userName) {
                    Toast.makeText(this, "Username already exist", Toast.LENGTH_LONG).show()
                    userExist = true
                    println("ISTRUEUSERNAME" + userExist)
                } else{
                    userExist = false
                }
            }
        })

    }

}