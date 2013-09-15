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
        populateParties(4);

        Party party = new Party(this);
        List<Model> list = party.findAll();

        AppConfig.log("TEST", "4 Parties Added after clearTable(), result of findAll -> " + list.size());

        Iterator<Model> iterator = list.iterator();
        while (iterator.hasNext()) {
            AppConfig.log(iterator.next().toString());
        }
    }

    public void findParty(View view){
        populateParties(6);
        Party staticParty = new Party(this);

        Model ciudadanosModel = staticParty.read(5);

        AppConfig.log("TEST", ciudadanosModel.getValue("name"));
        AppConfig.log("TEST", ciudadanosModel.toString());
    }

    private void populateParties(int numOfPartiesToPopulate){
        Party party = new Party(this);
        party.clearTable();
        String[] parties = {"Ciudadanos", "PP", "PSOE", "IU"};

        for(int index = 0; index < numOfPartiesToPopulate; index++){
            party.setValue("_id", (index+1));
            party.setValue("name", parties[index%parties.length]);
            party.save(party);
        }
    }
}
