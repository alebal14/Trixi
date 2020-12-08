package com.trixibackend.entity;

public class Follow {
    private String followedUserId;
    private String followersId;

    public Follow() {
    }

    public String getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(String followedUserId) {
        this.followedUserId = followedUserId;
    }

    public String getFollowersId() {
        return followersId;
    }

    public void setFollowersId(String followersId) {
        this.followersId = followersId;
    }


}
