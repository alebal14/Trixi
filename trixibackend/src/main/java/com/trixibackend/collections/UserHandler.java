package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserHandler {
    private MongoCollection<User> userColl;
    private PostHandler postHandler;

    public UserHandler(MongoDatabase database) {
        userColl = database.getCollection("users", User.class);
        postHandler = new PostHandler(database);
    }

    public MongoCollection<User> getUserColl() {
        return userColl;
    }

    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            FindIterable<User> usersIter = userColl.find();
            users = new ArrayList<>();
            usersIter.forEach(users::add);
            users.forEach(user -> {
                user.setUid(user.getId().toString());
                user.setPosts(postHandler.findPostsByOwner(user.getUid()));
                //user.setPets(petHandler.findPetsByOwner(user.getUid()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findUserById(String id) {
        try {
            var userIter = userColl.find(eq("_id", new ObjectId(id)));
            var user = userIter.first();
            if (user == null) return null;
            user.setUid(user.getId().toString());
            user.setPosts(postHandler.findPostsByOwner(user.getUid()));
            //user.setPets(findPetsByOwner(user.getUid()));
            return user;
        } catch (Exception e) {
            return null;
        }
    }


}
