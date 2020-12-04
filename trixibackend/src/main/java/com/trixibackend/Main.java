package com.trixibackend;

import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import express.Express;

public class Main {

    public static void main(String[] args) {
        Express app = new Express();
        Database db = new Database();


//        User user1 = new User("Sofia","soff@gmail.com","12345","Admin");
//        db.save(user1);
        app.listen(3000);
        System.out.println("server started on port 3000");

        Post post = new Post("my dog","C:\\Users\\Snehal Patel\\Desktop\\ECU\\11.Portfolioproject\\Trixi\\trixibackend\\src\\main\\java\\com\\trixibackend\\resFolder\\images\\BT_20200111_PG1BRUNCHREVISE_4002715-1.jpg","walking with my dog");
        db.save(post);
        app.get("/rest/users", (req, res) -> {
            res.json(db.getUserHandler().getAllUsers());
        });

        app.get("/rest/users/:id", (req, res) -> {
            String id = req.getParam("id");
            var user = db.getUserHandler().findUserById(id);
            if (user == null) {
                res.send("Error: no user found");
                return;
            }

            res.json(db.getUserHandler().findUserById(id));
        });


    }
}
