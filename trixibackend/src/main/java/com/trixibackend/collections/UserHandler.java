package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.User;
import com.trixibackend.entity.UserPet;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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


    public User findUserByNameOrEmail(User loggedInUser) {
        String emailOrUsername;
        String fieldname;
        if(loggedInUser.getUserName() == null){
            emailOrUsername = loggedInUser.getEmail();
            fieldname = "email";
        }else{
            emailOrUsername = loggedInUser.getUserName();
            fieldname = "userName";
        }
        try {
            var user = userColl.find(eq(fieldname, emailOrUsername)).first();

           if (user == null) return null;

           user.setUid(user.getId().toString());
           return user;

        } catch (Exception e) {
            return null;
        }
    }


    //first parameter who wants to follow, second parameter the whom (I = user) want to follow
    public User updateList(User user, UserPet following) {
        user.addToFollowings(following);

        if( following instanceof User){
            ((User) following).addToFollowers(user);
            try {
                userColl.updateOne(
                        new BasicDBObject().append("_id", ((User) following).getId()),
                        new BasicDBObject().append("$set",
                                new BasicDBObject().append("followers", ((User) following).getFollowers()))
                );
            } catch (Exception e){
                e.printStackTrace();
            }
            User updatedUser = userColl.findOneAndReplace(eq("_id", ((User) following).getId()), (User) following);
        }  else if( following instanceof Pet){
            ((Pet) following).addToFollowers(user);
            try{
                petHandler.getPetColl().updateOne(
                        new BasicDBObject().append("_id", ((Pet)following).getId()),
                        new BasicDBObject().append("$set",
                                new BasicDBObject().append( "followers", ((Pet)following).getFollowers()))
                );
            } catch (Exception e){
                e.printStackTrace();
            }

            Pet updatedPet = petHandler.getPetColl().findOneAndReplace(eq("_id", ((Pet) following).getId()), (Pet) following);
        }
       /* switch (following.getClass().getSimpleName()) {
            case "User":
                //((User) following).addToFollowers(user);
                ((User) following).getFollowers().add(user);
                userColl.updateOne(
                        new BasicDBObject().append("uid", ((User)following).getUid()),
                        new BasicDBObject().append("$set",
                                new BasicDBObject().append( "followers", ((User)following).getFollowers()))
                );
                User updatedUser = userColl.findOneAndReplace(eq("_id", ((User) following).getId()), (User) following);
                break;
            case "Pet":
                //((Pet) following).addToFollowers(user);
                ((Pet) following).getFollowers().add(user);
                petHandler.getPetColl().updateOne(
                        new BasicDBObject().append("uid", ((Pet)following).getUid()),
                        new BasicDBObject().append("$set",
                                new BasicDBObject().append( "followers", ((Pet)following).getFollowers()))
                );
                Pet updatedPet = petHandler.getPetColl().findOneAndReplace(eq("_id", ((Pet) following).getId()), (Pet) following);
                break;
            default:
                break;
        }*/

        try {
            userColl.updateOne(
                    new BasicDBObject().append("_id", user.getId()),
                    new BasicDBObject().append("$set",
                            new BasicDBObject().append( "followings", user.getFollowings()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }


        User updated = userColl.findOneAndReplace(eq("_id", user.getId()), user);

        return updated;


    }


}
