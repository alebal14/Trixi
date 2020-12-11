package com.trixibackend;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trixibackend.entity.*;
import express.Express;
import express.http.Cookie;
import express.http.SessionCookie;
import express.middleware.Middleware;
import express.utils.Status;
import express.utils.Status;
import org.apache.commons.fileupload.FileItem;
import org.bson.internal.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RestApi {

    Express app = new Express();
    DatabaseHandler db = new DatabaseHandler();

    public RestApi() {
        initApi();
    }

    private void initApi() {
        app.listen(3000);
        // sets a unique cookie on each client to track authentication
        app.use(Middleware.cookieSession("f3v4", 60 * 60 * 24 * 7));
        System.out.println("server started on port 3000");

        var allCollectionsName = db.getDatabase().listCollectionNames();

        allCollectionsName.forEach(collectionName -> {
            setUpGetApi(collectionName);
            setUpPostApi(collectionName);
            setUpDeleteApi(collectionName);

        });
        setUpUpdateApi();
        setLoginUser();
        getLoggedinUser();
        logoutUser();


    }

    private void setUpUpdateApi() {

        app.post("/api/users/follow/:userid/:followingId", (req, res) -> {

            String userid = req.getParam("userid");
            String followingId = req.getParam("followingId");

            User user = db.getUserHandler().findUserById(userid);
            User followingUser = db.getUserHandler().findUserById(followingId);

            System.out.println("User:  " + user);

            if (followingUser == null) {
                Pet followingPet = db.getPetHandler().findPetById(followingId);
                System.out.println("(Pet) Following:  " + followingPet);
                var updatedUser = db.getUserHandler().updateFollowPetList(user, followingPet);
                if (updatedUser == null) {
                    res.setStatus(Status._403);
                    res.send("Error: you are already following this Pet");
                    return;
                }
                res.json(updatedUser);
            } else {
                System.out.println("(User) following:  " + followingUser);
                var updatedUser = db.getUserHandler().updateFollowUserList(user, followingUser);
                if (updatedUser == null) {
                    res.setStatus(Status._403);
                    res.send("Error: you are already following this User");
                    return;
                }
                res.json(updatedUser);

            }
        });



        app.post("/api/users/un_follow/:userid/:followingId", (req, res) -> {

            String userid = req.getParam("userid");
            String followingId = req.getParam("followingId");

            User followingUser = db.getUserHandler().findUserById(followingId);
            User user = db.getUserHandler().findUserById(userid);

            System.out.println("User:  " + user);

            if (followingUser == null) {
                Pet followingPet = db.getPetHandler().findPetById(followingId);
                System.out.println("(Pet) unfollow:  " + followingPet);
                var updatedUser = db.getUserHandler().removeFromFollowPetList(user, followingPet);
                if (updatedUser == null) {
                    res.setStatus(Status._403);
                    res.send("Error: you are not following this Pet");
                    return;
                }
                res.json(updatedUser);
            } else {
                System.out.println("(User) unfollow:  " + followingUser);
                var updatedUser = db.getUserHandler().removeFromFollowUserList(user, followingUser);
                if (updatedUser == null) {
                    res.setStatus(Status._403);
                    res.send("Error: you are not following this user");
                    return;
                }
                res.json(updatedUser);

            }
        });

    }


    private void setUpDeleteApi(String collectionName) {
    }

    private void setUpPostApi(String collectionName) {
        app.post("/rest/" + collectionName, (req, res) -> {
            switch (collectionName) {
                case "users":

                  //Image image = (Image) req.getBody(Image.class);
                   // System.out.println("image: " + image.getFile());
                    User user = new User();
                  //  final String currentDir = System.getProperty("user.dir");
                   // String pathFile = currentDir + "/src/main/java/images";

                    String fileUrl = null;
                    try{

                        List<FileItem> files = req.getFormData("files");
                        System.out.println("here");




                        if(files == null){
                            res.send("files null");
                            return;
                        }

                        fileUrl = db.getUserHandler().uploadProfileImage(files.get(0));
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                   // user.setImageUrl(fileUrl);
                    //String hashedPassword = BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray());
                    //user.setPassword(hashedPassword);
                    res.json(user);
                    //res.json(db.save(user));
                    break;
                case "posts":
                    Post post = (Post) req.getBody(Post.class);
                    res.json(db.save(post));
                    break;
                case "pets":
                    Pet pet = (Pet) req.getBody(Pet.class);
                    res.json(db.save(pet));
                    break;
                case "likes":
                    Like like = (Like) req.getBody(Like.class);
                    res.json(db.save(like));
                    break;
                case "comments":
                    Comment comment = (Comment) req.getBody(Comment.class);
                    res.json(db.save(comment));
                    break;
                case "categories":
                    Category category = (Category) req.getBody(Category.class);
                    res.json(db.save(category));
                    break;
                case "pet_types":
                    PetType petType = (PetType) req.getBody(PetType.class);
                    res.json(db.save(petType));
                default:
                    break;
            }
        });
    }

    private void setUpGetApi(String collectionName) {

        app.get("/rest/" + collectionName, (req, res) -> res.json(db.getAll(collectionName)));
        app.get("/rest/posts/by_category/:id", (req, res) -> {

            String id = req.getParam("id");
            var obj = db.getPostHandler().getPostsByCategory(id);
            if (obj == null) {
                res.send("Error: no Object found");
                return;
            }
            res.json(db.getPostHandler().getPostsByCategory(id));
        });

        app.get("/rest/pets/by_pet_type/:id", (req, res) -> {

            String id = req.getParam("id");
            var obj = db.getPetHandler().findPetsByPetType(id);
            if (obj == null) {
                res.send("Error: no Object found");
                return;
            }
            res.json(db.getPetHandler().findPetsByPetType(id));
        });


        //b
        app.get("/rest/" + collectionName + "/by_owner/:id", (req, res) -> {

            String id = req.getParam("id");
            var obj = db.getByOwner(collectionName, id);
            if (obj == null) {
                res.send("Error: no Object found");
                return;
            }
            res.json(db.getByOwner(collectionName, id));
        });

        app.get("/rest/" + collectionName + "/:id", (req, res) -> {

            String id = req.getParam("id");
            var obj = db.getById(collectionName, id);
            if (obj == null) {
                res.send("Error: no Object found");
                return;
            }
            res.json(db.getById(collectionName, id));
        });

        app.get("/api/getUserFollowingPost/:id", (req, res) -> {

            String id = req.getParam("id");

            User user = db.getUserHandler().findUserById(id);

            var updatedUser = db.getUserHandler().findUserFollowingPostList(user);
            if (updatedUser == null) {
                res.setStatus(Status._403);
                res.send("Error: you are not following this Pet");
                return;
            }
            res.json(updatedUser);
        });


    }

    private void setLoginUser() {

        app.post("/rest/login", (req, res) ->{

            var sessionCookie = (SessionCookie) req.getMiddlewareContent("sessioncookie");

            if(sessionCookie.getData() != null) {
                res.send("Already logged in");
                return;
            }

            User loggedInUser = (User) req.getBody(User.class);
            User user = (User) db.getLoginByNameOrEmail(loggedInUser);

           if (user == null) {
                res.send((loggedInUser.getUserName() == "" || loggedInUser.getUserName() == null? "Email: " + loggedInUser.getEmail(): "Username: " + loggedInUser.getUserName()) + " does not exist");
                return;
            }

            var result = BCrypt.verifyer().verify(loggedInUser.getPassword().toCharArray(), user.getPassword().toCharArray());
            if(!result.verified) {
                res.setStatus(Status._401);
                res.send("password and username/email dont match");
                res.json(user);
                return;
            }

            sessionCookie.setData(user);
            user.setPassword(null); // sanitize password
            res.json(user);
        });
    }

    private void getLoggedinUser(){
        app.get("/rest/login", (req, res) ->{

            var sessionCookie = (SessionCookie) req.getMiddlewareContent("sessioncookie");

            if(sessionCookie.getData() == null) {
                res.send("Not logged in");
                return;
            }

            var user = (User) sessionCookie.getData();
            user.setPassword(null); // sanitize password
            res.json(user);

        });

    }

    private void logoutUser(){
        app.get("/rest/logout", (req, res) -> {
            var sessionCookie = (SessionCookie) req.getMiddlewareContent("sessioncookie");
            sessionCookie.setData(null);
            res.send("Successfully logged out");
        });
    }
}
