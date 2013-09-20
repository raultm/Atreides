package com.project.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void runTests(View view){
        startActivity(new Intent(this, TestsActivity.class));
    }

    public void proyectoColibriTest(View view){
        AppConfig.log("proyectoColibriTest");
        JsonObject data = Api.action(this, Api.API_ACTION_PARTY);
        AppConfig.log(data.toString());
    }
}
