package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import com.trixibackend.entity.*;
import org.bson.types.ObjectId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.mongodb.client.model.Filters.eq;

public class PostHandler {
    private MongoCollection<Post> postColl = null;


    public PostHandler(MongoDatabase database) {
        postColl = database.getCollection("posts", Post.class);

    }

    public MongoCollection<Post> getPostColl() {
        return postColl;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = null;
        try {
            FindIterable<Post> postIter = postColl.find();
            posts = new ArrayList<>();
            postIter.forEach(posts::add);
            posts.forEach(post -> post.setUid(post.getId().toString()));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Post> listOutput =
                posts.stream()
                        .sorted(Collections.reverseOrder(Comparator.comparing(Post::getUid)))
                        .collect(Collectors.toList());

        return listOutput;
    }

    public Post findPostById(String id) {
        try {
            var postIter = postColl.find(eq("_id", new ObjectId(id)));
            var post = postIter.first();
            if (post == null) return null;
            post.setUid(post.getId().toString());

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

    public List<Post> searchPost(String searchTerm , List<User> userList, List<Pet> petList ){

        List<User> getAllUser = userList ;
        List<Pet>  getAllPet = petList;

        List<Post> allPostFromDB = getAllPosts();

        List<User> getUserName = getAllUser.stream()
                .filter(e -> e.getUserName().toLowerCase().startsWith(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        List<Pet> getPetName = getAllPet.stream()
                .filter(e -> e.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        Set<String> userid =
                getUserName.stream()
                        .map(User::getUid)
                        .collect(Collectors.toSet());

        Set<String> petid = getPetName.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<String> concatlist = Stream.concat(userid.stream(),petid.stream())
                .collect(Collectors.toList());

        List<Post> listUserPetPost =
                allPostFromDB.stream()
                        .filter(e -> concatlist.contains(e.getOwnerId()))
                        .collect(Collectors.toList());

       List<Post> listCategory =
                allPostFromDB.stream()
                        .filter(d -> d.getCategoryName() != null)
                        .filter(e -> e.getCategoryName().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());

        List<Post> listTitle =
                allPostFromDB.stream()
                        .filter(e -> e.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());

        List<Post> listDescription=
                allPostFromDB.stream()
                        .filter(d -> d.getDescription() != null)
                        .filter(e -> !e.getDescription().isEmpty())
                        .filter(s -> s.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());

        List<Post> resultList = new ArrayList<>();
        resultList.addAll(listUserPetPost);
        resultList.addAll(listCategory);
        resultList.addAll(listTitle);
        resultList.addAll(listDescription);

        return resultList;
    }

    public List<Post> findPostWhitouthUser(List<User> userList, List<Pet> petList){

        List<Post> allPostFromDB = getAllPosts();

        List<User> getAllUser = userList ;
        List<Pet>  getAllPet = petList;

        Set<String> userid =
                getAllUser.stream()
                        .map(User::getUid)
                        .collect(Collectors.toSet());

        Set<String> petid = getAllPet.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<String> concatlist = Stream.concat(userid.stream(),petid.stream())
                .collect(Collectors.toList());

        List<Post> result =
                allPostFromDB.stream()
                        .filter(e -> !concatlist.contains(e.getOwnerId()))
                        .collect(Collectors.toList());

        //result.forEach(r -> deletePost(r.getUid()));

        return result;
    }

    public List<Post> findPostByPetType(List<Pet> petsByType){
        List<Post> allPostFromDB = getAllPosts();

        Set<String> petId = petsByType.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<Post> result = allPostFromDB.stream()
                .filter(e -> petId.contains(e.getOwnerId()))
                .sorted(Collections.reverseOrder(Comparator.comparing(o -> o.getUid())))
                .collect(Collectors.toList());

        return result;
    }



    public DeleteResult deletePost(String id) {
        Bson post = eq("_id",new ObjectId(id));
        DeleteResult result = postColl.deleteOne(post);
        System.out.println(result);
        return result;
    }
}
