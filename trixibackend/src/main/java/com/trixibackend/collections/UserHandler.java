package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.DatabaseHandler;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserHandler {
    private MongoCollection<User> userColl;
    private PostHandler postHandler;
    private PetHandler petHandler;
    private DatabaseHandler dbHandler;

    public UserHandler(MongoDatabase database) {
        userColl = database.getCollection("users", User.class);
        postHandler = new PostHandler(database);
        petHandler = new PetHandler(database);
        //dbHandler = new DatabaseHandler();
    }

    public MongoCollection<User> getUserColl() {
        return userColl;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            FindIterable<User> usersIter = userColl.find();
            usersIter.forEach(users::add);
            users.forEach(user -> {
                user.setUid(user.getId().toString());
                user.setPosts(postHandler.findPostsByOwner(user.getUid()));
                user.setPets(petHandler.findPetsByOwner(user.getUid()));
                user.getPosts().forEach(post -> {
                            post.setUid(post.getId().toString());
                            post.setLikes(postHandler.getLikeHandler().findLikesByPostId(post.getUid()));
                            post.setComments(postHandler.getCommentHandler().findCommentsByPostId(post.getUid()));
                        }
                );
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
            user.setPets(petHandler.findPetsByOwner(user.getUid()));
            user.getPosts().forEach(post -> {
                post.setUid(post.getId().toString());
                post.setLikes(postHandler.getLikeHandler().findLikesByPostId(post.getUid()));
                post.setComments(postHandler.getCommentHandler().findCommentsByPostId(post.getUid()));
            });
            return user;
        } catch (Exception e) {
            return null;
        }
    }

//    public User updateFollowingsList(User user, Object following) {
//        user.addToFollowings(following);
//        userColl.updateOne(
//                new BasicDBObject().append(user.getUid() + ".followings", user.getFollowings()),
//                new BasicDBObject().append("$set",
//                        new BasicDBObject().append(user.getUid() + ".followings", user.getFollowings()))
//        );
//        return dbHandler.save(user);
//
//
//    }


//    public User updateFollowersList(User user, User follower) {
//        user.addToFollowers(follower);
//        userColl.updateOne(
//                new BasicDBObject().append(user.getUid() + ".followers", user.getFollowers()),
//                new BasicDBObject().append("$set",
//                        new BasicDBObject().append(user.getUid() + ".followers", user.getFollowers()))
//        );
//        return dbHandler.save(user);
//    }

    public void updateList(User user, Object following) {
        user.addToFollowings(following);
        switch (following.getClass().getSimpleName()) {
            case "User":
                ((User) following).addToFollowers(user);
                userColl.updateOne(
                        new BasicDBObject().append("uid", ((User)following).getUid()),
                        new BasicDBObject().append("$set",
                                new BasicDBObject().append( "followers", ((User)following).getFollowers()))
                );
                break;
            case "Pet":
                ((Pet) following).addToFollowers(user);
        }

        userColl.updateOne(
                new BasicDBObject().append("uid", user.getUid()),
                new BasicDBObject().append("$set",
                        new BasicDBObject().append( "followings", user.getFollowings()))
        );




        //dbHandler.save(user);
        //dbHandler.save(following);


    }


}
