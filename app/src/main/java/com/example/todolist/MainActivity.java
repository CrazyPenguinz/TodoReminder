package com.example.todolist;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.todolist.Adapter.PostItAdapter;
import com.example.todolist.Model.PostIt;
import com.example.todolist.ViewModel.PostItViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NEW_POST = 1;
    public static final int EDIT_POST = 2;

    private PostItViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNote = findViewById(R.id.fab);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditPostItActivity.class);
                startActivityForResult(intent, ADD_NEW_POST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);

        final PostItAdapter adapter = new PostItAdapter();
        recyclerView.setAdapter(adapter);

        adapter.SetOnClickListener(new PostItAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(PostIt postIt) {
                Intent intent = new Intent(MainActivity.this, AddEditPostItActivity.class);
                intent.putExtra(AddEditPostItActivity.EXTRA_ID, postIt.getId());
                intent.putExtra(AddEditPostItActivity.EXTRA_TITLE, postIt.getTitle());
                intent.putExtra(AddEditPostItActivity.EXTRA_DESCRIPTION, postIt.getDescription());
                intent.putExtra(AddEditPostItActivity.EXTRA_PLACE_NAME, postIt.getPlaceName());
                intent.putExtra(AddEditPostItActivity.EXTRA_LATITUDE, postIt.getLatitude());
                intent.putExtra(AddEditPostItActivity.EXTRA_LONGITUDE, postIt.getLongitude());
                intent.putExtra(AddEditPostItActivity.EXTRA_DATE, postIt.getDueDate());
                startActivityForResult(intent, EDIT_POST);
            }
        });

        viewModel = ViewModelProviders.of(this).get(PostItViewModel.class);
        viewModel.getAllPosts().observe(this, new Observer<List<PostIt>>() {
            @Override
            public void onChanged(List<PostIt> postIts) {
                setDailyAlarm(postIts);
                adapter.setPostIts(postIts);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_POST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditPostItActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPostItActivity.EXTRA_DESCRIPTION);
            String placeName = data.getStringExtra(AddEditPostItActivity.EXTRA_PLACE_NAME);
            double latitude = data.getDoubleExtra(AddEditPostItActivity.EXTRA_LATITUDE, 0);
            double longitude = data.getDoubleExtra(AddEditPostItActivity.EXTRA_LONGITUDE, 0);
            String date = data.getStringExtra(AddEditPostItActivity.EXTRA_DATE);

            PostIt postIt = new PostIt(title, description, date, placeName, latitude, longitude);
            viewModel.insert(postIt);
            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_POST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPostItActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditPostItActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPostItActivity.EXTRA_DESCRIPTION);
            String placeName = data.getStringExtra(AddEditPostItActivity.EXTRA_PLACE_NAME);
            double latitude = data.getDoubleExtra(AddEditPostItActivity.EXTRA_LATITUDE, 0);
            double longitude = data.getDoubleExtra(AddEditPostItActivity.EXTRA_LONGITUDE, 0);
            String date = data.getStringExtra(AddEditPostItActivity.EXTRA_DATE);

            PostIt postIt = new PostIt(title, description, date, placeName, latitude, longitude);
            postIt.setId(id);
            viewModel.update(postIt);
            Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Note not Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.delete_all_notes:
               viewModel.deleteAllPosts();
               Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show();
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void setDailyAlarm(List<PostIt> postIts) {
        createNotificationChannel();

        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar tomorrow = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(AddEditPostItActivity.DATE_FORMAT);
        tomorrow.set(Calendar.HOUR, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        Date date = tomorrow.getTime();
        ArrayList<PostIt> tomorrowList = tomorrowPosts(postIts, format.format(date));
        Log.d("ntc", "setDailyAlarm: " + tomorrowList.size() + " items in " + format.format(date));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("POSTS", tomorrowList);
        intent.putExtra("BUNDLE", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, tomorrow.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for remind";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ReminderBroadcast.CHANNEL, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private ArrayList<PostIt> tomorrowPosts(List<PostIt> list, String date) {
        ArrayList<PostIt> tomorrowList = new ArrayList<>();
        for (PostIt postIt : list) {
            if (postIt.getDueDate().equals(date)) {
                tomorrowList.add(postIt);
            }
        }
        return tomorrowList;
    }
}
