package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.trixibackend.entity.Comment;
import com.trixibackend.entity.Like;
import com.trixibackend.entity.Post;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mongodb.client.model.Filters.eq;

public class PostHandler {
    private MongoCollection<Post> postColl = null;
    private LikeHandler likeHandler;


    private CommentHandler commentHandler;


    public PostHandler(MongoDatabase database) {
        postColl = database.getCollection("posts", Post.class);
        likeHandler = new LikeHandler(database);
        commentHandler = new CommentHandler(database);
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
            posts.forEach(post -> post.setUid(post.getId().toString()));
//            posts.forEach(post -> post.setLikes(likeHandler.findLikesByPostId(post.getUid())));
//            posts.forEach(post -> post.setComments(commentHandler.findCommentsByPostId(post.getUid())) );

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
            posts.forEach(post -> post.setUid(post.getId().toString()));

//            posts.forEach(post -> post.setLikes(likeHandler.findLikesByPostId(post.getUid())));
//            posts.forEach(post -> post.setComments(commentHandler.findCommentsByPostId(post.getUid())) );
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
//            post.setLikes(likeHandler.findLikesByPostId(post.getUid()));
//            post.setComments(commentHandler.findCommentsByPostId(post.getUid()));
            return post;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Post> getPostsByCategory(String categoryId) {
        List<Post> posts = null;
        try {
            FindIterable<Post> postsIter = postColl.find(eq("categoryId", categoryId));
            posts = new ArrayList<>();
            postsIter.forEach(posts::add);
//            posts.forEach(post -> post.setLikes(likeHandler.findLikesByPostId(post.getUid())));
//            posts.forEach(post -> post.setComments(commentHandler.findCommentsByPostId(post.getUid())) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post addLike(Like like) {
        String postId = like.getPostId();
        Post p = findPostById(postId);
        AtomicBoolean exist = new AtomicBoolean(false);
        p.getLikes().forEach(l -> {
            if (like.getUserId().equals(l.getUserId())) {
                exist.set(true);
            }
        });

        if (exist.get()) {
            return null;
        } else {
            postColl.updateOne(eq("_id", p.getId()), Updates.addToSet("likes", like));
            return p;
        }
    }

    public Post unlike(Like like) {
        String postId = like.getPostId();
        Post p = findPostById(postId);
        AtomicBoolean exist = new AtomicBoolean(false);
        p.getLikes().forEach(l -> {
            if (like.getUserId().equals(l.getUserId())) {
                exist.set(true);
            }
        });
        if (exist.get()) {
            postColl.updateOne(eq("_id", p.getId()), Updates.pull("likes", like));
            return p;
        } else {
            return null;
        }

    }

    public Post addComment(Comment comment) {
        String postId = comment.getPostId();
        Post p = findPostById(postId);

        postColl.updateOne(eq("_id", p.getId()), Updates.addToSet("comments", comment));
        return p;

    }

    public Post deleteComment(Comment comment){
        String postId = comment.getPostId();
        Post p = findPostById(postId);
        AtomicBoolean exist = new AtomicBoolean(false);
        p.getComments().forEach(c -> {
            if (c.getId().equals(comment.getId())) {
                exist.set(true);
            }
        });
        if(exist.get()){
            postColl.updateOne(eq("_id",p.getId()),Updates.pull("comments",comment));
            return p;
        }else{
            return null;
        }


    }


    public LikeHandler getLikeHandler() {
        return likeHandler;
    }

    public CommentHandler getCommentHandler() {
        return commentHandler;
    }

}
