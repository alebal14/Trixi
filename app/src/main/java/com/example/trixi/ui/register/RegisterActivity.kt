package com.example.trixi.ui.register

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean


class RegisterActivity : AppCompatActivity() {

    val post = PostToDb()
    var selectedImage: Uri? = null
    lateinit var bitmap: Bitmap
    var filePath = ""
    var mediaPath: String? = null
    var postPath: String? = null

    val model: TrixiViewModel by viewModels()
    var userExist: AtomicBoolean = AtomicBoolean(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_already_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        button_register.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray))

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
        ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasWriteExternalStoragePermission()) {
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
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
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
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

            val container = findViewById<View>(R.id.register_profile_image) as ImageView

            Picasso.get().load(selectedImage).transform(CropCircleTransformation()).centerCrop()
                .fit().into(
                    container
                )

            cursor.close()

            postPath = mediaPath
        }
    }

    private fun checkInput() {
        register_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Main).launch {
                    if (s?.length != 0 ) {
                        checkIfUserExist(s.toString())

                        delay(500)
                        if (userExist.get() == true || s?.length!! > 10 ) {
                            username_check.setColorFilter(getResources().getColor(R.color.red))


                        } else {
                            username_check.setColorFilter(getResources().getColor(R.color.green))
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        register_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Main).launch {
                    if (s?.length != 0) {
                        checkIfUserExist(s.toString())

                        delay(500)
                        if (userExist.get() == true || !Patterns.EMAIL_ADDRESS.matcher(
                                register_email.text.toString()
                            ).matches()
                        ) {
                            email_check.setColorFilter(getResources().getColor(R.color.red))
                        } else {
                            email_check.setColorFilter(getResources().getColor(R.color.green))
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        register_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Main).launch {
                    if (s?.length != 0) {
                        password_check.setColorFilter(getResources().getColor(R.color.green))
                        if (register_password.text.toString().isEmpty()) {
                            password_check.setColorFilter(getResources().getColor(R.color.gray))
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        button_register.setOnClickListener {
            if (userExist.get() != true && register_username.text.toString()
                    .isNotEmpty() && register_email.text.toString()
                    .isNotEmpty() && register_password.text.toString()
                    .isNotEmpty() && selectedImage != null
            ) {
                button_register.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )
                registerUser()
            }
            if (selectedImage == null) {
                Toast.makeText(this, "Please select profile image", Toast.LENGTH_LONG).show()
            }
            if (register_username.text.toString().isEmpty() || register_email.text.toString()
                    .isEmpty() || register_password.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please enter username/email/password", Toast.LENGTH_LONG)
                    .show()

            }
            if (register_username.text.toString().length!! > 10){
                Toast.makeText(this, "Username cannot be longer than 10 characters", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser() {
        val userName = register_username.text.toString()
        val email = register_email.text.toString()
        val password = register_password.text.toString()

        val file = File(postPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        post.PostRegisterUserToDb(imagenPerfil, null, userName, email, password, null, this)
    }


    private fun checkIfUserExist(userName: String) {
        model.getAllUsers()?.observe(this, Observer { user ->
            Log.d("reg", "size: users : ${user?.size}")
            for (u in user!!) {
                if (userName.contains("@")) {
                    if (userName == u.email) {
                        Toast.makeText(this, "Email already exist", Toast.LENGTH_SHORT).show()
                        userExist.set(true)
                        break
                    } else {
                        userExist.set(false)
                    }
                }
                if (userName == u.userName) {
                    Toast.makeText(this, "Username already exist", Toast.LENGTH_SHORT).show()
                    userExist.set(true)
                    println("ISTRUEUSERNAME" + userExist)
                    break
                } else {
                    userExist.set(false)
                }
            }
        })
    }

}