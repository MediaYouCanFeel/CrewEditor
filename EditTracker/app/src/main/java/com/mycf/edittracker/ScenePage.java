package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        ImageButton editSceneButton = (ImageButton) findViewById(R.id.imageButton_edit_scene);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_workflow);

        final HashMap<String, String> workflowStatus = myDb.getWorkflow(sceneId);

        Iterator it = workflowStatus.entrySet().iterator();
        while (it.hasNext()) {
            LinearLayout thisStep = new LinearLayout(this);
            thisStep.setOrientation(LinearLayout.HORIZONTAL);
            final Map.Entry pair = (Map.Entry) it.next();
            TextView thisStepText = new TextView(this);
            CheckBox statusCheck = new CheckBox(this);

            statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (isChecked) {
                        workflowStatus.put(pair.getKey().toString(), "TRUE");
                    } else {
                        workflowStatus.put(pair.getKey().toString(), "FALSE");
                    }
                    myDb.updateSceneStatus(sceneId, workflowStatus);
                }
            });

            if (pair.getValue().equals("TRUE")) {
                statusCheck.setChecked(true);
            } else {
                statusCheck.setChecked(false);
            }
            thisStepText.setText(pair.getKey().toString());

            thisStep.addView(statusCheck);
            thisStep.addView(thisStepText);
            linearLayout.addView(thisStep);
        }

        editSceneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                bundle.put("sceneId", sceneId);
                CrewUtils.sendIntent(ScenePage.this, EditScenePage.class, bundle);
            }
        });
    }

    @Override
    public void onBackPressed() {
        HashMap<String, String> bundle = new HashMap<>();
        bundle.put("projectId", projId);
        bundle.put("sceneId", sceneId);
        CrewUtils.sendIntent(ScenePage.this, ProjectPage.class, bundle);
    }
}
