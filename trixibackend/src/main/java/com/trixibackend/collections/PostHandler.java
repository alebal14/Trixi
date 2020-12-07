package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PostHandler {
    private MongoCollection<Post> postColl = null;
    private LikeHandler likeHandler;

    public PostHandler(MongoDatabase database) {
        postColl = database.getCollection("posts", Post.class);
        likeHandler = new LikeHandler(database);
    }

    public MongoCollection<Post> getPostColl() {
        return postColl;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = null;
        try {
            FindIterable<Post> usersIter = postColl.find();
            posts = new ArrayList<>();
            usersIter.forEach(posts::add);
            posts.forEach(post ->  post.setUid(post.getId().toString()));
            posts.forEach(post -> post.setLikes(likeHandler.findLikesByPostId(post.getUid())));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }

    public List<Post> findPostsByOwner(String id) {
        List<Post> posts = null;
        try {
            FindIterable<Post> postsIter = postColl.find(eq("ownerId", id));
            posts = new ArrayList<>();
            postsIter.forEach(posts::add);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;

    }

    public Post findPostById(String id) {
        try {
            var postIter = postColl.find(eq("_id", new ObjectId(id)));
            var post = postIter.first();
            if (post == null) return null;
            post.setUid(post.getId().toString());
            post.setLikes(likeHandler.findLikesByPostId(post.getUid()));
            return post;
        } catch (Exception e) {
            return null;
        }
    }

}
