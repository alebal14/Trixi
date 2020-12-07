package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.trixibackend.entity.Category;
import com.trixibackend.entity.Comment;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CategoryHandler {

    private MongoCollection<Category> categoryColl;

    public CategoryHandler(MongoDatabase database) {
        categoryColl = database.getCollection("categories", Category.class);
    }

    public MongoCollection<Category> getCategoryColl() {
        return categoryColl;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        try {
            FindIterable<Category> categoryIter = categoryColl.find();
            categoryIter.forEach(categories::add);
            categories.forEach(category -> category.setuId(category.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

//    public Category findCategoryByName(String categoryName) {
//
//        try {
//            var categoryIter = categoryColl.find({ "name": { categoryName }});
//
//            var category = categoryIter.first();
//            if (category == null) return null;
//            return category;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public Category findCategoryById(String id) {

        try {
            var categoryIter = categoryColl.find(eq("_id",  new ObjectId(id)));
            var category = categoryIter.first();
            if (category == null) return null;
            category.setuId(category.getId().toString());
            return category;
        } catch (Exception e) {
            return null;
        }
    }
}
