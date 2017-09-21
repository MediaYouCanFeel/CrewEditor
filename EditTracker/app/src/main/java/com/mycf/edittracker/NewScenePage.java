package com.mycf.edittracker;

import android.graphics.Color;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class NewScenePage extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scene_page);

        myDb = new DatabaseHelper(this);

        TextView projectTitleTextView = (TextView) findViewById(R.id.textView_proj_title);
        Button newSceneCreateButton = (Button) findViewById(R.id.edit_scene_create_button);
        Button newSceneCancelButton = (Button) findViewById(R.id.edit_scene_cancel_button);

        projectTitleTextView.setTypeface(FontManager.getTypeface(this, FontManager.POPPINS_SEMIBOLD));

        final EditText newSceneNumber = (EditText) findViewById(R.id.editText_new_scene_number);
        final EditText newSceneLocation = (EditText) findViewById(R.id.editText_new_scene_location);
        final EditText newSceneTime = (EditText) findViewById(R.id.editText_new_scene_time);

        Bundle b = getIntent().getExtras();
        final String projId = b.getString("projectId");

        String projectTitle = myDb.getProjectTitle(projId);
        projectTitleTextView.setText(projectTitle);

        newSceneCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(NewScenePage.this, ProjectPage.class, bundle);
            }
        });

        newSceneCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sceneNumberExists = myDb.sceneNumberExists(newSceneNumber.getText().toString().trim(), projId);

                if (!sceneNumberExists) { //If scene num does not exist already
                    boolean isInserted = myDb.insertNewScene(projId,
                            newSceneNumber.getText().toString(),
                            newSceneLocation.getText().toString(),
                            newSceneTime.getText().toString());
                    if (isInserted) {
                        Toast.makeText(NewScenePage.this, "New scene added", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NewScenePage.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                    HashMap<String, String> bundle = new HashMap<>();
                    bundle.put("projectId", projId);
                    CrewUtils.sendIntent(NewScenePage.this, ProjectPage.class, bundle);
                } else {
                    Toast.makeText(NewScenePage.this, "Scene number already exists", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
