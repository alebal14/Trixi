package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Like;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class LikeHandler {

    private MongoCollection<Like> likeColl;

    public LikeHandler(MongoDatabase database) {
        likeColl = database.getCollection("likes", Like.class);
    }

    public MongoCollection<Like> getLikeColl() {
        return likeColl;
    }

    public List<Like> findLikesByPostId(String postId) {
        List<Like> likes = null;
        try {
            FindIterable<Like> likesIter = likeColl.find(eq("postId", postId));
            likes = new ArrayList<>();
            likesIter.forEach(likes::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return likes;
    }

}
