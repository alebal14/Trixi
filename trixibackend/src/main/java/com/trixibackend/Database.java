package com.trixibackend;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.trixibackend.collections.PostHandler;
import com.trixibackend.collections.UserHandler;
import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    public UserHandler getUserHandler() {
        return userHandler;
    }

    private UserHandler userHandler = null;
    private PostHandler postHandler = null;

    MongoCollection<User> userColl = null;
    MongoCollection<Post> postColl = null;


    Map<Type, MongoCollection> collections = new HashMap<>();

    private final String atlasUrl = "mongodb+srv://Snehal:1234@cluster0.cemx5.mongodb.net/trixi?retryWrites=true&w=majority";
    private final String dbname= "trixi";

    public Database() {
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
        MongoDatabase database = mongoClient.getDatabase(dbname);

        // Register codec to support POJO operations (Plain Old Java Object)
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        database = database.withCodecRegistry(pojoCodecRegistry);

        // get users collection linked with POJO

        userHandler = new UserHandler(database);
        postHandler = new PostHandler(database);

        userColl = userHandler.getUserColl();
        postColl = postHandler.getPostColl();


        // generic collections
        collections.putIfAbsent(User.class, userColl);
        collections.putIfAbsent(Post.class,postColl);

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
        if(updated == null) {
            var res = coll.insertOne(object);
//            user.setId(res.getInsertedId().asObjectId().getValue());
//            user.setUid(user.getId().toString());
//            updated = user;
        }

        return updated;
    }



}
