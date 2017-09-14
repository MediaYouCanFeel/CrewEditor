package com.mycf.edittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    DatabaseHelper myDb;

    private static final String BUILD_TYPE = "NORMAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button myProjectsButton = (Button) findViewById(R.id.my_projects_button);

        myDb = new DatabaseHelper(this);

        switch (BUILD_TYPE) {
            case "DEMO":
                resetDatabase();
                setupDefaultWorkflow();
                demoSetup();
                break;
            case "CLEAN":
                resetDatabase();
                setupDefaultWorkflow();
                break;
            case "NORMAL":
                break;
            default:
                break;
        }

        myProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ProjectsPage.class));
            }
        });

    }

    private void resetDatabase() {
        myDb.onUpgrade(myDb.getWritableDatabase(),0,1);
    }

    private void demoSetup() {
        //Autoload Default Projects
        myDb.insertNewProject("DISCONNECTED");
        myDb.insertNewScene("1","1", "Computer Lab", "Night");
        myDb.insertNewScene("1","2", "Computer Lab", "Morning");
        myDb.insertNewScene("1","3", "Jack Office", "Morning");
        myDb.insertNewScene("1","4", "Dorm Room", "Noon");
        myDb.insertNewScene("1","5", "Computer Lab", "Evening");
        myDb.insertNewScene("1","6", "Date", "Night");
        myDb.insertNewScene("1","7", "Food Court", "Night");
        myDb.insertNewScene("1","8", "Computer Lab", "Night");

        myDb.insertNewProject("Second Time");
        myDb.insertNewScene("2","0", "Flash", "Various");
        myDb.insertNewScene("2","1", "Getting Ready", "Morning");
        myDb.insertNewScene("2","2", "The Second Time", "Continuous");
        myDb.insertNewScene("2","3", "Emmett Learns", "Afternoon");
        myDb.insertNewScene("2","4", "3MA Benches", "Continuous");
        myDb.insertNewScene("2","5", "Close Encounter", "Day");
//        myDb.insertNewScene("2","6", "3MA Benches", "Continuous");
//        myDb.insertNewScene("2","7", "How I Met My Ex", "Continuous");

//        for (int i = 1; i <= 16; i++) {
//            myDb.addWorkflowToScene("1", Integer.toString(i));
//        }
    }

    private void setupDefaultWorkflow() {
        //Creating Workflow Steps
        myDb.insertNewWorkflowStep("Catalogued", "CAT");
        myDb.insertNewWorkflowStep("Timeline Edited", "TE");
        myDb.insertNewWorkflowStep("Color Corrected", "CC");
        myDb.insertNewWorkflowStep("Audio Mixed", "AM");
        myDb.insertNewWorkflowStep("VFX Complete", "VFX");

        //TEMPORARY FOR V1 - ADDING DEFAULT WORKFLOW
        String workflowName = "DEFAULT";
        String[] workflowSteps = {"1", "2", "3", "4", "5"};
        myDb.insertNewWorkflow(workflowName, workflowSteps);
    }
}
