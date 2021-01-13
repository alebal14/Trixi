package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.User;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

public class PetHandler {

    private MongoCollection<Pet> petColl;
    private PostHandler postHandler;

    public PetHandler(MongoDatabase database) {
        petColl = database.getCollection("pets", Pet.class);
        postHandler = new PostHandler(database);
    }

    public MongoCollection<Pet> getPetColl() {
        return petColl;
    }

    public List<Pet> getAllPets() {
        List<Pet> pets = null;
        try {
            FindIterable<Pet> petIter = petColl.find();
            pets = new ArrayList<>();
            petIter.forEach(pets::add);
            pets.forEach(pet -> {
                pet.setUid(pet.getId().toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    public Pet findPetById(String id) {
        try {
            var petIter = petColl.find(eq("_id", new ObjectId(id)));
            var pet = petIter.first();
            if (pet == null) return null;
            pet.setUid(pet.getId().toString());
            pet.setPosts(postHandler.findPostsByOwner(pet.getUid()));

            return pet;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Pet> findPetsByOwner(String id) {
        List<Pet> pets = null;
        try {
            FindIterable<Pet> petsIter = petColl.find(eq("ownerId", id));
            pets = new ArrayList<>();
            petsIter.forEach(pets::add);
            pets.forEach(pet -> {
                pet.setUid(pet.getId().toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    public List<Pet> findPetsByPetType(String id){
        List<Pet> pets = null;
        try {
            FindIterable<Pet> petsIter = petColl.find(eq("petTypeId", id));
            pets = new ArrayList<>();
            petsIter.forEach(pets::add);
            pets.forEach(pet -> pet.setUid(pet.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    public List<Pet> findPetsByPetTypeName(String id){
        List<Pet> pets = null;
        try {
            FindIterable<Pet> petsIter = petColl.find(eq("petType", id));
            pets = new ArrayList<>();
            petsIter.forEach(pets::add);
            pets.forEach(pet -> pet.setUid(pet.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    public DeleteResult deletePet(String id, MongoCollection<User> userColl) {
        var pet = findPetById(id);
        var petFollowers = pet.getFollowers();
        for(User u:petFollowers){
            userColl.updateOne(eq("_id",u.getId()),Updates.pull("followingsPet",new BasicDBObject("_id", pet.getId())));
        }
        Bson petTobeDeleted = eq("_id",new ObjectId(id));
        DeleteResult deletedPet = petColl.deleteOne(petTobeDeleted);

        Bson posts = eq("ownerId",id);
        DeleteResult deletedPost = postHandler.getPostColl().deleteMany(posts);
        return deletedPet;
    }
}
