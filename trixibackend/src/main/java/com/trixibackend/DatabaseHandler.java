package com.trixibackend;


import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.trixibackend.collections.*;
import com.trixibackend.entity.*;
import org.apache.commons.fileupload.FileItem;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseHandler {

    MongoDatabase database;
    private UserHandler userHandler = null;
    private PostHandler postHandler = null;
    private PetHandler petHandler = null;
    private LikeHandler likeHandler = null;
    private CommentHandler commentHandler = null;
    private CategoryHandler categoryHandler = null;
    private PetTypeHandler petTypeHandler = null;

    MongoCollection<User> userColl = null;
    MongoCollection<Post> postColl = null;
    MongoCollection<Pet> petColl = null;
    MongoCollection<Like> likeColl = null;
    MongoCollection<Comment> commentColl = null;
    MongoCollection<Category> categoryColl = null;
    MongoCollection<PetType> petTypeColl = null;

    Map<Type, MongoCollection> collections = new HashMap<>();

    private final String atlasUrl = "mongodb+srv://Snehal:1234@cluster0.cemx5.mongodb.net/trixi?retryWrites=true&w=majority";

    private final String dbname = "trixi";

    public DatabaseHandler() {
        init();
    }

    private void init() {
        // Connect to MongoDB atlas
        ConnectionString connString = new ConnectionString(atlasUrl);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(dbname);

        // Register codec to support POJO operations (Plain Old Java Object)
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        database = database.withCodecRegistry(pojoCodecRegistry);

        // get users collection linked with POJO

        userHandler = new UserHandler(database);
        postHandler = new PostHandler(database);
        petHandler = new PetHandler(database);
        likeHandler = new LikeHandler(database);
        commentHandler = new CommentHandler(database);
        categoryHandler = new CategoryHandler(database);
        petTypeHandler = new PetTypeHandler(database);

        userColl = userHandler.getUserColl();
        postColl = postHandler.getPostColl();
        petColl = petHandler.getPetColl();
        likeColl = likeHandler.getLikeColl();
        commentColl = commentHandler.getCommentColl();
        categoryColl = categoryHandler.getCategoryColl();
        petTypeColl = petTypeHandler.getPetTypeColl();

        // generic collections
        collections.putIfAbsent(User.class, userColl);
        collections.putIfAbsent(Post.class, postColl);
        collections.putIfAbsent(Pet.class, petColl);
        collections.putIfAbsent(Like.class, likeColl);
        collections.putIfAbsent(Comment.class, commentColl);
        collections.putIfAbsent(Category.class, categoryColl);
        collections.putIfAbsent(PetType.class, petTypeColl);


    }

    public <T> T save(Object object) {
        ObjectId id = null;

        try {
            Field privateStringField = object.getClass().getDeclaredField("id");
            privateStringField.setAccessible(true);
            id = (ObjectId) privateStringField.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        var coll = collections.get(object.getClass());

        T updated = (T) coll.findOneAndReplace(eq("_id", id), object);
        if (updated == null) {

            var res = coll.insertOne(object);
            updated = (T) object;

        }

        return updated;
    }

    public List<? extends Object> getAll(String collectionName) {
        switch (collectionName) {
            case "users":
                return userHandler.getAllUsers();
            case "posts":
                return postHandler.getAllPosts();
            case "pets":
                return petHandler.getAllPets();
            case "categories":
                return categoryHandler.getAllCategories();
            case "pet_types":
                return petTypeHandler.getAllPetTypes();
            default:
                return null;
        }

    }

    public DeleteResult deleteById(String collectionName, String id){
        switch (collectionName){
            case "users":
                return userHandler.deleteUser(id);
            case "posts":
                return postHandler.deletePost(id);
            case "pets":
                return petHandler.deletePet(id);
            default:
                return null;
        }
    }

    public Object getById(String collectionName, String id) {
        switch (collectionName) {
            case "users":
                return userHandler.findUserById(id);
            case "posts":
                return postHandler.findPostById(id);
            case "pets":
                return petHandler.findPetById(id);
            case "categories":
                return categoryHandler.findCategoryById(id);
            case "pet_types":
                return petTypeHandler.findPetTypesById(id);

            default:
                return null;
        }
    }


    public Object getByOwner(String collectionName, String id) {
        switch (collectionName) {
            case "posts":
                return postHandler.findPostsByOwner(id);
            case "pets":
                return petHandler.findPetsByOwner(id);
            default:
                return null;
        }
    }

    public String uploadImage(FileItem file) {

        String fileUrl = null;

        fileUrl = "resFolder/media/" + file.getName();

        try (var os = new FileOutputStream(Paths.get("resFolder/media/" + file.getName()).toString())) {
            os.write(file.get());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileUrl;

    }


    public Object getLoginByNameOrEmail(User user) {
        return userHandler.findUserByNameOrEmail(user);
    }


    public PostHandler getPostHandler() {
        return postHandler;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public PetHandler getPetHandler() {
        return petHandler;
    }

    public CategoryHandler getCategoryHandler() {
        return categoryHandler;
    }

    public LikeHandler getLikeHandler() {
        return likeHandler;
    }

    public CommentHandler getCommentHandler() {
        return commentHandler;
    }


    public MongoDatabase getDatabase() {
        return database;
    }
}
