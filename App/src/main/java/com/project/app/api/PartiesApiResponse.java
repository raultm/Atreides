package com.project.app.api;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.project.app.AppConfig;
import com.project.app.api.interfaces.ApiResponse;
import com.project.app.model.Party;
import com.project.app.model.SQLiteModel;
import com.project.app.model.interfaces.Model;

import java.util.Iterator;
import java.util.List;

public class PartiesApiResponse extends DefaultApiResponse implements ApiResponse {

    public PartiesApiResponse(DefaultApiResponse defaultApiReponse) {
        super(defaultApiReponse.getResponse());
    }

    public PartiesApiResponse(Future<JsonObject> future) {
        super(future);
    }

    public PartiesApiResponse(JsonObject jsonResponse) {
        super(jsonResponse);
    }

    @Override
    public JsonObject execute(Context context) {
        JsonArray parties = getObjects();
        int partiesCount = parties.size();
        Party party = new Party(context);
        for(int i= 0; i < partiesCount; i++){
            party.fillWithJson(parties.get(i).getAsJsonObject());
            SQLiteModel.save(party);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "OK");
        jsonObject.addProperty("message", partiesCount + " parties saved.");

        List<Model> partiesList = party.findAll();
        Iterator partiesIterator = partiesList.iterator();
        while(partiesIterator.hasNext()){
            party = (Party)partiesIterator.next();
            AppConfig.log(party.toString());
        }

        return jsonObject;
    }
}
