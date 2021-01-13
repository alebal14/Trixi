package com.trixibackend.entity;

import org.bson.types.ObjectId;

public class Report {
    private ObjectId id;
    private String uid;
    private User reporter;
    private String reportText;
    private Post post;

    public Report(){

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", reporter=" + reporter +
                ", reportText='" + reportText + '\'' +
                ", post=" + post +
                '}';
    }
}
