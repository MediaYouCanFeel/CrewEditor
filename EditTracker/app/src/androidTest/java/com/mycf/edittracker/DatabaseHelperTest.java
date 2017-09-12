package com.mycf.edittracker;

import android.support.test.InstrumentationRegistry;
import android.test.AndroidTestCase;

import com.mycf.edittracker.DatabaseHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by John on 9/9/2017.
 */
public class DatabaseHelperTest extends AndroidTestCase {

    private DatabaseHelper myDb;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myDb =  new DatabaseHelper(InstrumentationRegistry.getTargetContext());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }



    @Test
    public void testInsertNewProject() throws Exception {
        String title="Test Project";
        myDb.insertNewProject(title);
        String outputTitle = myDb.getProjectTitle("1");
        assertEquals(title, outputTitle);
    }

    @Test
    public void getAllProjects() throws Exception {

    }

    @Test
    public void getProjectTitle() throws Exception {

    }

    @Test
    public void updateProjectTitle() throws Exception {

    }

    @Test
    public void deleteProject() throws Exception {

    }

    @Test
    public void insertNewScene() throws Exception {

    }

    @Test
    public void updateScene() throws Exception {

    }

    @Test
    public void addWorkflowToScene() throws Exception {

    }

    @Test
    public void updateSceneStatus() throws Exception {

    }

    @Test
    public void updateSceneStatus1() throws Exception {

    }

    @Test
    public void getScenesForProject() throws Exception {

    }

    @Test
    public void getSceneNumber() throws Exception {

    }

    @Test
    public void getSceneLocation() throws Exception {

    }

    @Test
    public void getSceneTime() throws Exception {

    }

    @Test
    public void getWorkflow() throws Exception {

    }

    @Test
    public void insertNewWorkflow() throws Exception {

    }

    @Test
    public void getWorkflowSteps() throws Exception {

    }

    @Test
    public void convertArrayToString() throws Exception {

    }

    @Test
    public void convertStringToArray() throws Exception {

    }

}