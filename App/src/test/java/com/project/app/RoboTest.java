package com.robotest.test;

import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Button;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static org.robolectric.Robolectric.shadowOf;

import com.project.app.MainActivity;
import com.project.app.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RoboTest {

    private MainActivity activity;
    private TextView textView;
    private Button openAnotherMainActivityButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        textView = (TextView) activity.findViewById(R.id.mainactivity_text);
        openAnotherMainActivityButton = (Button) activity.findViewById(R.id.mainactivity_button_open);
    }

    @Test
    public void mainLabelHasStringHelloWorldFromResources() throws Exception {
        String testText = activity.getString(R.string.hello_world);
        String viewText = textView.getText().toString();
        assertThat(viewText, equalTo(testText));
    }

    @Test
    public void openAnotherMainActivityOnPerformClickInButton() throws Exception {
        openAnotherMainActivityButton.performClick();

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(MainActivity.class.getName()));
    }

}
