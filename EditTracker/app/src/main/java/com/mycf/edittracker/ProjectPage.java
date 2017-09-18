package com.mycf.edittracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class ProjectPage extends AppCompatActivity {

    DatabaseHelper myDb;
    String projId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        myDb = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        TextView projectTitleTextView = (TextView) findViewById(R.id.textView_project_title);

        projId = b.getString("projectId");
        final String projectTitle = myDb.getProjectTitle(projId);

        projectTitleTextView.setText(projectTitle);

        Button editProjectButton = (Button) findViewById(R.id.button_edit_project);
        editProjectButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        editProjectButton.setText(getResources().getString(R.string.fa_icon_pencil));
        editProjectButton.setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        Button addSceneButton = (Button) findViewById(R.id.button_add_scene);

        displayAllScenes();

        addSceneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(ProjectPage.this, NewScenePage.class, bundle);
            }
        });;

        editProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(ProjectPage.this, EditProjectPage.class, bundle);
            }
        });
    }

    protected void displayAllScenes() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.all_scenes_layout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0,15,0,15);



        Cursor res = myDb.getScenesForProject(projId);
        if (res.getCount() == 0) {
            // show message for no results
            //showMessage("Error", "No data found.");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        while (res.moveToNext()) { //FOR EACH SCENE
            LinearLayout fullSceneLinLay = new LinearLayout(this);
            fullSceneLinLay.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout sceneInfoRelLay = new RelativeLayout(this);

            LinearLayout workflowStepsLinLay = new LinearLayout(this);
            workflowStepsLinLay.setOrientation(LinearLayout.HORIZONTAL);

            final String sceneId = res.getString(0);
            final String sceneNumber = res.getString(2);
            final String sceneLocation = res.getString(3);
            final String sceneTime = res.getString(4);
            TextView numberTextView = new TextView(this);
            TextView locationTextView = new TextView(this);
            TextView timeTextView = new TextView(this);

            numberTextView.setText(sceneNumber);
            numberTextView.setTextSize(20);
            numberTextView.setTextColor(Color.parseColor("#000000"));

            locationTextView.setText(sceneLocation);
            locationTextView.setTextSize(20);
            locationTextView.setTypeface(null, Typeface.BOLD);
            locationTextView.setTextColor(Color.parseColor("#000000"));

            timeTextView.setText(sceneTime);
            timeTextView.setTextSize(20);
            timeTextView.setTextColor(Color.parseColor("#000000"));

            sceneInfoRelLay.setBackground(getResources().getDrawable(R.drawable.border));
            sceneInfoRelLay.setPadding(10,10,10,10);
            sceneInfoRelLay.setLayoutParams(llp);

            RelativeLayout.LayoutParams numLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            numLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            RelativeLayout.LayoutParams locLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            locLP.addRule(RelativeLayout.CENTER_IN_PARENT);

            RelativeLayout.LayoutParams timeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            numberTextView.setLayoutParams(numLP);
            locationTextView.setLayoutParams(locLP);
            timeTextView.setLayoutParams(timeLP);

            sceneInfoRelLay.addView(numberTextView);
            sceneInfoRelLay.addView(locationTextView);
            sceneInfoRelLay.addView(timeTextView);

            fullSceneLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> bundle = new HashMap<>();
                    bundle.put("projectId", projId);
                    bundle.put("sceneId", sceneId);
                    CrewUtils.sendIntent(ProjectPage.this, ScenePage.class, bundle);
                }
            });

            String workflowId = myDb.getWorkflowId(sceneId);
            String[] workflowStepIds = myDb.getWorkflowSteps(workflowId);
            String[] status = myDb.getStatus(sceneId);

            for (int i = 0; i < workflowStepIds.length; i++) {
                TextView workflowStepTextView = new TextView(this);
                workflowStepTextView.setText(myDb.getWorkflowStepAbbr(workflowStepIds[i]));
                workflowStepTextView.setPadding(10, 0, 10, 0);
                workflowStepTextView.setBackground(getResources().getDrawable(R.drawable.border));
                if (status[i].equals("TRUE")) {
                    workflowStepTextView.setBackgroundColor(Color.BLUE);
                }
                workflowStepsLinLay.addView(workflowStepTextView);
            }

            workflowStepsLinLay.setPadding(0,0,0,0);
            workflowStepsLinLay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            workflowStepsLinLay.setBackground(getResources().getDrawable(R.drawable.border));

            TextView percentageComplete = new TextView(this);
            percentageComplete.setText(Integer.toString(myDb.getPercentageCompleteScene(sceneId)) + "% Complete");

            workflowStepsLinLay.addView(percentageComplete);

            fullSceneLinLay.addView(sceneInfoRelLay);
            fullSceneLinLay.addView(workflowStepsLinLay);

            linearLayout.addView(fullSceneLinLay);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectPage.this, ProjectsPage.class));
    }
}
