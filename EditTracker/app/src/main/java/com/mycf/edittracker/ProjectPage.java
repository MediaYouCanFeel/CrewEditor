package com.mycf.edittracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectPage extends AppCompatActivity {

    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        myDb = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        TextView projectTitleTextView = (TextView) findViewById(R.id.textView_project_title);

        final String projId = b.getString("projectId");
        final String projectTitle = myDb.getProjectTitle(projId);

        projectTitleTextView.setText(projectTitle);

        ImageButton editProjectButton = (ImageButton) findViewById(R.id.imageButton_edit_project);

        editProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectPage.this, EditProjectPage.class);
                Bundle b = new Bundle();
                b.putString("projectId", projId); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectPage.this, ProjectsPage.class));
    }
}
