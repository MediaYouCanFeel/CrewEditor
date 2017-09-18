package com.mycf.edittracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.app.DatePickerDialog.*;

public class NewProject extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    DatabaseHelper myDb;
    EditText projectTitle;
    EditText projectReleaseDate;
    Button newProjectCreateButton;
    Button newProjectCancelButton;

    private DatePickerDialog releaseDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        myDb = new DatabaseHelper(this);

        projectTitle = (EditText) findViewById(R.id.editText_new_proj_title);
        projectReleaseDate = (EditText) findViewById(R.id.editText_release_date);
        newProjectCreateButton = (Button) findViewById(R.id.new_project_create_button);
        newProjectCancelButton = (Button) findViewById(R.id.new_project_cancel_button);

        projectReleaseDate.setInputType(InputType.TYPE_NULL);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        AddData();

        setDateTimeField();

        newProjectCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewProject.this, ProjectsPage.class));
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean shown) {
        if(view == projectReleaseDate && shown) {
            releaseDatePickerDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == projectReleaseDate) {
            releaseDatePickerDialog.show();
        }
    }

    private void setDateTimeField() {
        projectReleaseDate.setOnClickListener(this);
        projectReleaseDate.setOnFocusChangeListener(this);

        Calendar newCalendar = Calendar.getInstance();
        releaseDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                projectReleaseDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }



    public void AddData() {
        newProjectCreateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted;
                        String title = projectTitle.getText().toString();
                        String releaseDate = projectReleaseDate.getText().toString();

                        if (releaseDate.isEmpty()) {
                            isInserted = myDb.insertNewProject(title);
                        } else {
                            isInserted = myDb.insertNewProject(title, releaseDate);
                        }

                        if(isInserted) {
                            Toast.makeText(NewProject.this, "New project added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NewProject.this, "ERROR", Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(NewProject.this, ProjectsPage.class));
                    }
                }
        );
    }
}
