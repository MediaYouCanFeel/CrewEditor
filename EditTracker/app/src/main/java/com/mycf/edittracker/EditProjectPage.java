package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;

public class EditProjectPage extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_page);

        myDb = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        final String projId = b.getString("projectId");

        final EditText projectTitleEditView = (EditText) findViewById(R.id.editText_edit_proj_title);

        String projectTitle = myDb.getProjectTitle(projId);
        projectTitleEditView.setText(projectTitle);

        final Button saveEditButton = (Button) findViewById(R.id.edit_project_save_button);
        final ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButton_delete_proj);
        final Button cancelEditButton = (Button) findViewById(R.id.edit_project_cancel_button);

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateProjectTitle(projectTitleEditView.getText().toString(), projId);
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(EditProjectPage.this, ProjectPage.class, bundle);
            }
        });

        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(EditProjectPage.this, ProjectPage.class, bundle);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteProject(projId);
                startActivity(new Intent(EditProjectPage.this, ProjectsPage.class));
            }
        });

    }

}
