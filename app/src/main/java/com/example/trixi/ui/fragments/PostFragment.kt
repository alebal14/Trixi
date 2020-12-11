package com.example.trixi.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import com.example.trixi.R
import kotlinx.android.synthetic.main.viewpager.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =// Inflate the layout for this fragment

        inflater.inflate(R.layout.fragment_post, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeCurrentFragment(CameraFragment())
    }


    /*

    private fun setupView(view: View) {
        val fragmentList : MutableList <Fragment> = ArrayList ()
        val titleList : MutableList <String> = ArrayList ()

        val adapter : ViewPagerAdapter? = activity?.supportFragmentManager?.let { ViewPagerAdapter(it) }
        adapter?.addFragment(CameraFragment(), "Camera");
        adapter?.addFragment(UploadFragment(), "Gallery");

        //view_pager?.adapter
        view_pager.adapter = adapter;
        tab_layout.setupWithViewPager(view_pager)

    }

     */





    /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter : ViewPagerAdapter? = activity?.supportFragmentManager?.let { ViewPagerAdapter(it) }
        adapter?.addFragment(CameraFragment(), "Camera");
        adapter?.addFragment(UploadFragment(), "Gallery");

        view_pager?.adapter
        view_pager.adapter = adapter;
        tab_layout.setupWithViewPager(view_pager)


    }

     */


    private fun makeCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}







