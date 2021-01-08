package com.example.trixi.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
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
    val post = PostToDb()
    lateinit var model: TrixiViewModel
    val loggedInUser = PostToDb.loggedInUser
    var newBio : String = ""
    private var postPath: String? = null

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

        edit_bio.hint = if(loggedInUser?.bio.isNullOrEmpty()) "Edit bio " else loggedInUser?.bio
        checkInput()
        button_update_profile.setOnClickListener {handleUpdateProfile()}

    }

    private fun checkInput() {

        edit_bio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 ){
                    newBio = s.toString()
                }
            }
        })
        
    }


    private fun handleUpdateProfile(){
        val updatedBio = edit_bio.text.toString()
        val password = edit_password.text.toString()
        val profileImage = edit_profile_image

        val file = File(postPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

    }

    private fun checkBioLength(){
        newBio = edit_bio.text.toString()
        if (newBio?.length!! < 150)
            Toast.makeText(activity, "Bio cannot be longer than 150 characters", Toast.LENGTH_SHORT).show()

    }

}