package com.trixibackend;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Database {

    public Database(){
        connectToDb();
    }

    private void connectToDb() {
        ConnectionString connString = new ConnectionString(
                "mongodb+srv://Snehal:1234@cluster0.cemx5.mongodb.net/trixi?retryWrites=true&w=majority"
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("test");


        // System.setProperty("jdk.tls.trustNameService", "true");
        //test get
        MongoCollection<Document> collection = database.getCollection("test");
        Document query = new Document("_id", new ObjectId("5fc8d014f6758a30a800f48e"));
        Document result = collection.find(query).iterator().next();

        System.out.println("test1: " + result.getString("test1"));
    }



}
