package com.trixibackend;

import express.Express;

public class Main {

    public static void main(String[] args) {
        Express app = new Express();

        app.get("/hello-world", (req, res) -> {
            res.send("Hello World");
        });

        app.listen(3000);
        System.out.println("server started on port 3000");

        Database database = new Database();

    }
}
