package com.mycf.edittracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        TextView releaseDateTextView = (TextView) findViewById(R.id.textView_release_date);
        String releaseDate = myDb.getProjectReleaseDate(projId);
        String releaseDateText;
        if (releaseDate.equals("No release date.")) {
            releaseDateText = "No release date";
        } else {
            releaseDateText = "Release Date: " + releaseDate;
        }

        releaseDateTextView.setText(releaseDateText);

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

            //Scene Info
            final String sceneId = res.getString(0);
            final String sceneNumber = res.getString(2);
            final String sceneLocation = res.getString(3);
            final String sceneTime = res.getString(4);

            //Params
            LinearLayout.LayoutParams fill_parent_width_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            fill_parent_width_params.weight = 1.0f;
            fill_parent_width_params.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams fill_parent_width_padded_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            fill_parent_width_padded_params.weight = 1.0f;
            fill_parent_width_padded_params.gravity = Gravity.CENTER;
            fill_parent_width_padded_params.setMargins(0,15,0,15);

            LinearLayout.LayoutParams fill_parent_height_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            fill_parent_height_params.weight = 1.0f;
            fill_parent_height_params.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams fill_parent_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            fill_parent_params.weight = 1.0f;
            fill_parent_params.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams heavier_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            heavier_params.weight = 2.0f;
            heavier_params.gravity = Gravity.CENTER;

            //full layout
            LinearLayout fullSceneLinLay = new LinearLayout(this);
            fullSceneLinLay.setOrientation(LinearLayout.HORIZONTAL);
            fullSceneLinLay.setLayoutParams(fill_parent_width_padded_params);

            //Scene Number
            TextView sceneNumberTextView = new TextView(this);
            sceneNumberTextView.setText(sceneNumber);
            sceneNumberTextView.setGravity(Gravity.CENTER_VERTICAL);
            sceneNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            sceneNumberTextView.setTextColor(Color.BLACK);
            sceneNumberTextView.setPadding(30,0,30,0);

            //Non-number info
            LinearLayout nonNumberLinearLayout = new LinearLayout(this);
            nonNumberLinearLayout.setOrientation(LinearLayout.VERTICAL);
            nonNumberLinearLayout.setLayoutParams(fill_parent_width_params);

            //Scene Location and Time
            TextView sceneLocationTimeTextView = new TextView(this);
            sceneLocationTimeTextView.setText(sceneLocation + " - " + sceneTime);
            sceneLocationTimeTextView.setBackground(getResources().getDrawable(R.drawable.border));
            sceneLocationTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            sceneLocationTimeTextView.setTextColor(Color.BLACK);
            sceneLocationTimeTextView.setGravity(Gravity.CENTER);
            sceneLocationTimeTextView.setPadding(0,10,0,10);

            //Workflow Progress
            LinearLayout workflowLinLay = new LinearLayout(this);
            workflowLinLay.setOrientation(LinearLayout.HORIZONTAL);
            workflowLinLay.setLayoutParams(fill_parent_params);

            //Workflow Steps
            LinearLayout workflowStepsLinLay = new LinearLayout(this);
            workflowStepsLinLay.setOrientation(LinearLayout.HORIZONTAL);

            String workflowId = myDb.getWorkflowId(sceneId);
            String[] workflowStepIds = myDb.getWorkflowSteps(workflowId);
            String[] status = myDb.getStatus(sceneId);

            for (int i = 0; i < workflowStepIds.length; i++) {
                TextView workflowStepTextView = new TextView(this);
                workflowStepTextView.setText(myDb.getWorkflowStepAbbr(workflowStepIds[i]));
                workflowStepTextView.setPadding(10, 0, 10, 0);
                workflowStepTextView.setBackground(getResources().getDrawable(R.drawable.border));
                workflowStepTextView.setLayoutParams(fill_parent_height_params);
                workflowStepTextView.setGravity(Gravity.CENTER);
                if (status[i].equals("TRUE")) {
                    workflowStepTextView.setBackgroundColor(Color.DKGRAY);
                    workflowStepTextView.setTextColor(Color.WHITE);
                }
                workflowStepsLinLay.addView(workflowStepTextView);
            }
            workflowStepsLinLay.setLayoutParams(fill_parent_params);

            //Workflow Percentage Complete
            LinearLayout percentageCompleteLinLay = new LinearLayout(this);
            percentageCompleteLinLay.setOrientation(LinearLayout.VERTICAL);

            TextView percentageCompletePercentage = new TextView(this);
            percentageCompletePercentage.setText(Integer.toString(myDb.getPercentageCompleteScene(sceneId) )+ "%");
            percentageCompletePercentage.setGravity(Gravity.CENTER_HORIZONTAL);
            percentageCompletePercentage.setTextColor(Color.BLACK);
            percentageCompletePercentage.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

            TextView percentageCompleteText = new TextView(this);
            percentageCompleteText.setText("complete");
            percentageCompleteText.setGravity(Gravity.CENTER_HORIZONTAL);
            percentageCompleteText.setTextColor(Color.BLACK);

            percentageCompleteLinLay.addView(percentageCompletePercentage);
            percentageCompleteLinLay.addView(percentageCompleteText);
            percentageCompleteLinLay.setPadding(10,10,10,10);
            percentageCompleteLinLay.setLayoutParams(heavier_params);

            workflowLinLay.addView(workflowStepsLinLay);
            workflowLinLay.addView(percentageCompleteLinLay);
            workflowLinLay.setBackground(getResources().getDrawable(R.drawable.border));

            nonNumberLinearLayout.addView(sceneLocationTimeTextView);
            nonNumberLinearLayout.addView(workflowLinLay);

            fullSceneLinLay.addView(sceneNumberTextView);
            fullSceneLinLay.addView(nonNumberLinearLayout);
            fullSceneLinLay.setGravity(Gravity.CENTER_VERTICAL);

            fullSceneLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> bundle = new HashMap<>();
                    bundle.put("projectId", projId);
                    bundle.put("sceneId", sceneId);
                    CrewUtils.sendIntent(ProjectPage.this, ScenePage.class, bundle);
                }
            });

            fullSceneLinLay.setBackground(getResources().getDrawable(R.drawable.border));

            linearLayout.addView(fullSceneLinLay);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectPage.this, ProjectsPage.class));
    }
}
