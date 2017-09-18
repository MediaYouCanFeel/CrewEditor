package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class EditScenePage extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scene_page);

        myDb = new DatabaseHelper(this);

        TextView projectTitleTextView = (TextView) findViewById(R.id.textView_proj_title);
        Button editSceneSaveButton = (Button) findViewById(R.id.edit_scene_create_button);
        Button editSceneCancelButton = (Button) findViewById(R.id.edit_scene_cancel_button);

        final EditText newSceneNumber = (EditText) findViewById(R.id.editText_new_scene_number);
        final EditText newSceneLocation = (EditText) findViewById(R.id.editText_new_scene_location);
        final EditText newSceneTime = (EditText) findViewById(R.id.editText_new_scene_time);
        final ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButton_delete_scene);

        Bundle b = getIntent().getExtras();
        final String projId = b.getString("projectId");
        final String sceneId = b.getString("sceneId");

        String projectTitle = myDb.getProjectTitle(projId);
        projectTitleTextView.setText(projectTitle);

        newSceneNumber.setText(myDb.getSceneNumber(sceneId));
        newSceneLocation.setText(myDb.getSceneLocation(sceneId));
        newSceneTime.setText(myDb.getSceneTime(sceneId));

        editSceneCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                bundle.put("sceneId", sceneId);
                CrewUtils.sendIntent(EditScenePage.this, ScenePage.class, bundle);
            }
        });

        editSceneSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.updateScene(sceneId,
                        newSceneNumber.getText().toString(),
                        newSceneLocation.getText().toString(),
                        newSceneTime.getText().toString());
                if(isInserted) {
                    Toast.makeText(EditScenePage.this, "Scene Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditScenePage.this, "ERROR", Toast.LENGTH_LONG).show();
                }
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                bundle.put("sceneId", sceneId);
                CrewUtils.sendIntent(EditScenePage.this, ScenePage.class, bundle);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteScene(sceneId);
                Intent intent = new Intent(EditScenePage.this, ProjectPage.class);
                Bundle b = new Bundle();
                b.putString("projectId", projId); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

    }
}
