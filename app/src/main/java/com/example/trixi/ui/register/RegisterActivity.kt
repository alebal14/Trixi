package com.example.trixi.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Base64.getDecoder
import android.util.Base64.decode as decode

class RegisterActivity : AppCompatActivity() {

    val post = PostToDb()
    var selectedPhotouri: Uri? = null
    lateinit var bitmap : Bitmap
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var encodedImage: String = ""
    var filePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_already_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        register_profile_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 0)
        }

        button_register.setOnClickListener {
            saveProfileImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //check what the selected image is

            var selectedImage = data.getData()
            if (selectedImage != null) {
              filePath = selectedImage.path.toString()
            }

            println(filePath)

            val file = File(filePath)
            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload_test")

            println("reqFile: " + reqFile.toString())
            println("body: " + body.toString())

            selectedPhotouri = data.data

            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotouri)
             register_profile_image.setImageBitmap(bitmap)

        }
    }

    private fun saveProfileImage(){
        if (selectedPhotouri == null){
            Toast.makeText(this, "Please select profile image", Toast.LENGTH_LONG).show()
            return
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)

        val imageInByte: ByteArray = byteArrayOutputStream.toByteArray()
        encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT)


        registerUser()
    }

    private fun registerUser(){
        val userName = register_username.text.toString()
        val email = register_email.text.toString()
        val password = register_password.text.toString()

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username/email/password", Toast.LENGTH_LONG).show()
            return
        }

        val user = User("",userName, email, password, "",encodedImage,"user",null, null)
            println("here")
        post.PostRegisterUserToDb(user)

    }
}