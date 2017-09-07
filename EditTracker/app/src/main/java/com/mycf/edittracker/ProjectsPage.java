package com.mycf.edittracker;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0,15,0,15);

        Cursor res = myDb.getAllData();
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
            final String projectTitle = res.getString(1);
            final String projectId = res.getString(0);
            TextView textView = new TextView(this);
            textView.setText(projectTitle);
            textView.setTextSize(28);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setBackground(getResources().getDrawable(R.drawable.border));
            textView.setPadding(10,10,10,10);
            textView.setLayoutParams(llp);
            textView.setWidth(width-16);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectsPage.this, ProjectPage.class);
                    Bundle b = new Bundle();
                    b.putString("projectId", projectId); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });
            linearLayout.addView(textView);
        }
    }
}
