package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class AddEditPostItActivity extends AppCompatActivity {

    private static final int PICK_PLACE = 3;

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_DESCRIPTION = "DESCRIPTION";
    public static final String EXTRA_DATE = "DATE";
    public static final String EXTRA_PLACE_NAME = "PLACE";
    public static final String EXTRA_LATITUDE = "LATITUDE";
    public static final String EXTRA_LONGITUDE = "LONGITUDE";

    public static final String DATE_FORMAT = "dd/MM/YYYY";

    DatePickerDialog picker;
    TextView tv_location;
    EditText et_title;
    EditText et_description;
    EditText et_placeName;
    EditText et_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_postit);

        tv_location = findViewById(R.id.tv_location);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        et_placeName = findViewById(R.id.et_place_name);
        et_date = findViewById(R.id.et_date);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            et_title.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            et_placeName.setText(intent.getStringExtra(EXTRA_PLACE_NAME));
            String latlag = String.format("%.6f:%.2f",
                    intent.getDoubleExtra(EXTRA_LATITUDE, 0.0), intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0));
            tv_location.setText(latlag);
            et_date.setText(intent.getStringExtra(EXTRA_DATE));
        }
        else {
            setTitle("Add Note");
        }

        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddEditPostItActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    private void save() {
        String title = et_title.getText().toString();
        String description = et_description.getText().toString();
        String placeName = et_placeName.getText().toString();
        String dueDate = et_date.getText().toString();

        String latlag = tv_location.getText().toString();
        if (latlag.trim().isEmpty() || title.trim().isEmpty() || placeName.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please insert the title, place and due date.", Toast.LENGTH_LONG).show();
            return;
        }
        String[] tmp = latlag.split(":");
        double latitude = Double.valueOf(tmp[0]);
        double longitude = Double.valueOf(tmp[1]);

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PLACE_NAME, placeName);
        data.putExtra(EXTRA_LATITUDE, latitude);
        data.putExtra(EXTRA_LONGITUDE, longitude);
        data.putExtra(EXTRA_DATE, dueDate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PLACE && resultCode == RESULT_OK) {
            double latitude =  data.getDoubleExtra(EXTRA_LATITUDE, 0);
            double longitude = data.getDoubleExtra(EXTRA_LONGITUDE, 0);
            String name = data.getStringExtra(EXTRA_PLACE_NAME);

            tv_location.setText(latitude + ":" + longitude);
            et_placeName.setText(name);
        }
    }

    public void findLocation(View view) {
        Intent intent = new Intent(AddEditPostItActivity.this, GoogleMapActivity.class);
        startActivityForResult(intent, PICK_PLACE);
    }
}
