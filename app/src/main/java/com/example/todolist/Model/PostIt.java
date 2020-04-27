package com.example.todolist.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostIt")
public class PostIt {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String dueDate;
    @Embedded
    private Location place;

    public PostIt(String title, String description, String dueDate, Location place) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.place = place;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Location getPlace() {
        return place;
    }
}
