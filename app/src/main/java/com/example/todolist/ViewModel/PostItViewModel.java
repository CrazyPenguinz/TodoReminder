package com.example.todolist.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.Model.PostIt;
import com.example.todolist.Repository.PostItRepository;

import java.util.List;

public class PostItViewModel extends AndroidViewModel {
    private PostItRepository repository;
    private LiveData<List<PostIt>> allPosts;

    public PostItViewModel(Application application) {
        super(application);
        repository = new PostItRepository(application);
        allPosts = repository.getAllPosts();
    }

    public  void insert(PostIt postIt){
        repository.insert(postIt);
    }
    public  void update(PostIt postIt){
        repository.update(postIt);
    }

    public  void delete(PostIt postIt){
        repository.delete(postIt);
    }

    public  void deleteAllPosts(){
        repository.deleteAllPosts();
    }

    public LiveData<List<PostIt>> getAllPosts(){
        return allPosts;
    }

    public LiveData<List<PostIt>> getTomorrowPosts(String today) { return repository.getTomorrowPosts(today); }
}
