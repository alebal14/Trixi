package com.example.trixi.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Comment
import com.example.trixi.entities.Post
import com.example.trixi.entities.Report
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.android.synthetic.main.fragment_report_content.*


class PopUpSendReportWindow(var post: Post?) :
    DialogFragment() {

    private val db = PostToDb()

    companion object {
        const val TAG = "popUpReport"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_report_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_report.setOnClickListener {
            sendReport()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun sendReport() {
        val reportText = enter_report_content.text.toString()
        val user = PostToDb.loggedInUser!!

        if (reportText.isEmpty()) {
            Toast.makeText(context, "Please write a comment", Toast.LENGTH_LONG).show()
            return
        }

        if (reportText.length > 500) {
            Toast.makeText(activity, "Report message cannot be longer than 500 characters", Toast.LENGTH_SHORT)
                    .show()
            return
        }

        val reportObj = Report("", user, reportText, post!! )
        db.addReportToDb(reportObj)

        Toast.makeText(context, "Thank you! Your report has been sent to Trixi and will be reviewed.", Toast.LENGTH_LONG).show()
        dialog!!.dismiss()


    }
}


