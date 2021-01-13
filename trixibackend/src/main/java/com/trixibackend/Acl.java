package com.trixibackend;

import com.mongodb.client.MongoCollection;
import com.trixibackend.entity.User;
import express.http.SessionCookie;
import express.http.request.Request;
import express.http.response.Response;
import express.utils.Status;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Acl {

    public Acl() {

    }

    public static  Boolean allowed(Request req, Response res, String collectionName, SessionCookie sessionCookie){
        var loggedInUser = (User) sessionCookie.getData();
        var method = req.getMethod();

        //user collection
        if(collectionName.equals("users")
                ||collectionName.equals("posts")
                || collectionName.equals("pets")
                ||collectionName.equals("categories")
                ||collectionName.equals("pet_types")
        ){
            if(method.equals("GET") && req.getParam("id").equals(loggedInUser.getUid())){
                return true;
            }

            if(method.equals("GET") && loggedInUser!=null){
                return true;
            }


        }





        res.setStatus(Status._403);
        res.send("Not allowed");
        return false;

    }
}
