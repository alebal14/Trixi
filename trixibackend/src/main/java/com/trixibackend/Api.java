package com.trixibackend;

import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import express.Express;

public class Api {

    Express app = new Express();
    Database db = new Database();

    public Api() {
        initApi();
//        Post post = new Post("my parrot","src/main/java/com/trixibackend/resFolder/images/group-portrait-adorable-puppies_53876-64778.jpg","my Parrot is cute","5fca167381be7703497db5cd");
//        db.save(post);
    }

    private void initApi() {
        app.listen(3000);
        System.out.println("server started on port 3000");

        getUserApi();
        getPostApi();

    }

    private void getUserApi() {
        app.get("/rest/users", (req, res) ->

        {
            res.json(db.getUserHandler().getAllUsers());
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
            res.json(db.getPostHandler().getAllPosts());
        });

        app.get("/rest/allPosts/:ownerId",(req,res) ->{
            String ownerId = req.getParam("ownerId");
            res.json(db.getPostHandler().findPostsByOwner(ownerId));

        });

        app.post("/rest/posts", (req, res) -> {
            Post post = (Post) req.getBody(Post.class);
            res.json(db.save(post));
        });

    }


}
