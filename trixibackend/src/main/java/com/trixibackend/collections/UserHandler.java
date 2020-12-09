package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.User;
import com.trixibackend.entity.UserPet;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mongodb.client.model.Filters.eq;

public class UserHandler {
    private MongoCollection<User> userColl;
    private PostHandler postHandler;
    private PetHandler petHandler;


    public UserHandler(MongoDatabase database) {
        userColl = database.getCollection("users", User.class);
        postHandler = new PostHandler(database);
        petHandler = new PetHandler(database);

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

    //first parameter who wants to follow, second parameter the whom (I = user) want to follow
    public User updateList(User user, UserPet following) {
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowings().forEach(u -> {
            if (u.getUid().equals(following.getUid())) {
                found.set(true);
            }
        });

        if (found.get() == false) {
            return null;
        }


        //user.addToFollowings(following);

        if (following instanceof User) {
            User u = (User) following;
            u.setFollowings(null);
            u.setFollowers(null);
            u.setPets(null);
            u.setPosts(null);
            userColl.updateOne(eq("uid", user.getUid()), Updates.addToSet("followings", u));

            userColl.updateOne(eq("uid", u.getUid()), Updates.addToSet("followers", user));
//            ((User) following).addToFollowers(user);
//            try {
//                userColl.updateOne(
//                        new BasicDBObject().append("_id", ((User) following).getId()),
//                        new BasicDBObject().append("$set",
//                                new BasicDBObject().append("followers", ((User) following).getFollowers()))
//                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //User updatedUser = userColl.findOneAndReplace(eq("_id", ((User) following).getId()), (User) following);
        } else if (following instanceof Pet) {
            Pet p = (Pet) following;
            p.setFollowers(null);
            p.setPosts(null);
            userColl.updateOne(eq("uid", user.getUid()), Updates.addToSet("followings", p));

            petHandler.getPetColl().updateOne(eq("uid", p.getUid()), Updates.addToSet("followers", user));
//            ((Pet) following).addToFollowers(user);
//            try {
//                petHandler.getPetColl().updateOne(
//                        new BasicDBObject().append("_id", ((Pet) following).getId()),
//                        new BasicDBObject().append("$set",
//                                new BasicDBObject().append("followers", ((Pet) following).getFollowers()))
//                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
            //Pet updatedPet = petHandler.getPetColl().findOneAndReplace(eq("_id", ((Pet) following).getId()), (Pet) following);
        }

//        try {
//            userColl.updateOne(
//                    new BasicDBObject().append("_id", user.getId()),
//                    new BasicDBObject().append("$set",
//                            new BasicDBObject().append("followings", user.getFollowings()))
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
        // User updated = userColl.findOneAndReplace(eq("uid", user.getUid()), user);

        User updatedUser = findUserById(user.getUid());
        return updatedUser;

    }

    public User removeFromList(User user, UserPet following) {
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowings().forEach(u -> {
            if (u.getUid().equals(following.getUid())) {
                found.set(true);
            }
        });

        if (found.get() == false) {
            return null;
        }


        if (following instanceof User) {
            User u = (User) following;

            userColl.updateOne(eq("uid", user.getUid()), Updates.pull("followings", u));

            userColl.updateOne(eq("uid", u.getUid()), Updates.pull("followers", user));




        } else if (following instanceof Pet) {
            Pet p = (Pet) following;
            userColl.updateOne(eq("uid", user.getUid()), Updates.pull("followings", p));

            petHandler.getPetColl().updateOne(eq("uid", p.getUid()), Updates.pull("followers", user));


        }
        User updatedUser = findUserById(user.getUid());
        return updatedUser;


    }


}
