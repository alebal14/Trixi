package com.example.trixi.ui.report

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Activity
import com.example.trixi.entities.Report
import com.example.trixi.repository.TrixiViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_row.view.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*

class ReportAdapter(
    private val reports:ArrayList<Report>,
    private val fm: FragmentManager,
    private val viewLifecycleOwner: LifecycleOwner, private val listener: ((Report) -> Unit)
):RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val linearView = (LayoutInflater.from(parent.context).inflate(
            R.layout.activity_row,parent,false))
        return ReportViewHolder(linearView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {


        holder.bindData(reports[position],listener,viewLifecycleOwner)

    }

    override fun getItemCount(): Int {
        return reports.size
    }


    class ReportViewHolder(view:View):RecyclerView.ViewHolder(view),View.OnClickListener{
        override fun onClick(p0: View?) {
            Log.d("PageAct", "click on activity recycle view!")
        }
        val model = TrixiViewModel()


        fun bindData(
            report: Report,
            listener: ((Report) -> Unit)?,
            viewLifecycleOwner: LifecycleOwner
        ) {
            itemView.setOnClickListener {
                if (listener != null) {
                    listener(report)
                }
            }
           /* if(activity.comment !=null){
                model.getOneUser(activity.comment.userId)?.observe(viewLifecycleOwner,{user ->
                    Picasso.get()
                        .load(RetrofitClient.BASE_URL + (user.imageUrl))
                        .transform(CropCircleTransformation()).fit()
                        .centerCrop().into(itemView.activity_profile_img)
                    itemView.activity_sender_name.text = user.userName
                    itemView.activity_detail.text = "has commented on your post"
                    itemView.activity_comment.visibility = View.VISIBLE
                    itemView.activity_comment.text = "\"${activity.comment.comment}\""
                })
            }

            if(activity.like!=null){
                model.getOneUser(activity.like.userId)?.observe(viewLifecycleOwner,{user ->
                    Picasso.get()
                        .load(RetrofitClient.BASE_URL + (user.imageUrl))
                        .transform(CropCircleTransformation()).fit()
                        .centerCrop().into(itemView.activity_profile_img)
                    itemView.activity_sender_name.text = user.userName
                    itemView.activity_detail.text = "has liked your post"
                })
            }


            if (activity.post.fileType.toString() == "image") {
                itemView.activity_postImg.visibility = View.VISIBLE
                itemView.activity_postVideo.visibility = View.GONE
                Picasso.get()
                        .load(RetrofitClient.BASE_URL + (activity.post.filePath))
                        .fit().into(itemView.activity_postImg)

            } else {
                itemView.activity_postImg.visibility = View.GONE
                itemView.activity_postVideo.visibility = View.VISIBLE
                itemView.activity_postVideo.setSource(RetrofitClient.BASE_URL + activity.post.filePath.toString())

            }*/





        }


    }


}