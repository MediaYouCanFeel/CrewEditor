package com.mycf.edittracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProjectsPage extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_page);

        myDb = new DatabaseHelper(this);

        ScrollView scrollView = (ScrollView) findViewById(R.id.all_projects_layout_scroll);
        TextView titleView = (TextView) findViewById(R.id.textView_my_projects);
        Button newProjectButton = (Button) findViewById(R.id.new_project_button);

        displayAllProjects();

        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProjectsPage.this, NewProject.class));
            }
        });
    }

    protected void displayAllProjects() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.all_projects_layout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView.LayoutParams llp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0,15,0,15);

        linearLayout.setLayoutParams(llp);

        Cursor res = myDb.getAllProjects();
        if (res.getCount() == 0) {
            // show message for no results
            //showMessage("Error", "No data found.");
            return;
        }

        StringBuffer buffer = new StringBuffer();

        while (res.moveToNext()) {
            LinearLayout individualProjectLinearLayout = new LinearLayout(this);
            individualProjectLinearLayout.setOrientation(LinearLayout.VERTICAL);

            final String projectTitle = res.getString(1);
            final String projectId = res.getString(0);

            individualProjectLinearLayout.addView(createProjectTitleTextView(projectTitle, projectId));
            individualProjectLinearLayout.addView(createProjectLowerInfoLinearLayout(projectId));

            LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp2.setMargins(0,15,0,15);
            individualProjectLinearLayout.setLayoutParams(llp2);

            individualProjectLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectsPage.this, ProjectPage.class);
                    Bundle b = new Bundle();
                    b.putString("projectId", projectId); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });

            linearLayout.addView(individualProjectLinearLayout);
        }
    }

    private LinearLayout createProjectLowerInfoLinearLayout(final String id) {
        LinearLayout lowerInfoLinearLayout = new LinearLayout(this);
        lowerInfoLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Number of Scenes
        LinearLayout scenesLinearLayout = new LinearLayout(this);
        scenesLinearLayout.setOrientation(LinearLayout.VERTICAL);

        int numOfScenes = myDb.getNumberOfScenes(id);
        String scenesText = "scenes";

        if (numOfScenes == 1) {
            scenesText = "scene";
        }

        scenesLinearLayout.addView(createNewLowerInfoTextView(Integer.toString(numOfScenes), true));
        scenesLinearLayout.addView(createNewLowerInfoTextView(scenesText, false));
        scenesLinearLayout.setBackground(getResources().getDrawable(R.drawable.border));

        //Percentage Complete
        LinearLayout percentCompleteLinearLayout = new LinearLayout(this);
        percentCompleteLinearLayout.setOrientation(LinearLayout.VERTICAL);

        percentCompleteLinearLayout.addView(createNewLowerInfoTextView(myDb.getProjectPercentComplete(id) + "%", true));
        percentCompleteLinearLayout.addView(createNewLowerInfoTextView("complete", false));
        percentCompleteLinearLayout.setBackground(getResources().getDrawable(R.drawable.border));

        //Release Date
        LinearLayout releaseLinearLayout = new LinearLayout(this);
        releaseLinearLayout.setOrientation(LinearLayout.VERTICAL);

        String releaseDate = myDb.getProjectReleaseDate(id);

        if (!releaseDate.equals("No release date.")) {
          releaseLinearLayout.addView(createNewLowerInfoTextView("Release Date", false));
        }

        releaseLinearLayout.addView(createNewLowerInfoTextView(releaseDate, true));

        releaseLinearLayout.setBackground(getResources().getDrawable(R.drawable.border));

        //Create full lower info LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        scenesLinearLayout.setLayoutParams(params);
        percentCompleteLinearLayout.setLayoutParams(params);
        releaseLinearLayout.setLayoutParams(params);

        lowerInfoLinearLayout.addView(scenesLinearLayout);
        lowerInfoLinearLayout.addView(percentCompleteLinearLayout);
        lowerInfoLinearLayout.addView(releaseLinearLayout);

        return lowerInfoLinearLayout;
    }

    private TextView createNewLowerInfoTextView(String text, boolean upper) {
        TextView thisTextView = new TextView(this);
        thisTextView.setText(text);
        thisTextView.setGravity(Gravity.CENTER);
        thisTextView.setTextColor(Color.BLACK);

        int textSize;
        if (upper) {
            textSize = 18;
        } else {
            textSize = 12;
        }

        thisTextView.setTextSize(textSize);

        return thisTextView;
    }

    private TextView createProjectTitleTextView(final String title, final String id) {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0,0,0,0);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width = size.x;

        TextView projTitleTextView = new TextView(this);
        projTitleTextView.setText(title);
        projTitleTextView.setTextSize(28);
        projTitleTextView.setTypeface(null, Typeface.BOLD);
        projTitleTextView.setTextColor(Color.parseColor("#000000"));
        projTitleTextView.setBackground(getResources().getDrawable(R.drawable.border));
        projTitleTextView.setPadding(10,10,10,10);
        projTitleTextView.setLayoutParams(llp);
        projTitleTextView.setWidth(width-16);
        projTitleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        return projTitleTextView;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectsPage.this, Home.class));
    }
}
