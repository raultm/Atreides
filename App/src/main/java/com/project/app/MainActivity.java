package com.project.app;

import android.app.Activity;
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

    public void createDatabase(View view){
        Party party = new Party(this);
        AppConfig.log(party.getCreateSql());
    }

    public void populateParty(View view){
        Party party = new Party(this);
        party.setValue("name", "Ciudadanos");
        AppConfig.log(party.toString());
    }

    public void populateDatabase(View view){
        Party party = new Party(this);
        party.clearTable();

        party.setValue("_id", "1");
        party.setValue("name", "Ciudadanos");
        party.save(party);

        party.setValue("_id", "2");
        party.setValue("name", "PP");
        party.save(party);

        party.setValue("_id", "3");
        party.setValue("name", "PSOE");
        party.save(party);

        party.setValue("_id", "4");
        party.setValue("name", "IU");
        party.save(party);

        List<Model> list = party.findAll();

        AppConfig.log("TEST", "4 Parties Added after clearTable(), result of findAll -> " + list.size());

        Iterator<Model> iterator = list.iterator();
        while (iterator.hasNext()) {
            AppConfig.log(iterator.next().toString());
        }
    }
}
