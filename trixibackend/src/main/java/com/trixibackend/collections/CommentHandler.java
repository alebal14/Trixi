package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Comment;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class CommentHandler {
    private MongoCollection<Comment> commentColl;

    public CommentHandler(MongoDatabase database){
        commentColl = database.getCollection("comments", Comment.class);
    }

    public MongoCollection<Comment> getCommentColl() {
        return commentColl;
    }



    public List<Comment> findCommentsByPostId(String postId){
        List<Comment> comments = null;
        try{
            FindIterable<Comment> commentsIter= commentColl.find(eq("postId", postId));
            comments = new ArrayList<>();
            commentsIter.forEach(comments::add);
            comments.forEach(System.out::println);
        } catch (Exception e ){
            e.printStackTrace();
        }
        return comments;
    }

}
