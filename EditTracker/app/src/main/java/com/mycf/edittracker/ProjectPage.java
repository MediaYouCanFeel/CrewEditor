package com.mycf.edittracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

        ImageButton editProjectButton = (ImageButton) findViewById(R.id.imageButton_edit_project);
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
        int width = size.x;
        int height = size.y;
        while (res.moveToNext()) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            final String sceneNumber = res.getString(2);
            final String sceneLocation = res.getString(3);
            final String sceneTime = res.getString(4);
            TextView numberTextView = new TextView(this);
            TextView locationTextView = new TextView(this);
            TextView timeTextView = new TextView(this);

            numberTextView.setText(sceneNumber);
            numberTextView.setTextSize(20);
            //numberTextView.setTypeface(null, Typeface.BOLD);
            numberTextView.setTextColor(Color.parseColor("#000000"));

            locationTextView.setText(sceneLocation);
            locationTextView.setTextSize(20);
            locationTextView.setTypeface(null, Typeface.BOLD);
            locationTextView.setTextColor(Color.parseColor("#000000"));

            timeTextView.setText(sceneTime);
            timeTextView.setTextSize(20);
            //timeTextView.setTypeface(null, Typeface.BOLD);
            timeTextView.setTextColor(Color.parseColor("#000000"));

            relativeLayout.setBackground(getResources().getDrawable(R.drawable.border));
            relativeLayout.setPadding(10,10,10,10);
            relativeLayout.setLayoutParams(llp);

            RelativeLayout.LayoutParams numLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            numLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            RelativeLayout.LayoutParams locLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            locLP.addRule(RelativeLayout.CENTER_IN_PARENT);

            RelativeLayout.LayoutParams timeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            numberTextView.setLayoutParams(numLP);
            locationTextView.setLayoutParams(locLP);
            timeTextView.setLayoutParams(timeLP);

            relativeLayout.addView(numberTextView);
            relativeLayout.addView(locationTextView);
            relativeLayout.addView(timeTextView);

//            relativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(ProjectPage.this, ScenePage.class);
//                    Bundle b = new Bundle();
//                    b.putString("projectId", projectId); //Your id
//                    intent.putExtras(b); //Put your id to your next Intent
//                    startActivity(intent);
//                }
//            });
            linearLayout.addView(relativeLayout);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectPage.this, ProjectsPage.class));
    }
}
