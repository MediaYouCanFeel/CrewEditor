package com.mycf.edittracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditProjectPage extends AppCompatActivity {

    DatabaseHelper myDb;

    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_page);

        myDb = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        final EditText projectTitleEditView = (EditText) findViewById(R.id.editText_edit_proj_title);
        final String projId = b.getString("projectId");

        String projectTitle = myDb.getProjectTitle(projId);
        projectTitleEditView.setText(projectTitle);

        final Button saveEditButton = (Button) findViewById(R.id.edit_project_save_button);
        final ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButton_delete_proj);

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateTitle(projectTitleEditView.getText().toString(), projId);
                Intent intent = new Intent(EditProjectPage.this, ProjectPage.class);
                Bundle b = new Bundle();
                b.putString("projectId", projId); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteProject(projId);
                Intent intent = new Intent(EditProjectPage.this, ProjectsPage.class);
                startActivity(intent);
            }
        });

    }

}
