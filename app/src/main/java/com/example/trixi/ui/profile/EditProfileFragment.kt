package com.example.trixi.ui.profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditProfileFragment : Fragment() {

    val db = PostToDb()
    lateinit var model: TrixiViewModel
    val loggedInUser = PostToDb.loggedInUser
    var newBio: String = ""
    private var postPath: String? = null
    private var mediaPath: String? = null
    var selectedImage: Uri? = null
    var image: MultipartBody.Part? = null
    var password: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()
        setUpClickListeners()
        checkInput()
    }

    private fun setUpUI() {
        edit_bio.hint = if (loggedInUser?.bio.isNullOrEmpty()) "Edit bio " else loggedInUser?.bio

        Picasso.get()
            .load(BASE_URL + loggedInUser?.imageUrl)
            .transform(CropCircleTransformation())
            .centerCrop()
            .fit()
            .into(edit_profile_image)
    }

    private fun setUpClickListeners() {
        button_update_profile.setOnClickListener { handleUpdateProfile() }

        edit_profile_image.setOnClickListener {
            requestPermissions()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, 0)
        }
    }

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasWriteExternalStoragePermission()) {
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                permissionsToRequest.toTypedArray(),
                0
            )
        }
    }

    private fun hasWriteExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

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

            val cursor =
                activity?.contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
            assert(cursor != null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)

            if (mediaPath != null) {
                Picasso.get()
                    .load(selectedImage)
                    .transform(CropCircleTransformation())
                    .centerCrop()
                    .fit()
                    .into(edit_profile_image)

                cursor.close()
                postPath = mediaPath
            }
            else postPath = loggedInUser?.imageUrl
        }
    }

    private fun checkInput() {

        edit_bio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0) {
                    newBio = s.toString()
                }
            }
        })

    }


    private fun handleUpdateProfile() {

        //If everything is ok input, go to update User

        if(bioLengthIsOk()) {
            if(newBio.isEmpty()) newBio = loggedInUser?.bio.toString()

            password = if (edit_password.text.toString().isEmpty()) loggedInUser?.password else edit_password.text.toString()

            //TODO: image gets deleted
            if (postPath != null) {
                val file = File(postPath!!)
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                image = MultipartBody.Part.createFormData("file", file.getName(), requestFile)
            }
            updateUser()
        }
    }

    private fun updateUser() {
        db.PostRegisterUserToDb(
            image,
            loggedInUser?.uid,
            loggedInUser?.userName!!,
            loggedInUser.email!!,
            password,
            newBio!!,
            this.requireContext()
        )
    }

    private fun bioLengthIsOk() : Boolean {
        if (newBio.length > 150) {
            Toast.makeText(activity, "Bio cannot be longer than 150 characters", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        else return true
    }

}