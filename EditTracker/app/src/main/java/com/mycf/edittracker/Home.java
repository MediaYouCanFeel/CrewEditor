package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDb = new DatabaseHelper(this);
        //myDb.onUpgrade(myDb.getWritableDatabase(),1,2);
        Button myProjectsButton = (Button) findViewById(R.id.my_projects_button);

        myProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ProjectsPage.class));
            }
        });



    }
}
