package com.trixibackend;

import com.trixibackend.entity.User;
import express.Express;

public class Main {

    public static void main(String[] args) {
        Express app = new Express();
        Database db = new Database();


        User user1 = new User("Sofia","soff@gmail.com","12345","Admin");
        db.save(user1);
        app.listen(3000);
        System.out.println("server started on port 3000");

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
