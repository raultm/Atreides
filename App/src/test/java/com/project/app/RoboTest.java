package com.robotest.test;

import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Button;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowSQL;

import static org.robolectric.Robolectric.shadowOf;

import com.project.app.MainActivity;
import com.project.app.R;

import com.project.app.model.interfaces.Model;
import com.project.app.model.Party;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RoboTest {

    private MainActivity activity;
    private Button populateDatabase;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        populateDatabase = (Button) activity.findViewById(R.id.button_populate_database);
        Robolectric.bindShadowClass(SQLiteOpenHelper.class);
    }

    @Test
    public void mainLabelHasStringHelloWorldFromResources() throws Exception {
        populateDatabase.performClick();

        Party party = new Party(activity);
        List<Model> parties = party.findAll();
        assertThat(0, equalTo(parties.size()));
    }



}
