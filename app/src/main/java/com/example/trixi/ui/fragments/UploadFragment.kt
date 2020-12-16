package com.example.trixi.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.ui.UploadActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment() : Fragment() {
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


    //private var uploadImage: ImageView? = null




    //private val permissionsRequestCode = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       var rootView = inflater.inflate(R.layout.fragment_upload, container, false)

       /* activity?.let{
            when{
                permissionGranted() -> Log.e("granted--","granted")
                shouldShowRationale() -> {}// Show the rationale UI and then request permission
                else -> requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        permissionsRequestCode)
            }
        }*/

        activity?.let{
            val intent = Intent (it, UploadActivity::class.java)
            it.startActivity(intent)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupViews(view)
    }

   /* private fun shouldShowRationale() = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    // this is called when user closes the permission request dialog
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == permissionsRequestCode) {
            if (permissions[0]  == Manifest.permission.READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, do your work
            }
        }
    }*/




    /*private fun setupViews(view: View) {

        //val buttonImage = view.findViewById(R.id.uploadImage) as ImageView

        //val sendButton = view.findViewById(R.id.button_post) as Button



      /* buttonImage.setOnClickListener() {
            val frag = this
            val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, 0)
        }

        sendButton.setOnClickListener() {
            //Toast.makeText(activity, "Hejho", Toast.LENGTH_SHORT).show()
        }*/
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
    }






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








