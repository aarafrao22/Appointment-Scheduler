package com.aarafrao.busterlord_hiringscheduler.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDAO {
    @Query("select * FROM notifications")
    List<Notification> getAllNotifications();

    @Insert
    void addNotification(Notification notification);

    @Delete
    void deleteNotification(Notification notification);

}
