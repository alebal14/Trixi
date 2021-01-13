package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.trixibackend.entity.Report;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ReportHandler {

    private MongoCollection<Report> reportColl;


    public ReportHandler(MongoDatabase database) {
        reportColl = database.getCollection("reports", Report.class);

    }

    public MongoCollection<Report> getReportColl() {
        return reportColl;
    }

    public List<Report> getAllReports() {
        List<Report> reports= new ArrayList<>();
        try{
            FindIterable<Report> reportsIter = reportColl.find();
            reportsIter.forEach(reports::add);
            reports.forEach(report -> {
                report.setUid(report.getId().toString());
            });


        }catch (Exception e){
            e.printStackTrace();
        }

        return reports;
    }

    public DeleteResult deleteReport(String id) {
        Bson post = eq("_id",new ObjectId(id));
        DeleteResult result = reportColl.deleteOne(post);
        System.out.println(result);
        return result;
    }

}
