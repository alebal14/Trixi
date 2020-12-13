package com.example.trixi.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_upload.*
import java.io.ByteArrayOutputStream
import androidx.core.app.ActivityCompat as ActivityCompat1


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment(context: Context) : Fragment() {
    /*// TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null*/

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    val sendForm = PostToDb()
    val post = PostToDb()
    var selectedPhotouri: Uri? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadImage.setOnClickListener(this)



    }

    private fun startFragment(view: View) {

        Log.d("test", "test1")

        sendToPost()

        uploadImage.setOnClickListener {
            Log.d("test", "test2")
            requestPermissions()
            val intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            /* intent.type = "image/*"
             intent.setAction(Intent.ACTION_GET_CONTENT)*/
            */

            startActivityForResult(intent, 0)
        }
    }


    private fun hasWriteExternalStoragePermission() =
            context?.let { ActivityCompat1.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if(!hasWriteExternalStoragePermission()){
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat1.requestPermissions(context as Activity, permissionsToRequest.toTypedArray(), 0)
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


    var selectedPicture: Uri? = null;

    private fun sendToPost() {

        val title = title_field.text.toString();
        val description = description_field.text.toString();
        val ownerId =  "fdsfsd";

        button_post.setOnClickListener {
            val post = Post("", title, description, "", ownerId, null , null)
            sendForm.postPostToDb(post)
        }
    }

   /* private fun selectPicture() {
        uploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 0)
        }
    }*/

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            //the image URI
            val selectedImage = data.data

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = context?.contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
            assert(cursor != null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            // Set the Image in ImageView for Previewing the Media

            //Setting the image on frontend
            bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedImage)
            register_profile_image.setImageBitmap(bitmap)
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
        }
    }*/




/*companion object {
 /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        UploadFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
}*/

}




