package com.aarafrao.busterlord_hiringscheduler.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ModelDAO {
    @Query("select * FROM notifications")
    List<Model> getAllNotifications();

    @Insert
    void addNotification(Model model);

    @Delete
    void deleteNotification(Model model);

}
