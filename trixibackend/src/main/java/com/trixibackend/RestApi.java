package com.trixibackend;

import com.trixibackend.entity.Pet;
import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import express.Express;

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

                default:
                    break;
            }


        });
    }

    private void setUpGetApi(String collectionName) {

        app.get("/rest/" + collectionName, (req, res) ->

        {
            res.json(db.getAll(collectionName));
        });

        app.get("/rest/" + collectionName + "/:id", (req, res) ->

        {
            String id = req.getParam("id");
            var obj = db.getById(collectionName, id);
            if (obj == null) {
                res.send("Error: no Object found");
                return;
            }

            res.json(db.getById(collectionName, id));
        });
    }



   /* private void getUserApi() {
        app.get("/rest/users", (req, res) ->

        {
            res.json(db.getAll("users"));
        });

        app.get("/rest/users/:id", (req, res) ->

        {
            String id = req.getParam("id");
            var user = db.getUserHandler().findUserById(id);
            if (user == null) {
                res.send("Error: no user found");
                return;
            }

            res.json(db.getUserHandler().findUserById(id));
        });

        app.post("/rest/users", (req, res) -> {
            User user = (User) req.getBody(User.class);
            res.json(db.save(user));
        });
    }

    private void getPostApi() {

        app.get("/rest/allPosts", (req, res) -> {
            res.json(db.getAll("posts"));
        });

        app.get("/rest/allPosts/:ownerId",(req,res) ->{
            String ownerId = req.getParam("ownerId");
            res.json(db.getPostHandler().findPostsByOwner(ownerId));

        });

        app.post("/rest/posts", (req, res) -> {
            Post post = (Post) req.getBody(Post.class);
            res.json(db.save(post));
        });

    }*/


}
