package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Category;
import com.trixibackend.entity.PetType;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PetTypeHandler {
    private MongoCollection<PetType> petTypeColl;

    public PetTypeHandler(MongoDatabase database){
        petTypeColl = database.getCollection("pet_types", PetType.class);
    }

    public MongoCollection<PetType> getPetTypeColl() {
        return petTypeColl;
    }

    public List<PetType> getAllPetTypes() {
        List<PetType> petTypes = new ArrayList<>();

        try {
            FindIterable<PetType> petTypeIter = petTypeColl.find();
            petTypeIter.forEach(petTypes::add);
            petTypes.forEach(petType -> petType.setUid(petType.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return petTypes;
    }

    public PetType findPetTypesById(String id) {

        try {
            var petTypesIter = petTypeColl.find(eq("_id",  new ObjectId(id)));
            var petType = petTypesIter.first();
            if (petType == null) return null;
            petType.setUid(petType.getId().toString());
            return petType;
        } catch (Exception e) {
            return null;
        }
    }

}
