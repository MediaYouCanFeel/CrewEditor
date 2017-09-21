package com.mycf.edittracker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class EditProjectPage extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    DatabaseHelper myDb;

    private EditText projectReleaseEditText;

    private DatePickerDialog releaseDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String projId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_page);

        myDb = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        projId = b.getString("projectId");

        final EditText projectTitleEditView = (EditText) findViewById(R.id.editText_edit_proj_title);
        projectReleaseEditText = (EditText) findViewById(R.id.editText_release_date);

        String projectTitle = myDb.getProjectTitle(projId);
        projectTitleEditView.setText(projectTitle);

        final String projectReleaseDate = myDb.getProjectReleaseDate(projId);
        projectReleaseEditText.setText(projectReleaseDate);

        final TextView projectTitleTextView = (TextView) findViewById(R.id.textView_edit_proj_title_disp);
        final Button saveEditButton = (Button) findViewById(R.id.edit_project_save_button);
        final ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButton_delete_proj);
        final Button cancelEditButton = (Button) findViewById(R.id.edit_project_cancel_button);

        projectTitleTextView.setTypeface(FontManager.getTypeface(this, FontManager.POPPINS_SEMIBOLD));
        projectTitleTextView.setText(projectTitle);

        projectReleaseEditText.setInputType(InputType.TYPE_NULL);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        setDateTimeField();

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateProjectTitle(projectTitleEditView.getText().toString(), projId);
                myDb.updateProjectReleaseDate(projectReleaseEditText.getText().toString(), projId);
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(EditProjectPage.this, ProjectPage.class, bundle);
            }
        });

        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> bundle = new HashMap<>();
                bundle.put("projectId", projId);
                CrewUtils.sendIntent(EditProjectPage.this, ProjectPage.class, bundle);
            }
        });

//        deleteButton.setOnClickListener(dialogClickListener);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete this project permanently?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    myDb.deleteProject(projId);
                    startActivity(new Intent(EditProjectPage.this, ProjectsPage.class));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void setDateTimeField() {
        projectReleaseEditText.setOnClickListener(this);
        projectReleaseEditText.setOnFocusChangeListener(this);

        Calendar newCalendar = Calendar.getInstance();
        releaseDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                projectReleaseEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onFocusChange(View view, boolean shown) {
        if(view == projectReleaseEditText && shown) {
            releaseDatePickerDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == projectReleaseEditText) {
            releaseDatePickerDialog.show();
        }
    }

}
