package com.trixibackend.entity;

import org.bson.types.ObjectId;

public class UserPet {

    public ObjectId id;
    public String uid;

    public UserPet() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}