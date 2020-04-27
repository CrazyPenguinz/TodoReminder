package com.example.todolist.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.todolist.Model.PostIt;
import com.example.todolist.Model.PostItDao;
import com.example.todolist.Model.PostItDatabase;

import java.util.List;

public class PostItRepository {
    private PostItDao postItDao;
    private LiveData<List<PostIt>> allPosts;

    public PostItRepository(Application application) {
        PostItDatabase database = PostItDatabase.getInstance(application);
        postItDao = database.postItDao();
        allPosts = postItDao.selectAllPosts();
    }

    public void insert(PostIt note){
        new InsertNoteAsyncTask(postItDao).execute(note);
    }

    public void update(PostIt note){
        new UpdateNoteAsyncTask(postItDao).execute(note);
    }

    public void delete(PostIt note){
        new DeleteNoteAsyncTask(postItDao).execute(note);
    }

    public void deleteAllPosts(){
        new DeleteAllNotesAsyncTask(postItDao).execute( );
    }

    public LiveData<List<PostIt>> getAllPosts() { return allPosts; }

    public LiveData<List<PostIt>> getTomorrowPosts(String today) { return postItDao.selectTomorrowPosts(today); }

    private class InsertNoteAsyncTask extends AsyncTask<PostIt, Void, Void> {
        private PostItDao postItDao;
        public InsertNoteAsyncTask(PostItDao postItDao) {
            this.postItDao = postItDao;
        }

        @Override
        protected Void doInBackground(PostIt... postIts) {
            postItDao.insert(postIts[0]);
            return null;
        }
    }

    private class UpdateNoteAsyncTask extends AsyncTask<PostIt, Void, Void> {
        private PostItDao postItDao;
        public UpdateNoteAsyncTask(PostItDao postItDao) {
            this.postItDao = postItDao;
        }

        @Override
        protected Void doInBackground(PostIt... postIts) {
            postItDao.update(postIts[0]);
            return null;
        }
    }

    private class DeleteNoteAsyncTask extends AsyncTask<PostIt, Void, Void> {
        private PostItDao postItDao;
        public DeleteNoteAsyncTask(PostItDao postItDao) {
            this.postItDao = postItDao;
        }

        @Override
        protected Void doInBackground(PostIt... postIts) {
            postItDao.delete(postIts[0]);
            return null;
        }
    }

    private class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private PostItDao postItDao;
        public DeleteAllNotesAsyncTask(PostItDao postItDao) {
            this.postItDao = postItDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            postItDao.deleteAllNotes();
            return null;
        }
    }
}
