package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProject extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText projectTitle;
    Button newProjectCreateButton;
    Button newProjectCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        myDb = new DatabaseHelper(this);

        projectTitle = (EditText) findViewById(R.id.editText_new_proj_title);
        newProjectCreateButton = (Button) findViewById(R.id.new_project_create_button);
        newProjectCancelButton = (Button) findViewById(R.id.new_project_cancel_button);

        AddData();

        newProjectCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewProject.this, ProjectsPage.class));
            }
        });

    }

    public void AddData() {
        newProjectCreateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(projectTitle.getText().toString());
                        if(isInserted) {
                            Toast.makeText(NewProject.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NewProject.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(NewProject.this, ProjectsPage.class));
                    }
                }
        );
    }
}
