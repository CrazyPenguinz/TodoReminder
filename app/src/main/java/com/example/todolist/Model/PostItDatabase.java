package com.example.todolist.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {PostIt.class}, version = 1)
public abstract class PostItDatabase extends RoomDatabase {
    private static PostItDatabase instance;

    public abstract PostItDao postItDao();

    public static synchronized PostItDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PostItDatabase.class, "postit_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PostItDao postItDao;
        public PopulateDbAsyncTask(PostItDatabase db) {
            postItDao = db.postItDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
