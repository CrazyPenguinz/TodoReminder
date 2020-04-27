package com.example.todolist.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostItDao {
    @Insert
    void insert(PostIt postIt);

    @Update
    void update(PostIt postIt);

    @Delete
    void delete(PostIt postIt);

    @Query("DELETE FROM PostIt")
    void deleteAllNotes();

    @Query("SELECT * FROM PostIt ORDER BY dueDate")
    LiveData<List<PostIt>> selectAllPosts();

    @Query("SELECT * FROM PostIt WHERE dueDate = :today")
    LiveData<List<PostIt>> selectTomorrowPosts(String today);
}
