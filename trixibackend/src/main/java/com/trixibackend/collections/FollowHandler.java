package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Follow;
import com.trixibackend.entity.Like;
import com.trixibackend.entity.User;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class FollowHandler {
    private MongoCollection<Follow> followColl;

    public FollowHandler(MongoDatabase database){
        followColl = database.getCollection("follow", Follow.class);
    }

    public MongoCollection<Follow> getFollowColl() {
        return followColl;
    }

    public List<User> getFollowers(String id){

        /*List<User> followers = new ArrayList<>();
        try {
            FindIterable<User> likesIter = followColl.find(eq("followerId", id));
            likesIter.forEach(likes::add);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return likes;*/
        return null;
    }


}
