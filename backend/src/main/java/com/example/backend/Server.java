package com.example.backend;

import express.Express;

public class Server {

    public static void main(String[] args) {
        Express app = new Express();

        app.get("/hello-world", (req, res) -> {
            res.send("Hello World");
        });

        app.listen(3000);
        System.out.println("server started on port 3000");



    }
}