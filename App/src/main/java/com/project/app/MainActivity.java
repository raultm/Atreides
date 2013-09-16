package com.project.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.app.model.Party;
import com.project.app.model.interfaces.Model;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

     public void runTests(View view){
        startActivity(new Intent(this, TestsActivity.class));
    }
}
