package com.example.trixi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.trixi.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavigationCameraGalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationCameraGalleryFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    //private var param1: String? = null
    //private var param2: String? = null
    private val Image_Capture_Code: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater!!.inflate(R.layout.fragment_btn_gallery_camera_toolbar, container, false)
        val goToGallery = view.findViewById<ImageButton>(R.id.btn_open_img_gallery)
        goToGallery?.setOnClickListener(this)
        val goToCamera = view.findViewById<ImageButton>(R.id.btn_take_picure)
        goToCamera?.setOnClickListener(this)

        return view;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavigationCameraGalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : NavigationCameraGalleryFragment {
            return  NavigationCameraGalleryFragment()
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_open_img_gallery ->{
                val nextFrag = UploadFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit()
              Log.d("button", "button pressed")
            }
        }
        when(v?.id){
            R.id.btn_take_picure ->{
                val nextFrag = CameraFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit()
                Log.d("button", "button pressed")
            }
        }
    }


}








