package com.mycf.edittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScenePage extends AppCompatActivity {

    DatabaseHelper myDb;

    String projId;
    String sceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_page);

        Bundle b = getIntent().getExtras();

        projId = b.getString("projectId");
        sceneId = b.getString("sceneId");

        myDb = new DatabaseHelper(this);

        String projectTitle = myDb.getProjectTitle(projId);
        TextView projectTitleTextView = (TextView) findViewById(R.id.textView_scene_project_title);
        projectTitleTextView.setText(projectTitle);

        String sceneNumber = myDb.getSceneNumber(sceneId);
        TextView numberTextView = (TextView) findViewById(R.id.textView_scene_number);
        numberTextView.setText(sceneNumber);

        String sceneLocation = myDb.getSceneLocation(sceneId);
        String sceneTime = myDb.getSceneTime(sceneId);
        TextView locationTextView = (TextView) findViewById(R.id.textView_scene_location);
        locationTextView.setText(sceneLocation + " - " + sceneTime);
    }
}
