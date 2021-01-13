package com.trixibackend.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.trixibackend.entity.Like;
import com.trixibackend.entity.Notification;
import com.trixibackend.entity.Post;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class NotificationHandler {
    private MongoCollection<Notification> notColl;

    public NotificationHandler(MongoDatabase database) {

        notColl = database.getCollection("notifications",Notification.class);
    }

    public MongoCollection<Notification> getNotColl() {
        return notColl;
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = null;
        try {
            FindIterable<Notification> notIter = notColl.find();
            notifications = new ArrayList<>();
            notIter.forEach(notifications::add);
            notifications.forEach(not -> not.setUid(not.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }


    public List<Notification> getNotificationByPostOwner(String postOwnerId){
        List<Notification> notifications = null;
        try{
            FindIterable<Notification> notIter = notColl.find(eq("postOwnerId",postOwnerId));
            notifications = new ArrayList<>();
            notIter.forEach(notifications::add);
            notifications.forEach(notification -> notification.setUid(notification.getId().toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        assert notifications != null;
        Collections.reverse(notifications);
        return notifications;
    }

    public DeleteResult deleteNotification(String id) {
        Bson not = eq("_id",new ObjectId(id));
        DeleteResult result = notColl.deleteOne(not);
        System.out.println(result);
        return result;
    }

    public void deleteNotificationByLike(Like like){
        var allNotifications = getAllNotifications();
        Notification notification = new Notification();
        allNotifications.forEach(not ->{
            if(not.getLike() != null){
                if(not.getLike().getPostId().equals(like.getPostId()) && not.getLike().getUserId().equals(like.getUserId())){
                    var result = deleteNotification(not.getUid());
                    System.out.println("notification deleted: " + result );
                }
            }
        });


    }


}
