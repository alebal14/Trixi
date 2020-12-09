package com.trixibackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trixibackend.entity.*;
import express.Express;
import express.utils.Status;

import java.util.Map;

public class RestApi {

    Express app = new Express();
    DatabaseHandler db = new DatabaseHandler();

    public RestApi() {
        initApi();
    }

    private void initApi() {
        app.listen(3000);
        System.out.println("server started on port 3000");

        var allCollectionsName = db.getDatabase().listCollectionNames();

        allCollectionsName.forEach(collectionName -> {
            setUpGetApi(collectionName);
            setUpPostApi(collectionName);
            setUpDeleteApi(collectionName);

        });
        setUpUpdateApi();
        setLoginUser();


    }

    private void setUpUpdateApi() {

        app.post("/api/users/addFollower/:userid/:followingid", (req, res) -> {

            String userid = req.getParam("userid");
            String followingid = req.getParam("followingid");

            User user = db.getUserHandler().findUserById(userid);
            User following = db.getUserHandler().findUserById(followingid);

            System.out.println("User:  " + user);
            System.out.println("User following:  " + following);
            if (following == null) {
                Pet followingPet = db.getPetHandler().findPetById(followingid);
                System.out.println("Pet Following:  " + followingPet);
                var user1 = db.getUserHandler().updateList(user, followingPet);
                if (user1 == null) {
                    res.json(user1);
                    res.send("Error: you are already following this Pet");
                    //res.sendStatus(Status.valueOf("404"));
                    return;
                }
                res.json(user1);


            } else {
                var user1 = db.getUserHandler().updateList(user, following);
                if (user1 == null) {
                    //res.json(user1);
                    res.send("Error: you are already following this User");
                    //res.sendStatus(Status.valueOf("404"));
                    return;
                }
                res.json(user1);

            }
        });


        app.post("/api/users/unFollow/:userid/:followingid",(req,res)->{

            String userid = req.getParam("userid");
            String followingid = req.getParam("followingid");

            User user = db.getUserHandler().findUserById(userid);
            User following = db.getUserHandler().findUserById(followingid);

            if (following == null) {
                Pet followingPet = db.getPetHandler().findPetById(followingid);
                System.out.println("Pet Following:  " + followingPet);
                var user1 = db.getUserHandler().removeFromList(user, followingPet);
                if (user1 == null) {
                    res.json(user1);
                    res.send("Error: you are not following this Pet");
                    //res.sendStatus(Status.valueOf("404"));
                    return;
                }
                res.json(user1);


            } else {
                var user1 = db.getUserHandler().removeFromList(user, following);
                if (user1 == null) {
                    //res.json(user1);
                    res.send("Error: you are not following this user");
                    //res.sendStatus(Status.valueOf("404"));
                    return;
                }
                res.json(user1);

            }


        });
    }



    private void setUpDeleteApi(String collectionName) {
    }

    private void setUpPostApi(String collectionName) {
        app.post("/rest/" + collectionName, (req, res) -> {
            switch (collectionName) {
                case "users":
                    User user = (User) req.getBody(User.class);
                    res.json(db.save(user));
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

    }

    private void setLoginUser() {

        app.post("/rest/login", (req, res) ->{

            User loggedInUser = (User) req.getBody(User.class);
            User user = (User) db.getLoginByNameOrEmail(loggedInUser);



           if (user == null) {
                res.send((loggedInUser.getUserName() == null? "Email: " + loggedInUser.getEmail(): "Username: " + loggedInUser.getUserName()) + " does not exist");
                return;
            }
            if(!loggedInUser.getPassword().equals(user.getPassword())){
                res.send("password and username/email dont match");
                return;
            }

            res.json(db.getLoginByNameOrEmail(user));
        });
    }
}
