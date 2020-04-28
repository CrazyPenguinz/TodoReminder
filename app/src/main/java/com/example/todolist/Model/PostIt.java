package com.example.todolist.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostIt")
public class PostIt implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String placeName;
    private double latitude;
    private double longitude;

    public PostIt(String title, String description, String dueDate, String placeName, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected PostIt(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        dueDate = in.readString();
        placeName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PostIt> CREATOR = new Creator<PostIt>() {
        @Override
        public PostIt createFromParcel(Parcel in) {
            return new PostIt(in);
        }

        @Override
        public PostIt[] newArray(int size) {
            return new PostIt[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(dueDate);
        dest.writeString(placeName);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
