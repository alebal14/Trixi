package com.example.trixi.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.entities.Report
import com.example.trixi.repository.DeleteFromDb
import com.example.trixi.repository.PostToDb
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_report_content.*
import kotlinx.android.synthetic.main.popup_report.*


class PopUpReportWindow( private var report: Report?) :
    DialogFragment() {

    private val dbDelete = DeleteFromDb()

    companion object {
        const val TAG = "popUpReport"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.popup_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateReport()

        button_approve_post.setOnClickListener {
            deleteReport(report)
        }

        button_delete_post.setOnClickListener {
            deletePost(report)
        }

    }

    private fun deleteReport(deleteReport: Report?) {
        dbDelete.deleteAReportFromDb(deleteReport!!.uid)

    }

    private fun deletePost(deleteReport: Report?) {
        if (deleteReport != null) {
            deleteReport.post.uid?.let { dbDelete.deleteAPostFromDb(it) }
        }
        deleteReport(deleteReport)
    }

    private fun populateReport() {

        Picasso.get()
            .load(RetrofitClient.BASE_URL + (report!!.reporter.imageUrl))
            .transform(CropCircleTransformation()).fit()
            .centerCrop().into(report_profileimg)

        report_profileName.text = report!!.reporter.userName
        report_text.text = report!!.reportText
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

        //val reportObj = Report("", user, reportText, post!! )
        //db.addReportToDb(reportObj)

        Toast.makeText(context, "Thank you! Your report has been sent to Trixi and will be reviewed.", Toast.LENGTH_LONG).show()
        dialog!!.dismiss()


    }
}


