package com.project.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.project.app.model.Party;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createDatabase(View view){
        Party party = new Party(this);
        AppConfig.log(party.getCreateSql());
    }

    public void populateParty(View view){
        Party party = new Party(this);
        party.setValue("name", "Ciudadanos");
        AppConfig.log(party.toString());
    }
}
