package com.project.app.model;

import android.content.Context;
import android.view.View;

import com.google.gson.JsonObject;
import com.project.app.model.interfaces.Model;

import java.util.HashMap;

public class Party extends SQLiteModel implements Model {

    public static final String TABLE_NAME = "parties";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public HashMap<String, SQLiteField> getModelStructure(){
        HashMap<String, SQLiteField> fields = new HashMap<String, SQLiteField>();
        //fields.put("id"         , new SQLiteField(SQLiteField.INTEGER));
        fields.put("logo"       , new SQLiteField(SQLiteField.TEXT));
        fields.put("name"       , new SQLiteField(SQLiteField.TEXT));
        fields.put("resource_uri", new SQLiteField(SQLiteField.TEXT));
        fields.put("validate"   , new SQLiteField(SQLiteField.TEXT));
        fields.put("web"        , new SQLiteField(SQLiteField.TEXT));
        return fields;
    }

    public Party(Context paramContext) {
        super(paramContext);
    }

    @Override
    public View populateItem(View view) {
        return null;
    }

    @Override
    public View populateListItem(View view) {
        return null;
    }

    public void fillWithJson(JsonObject jsonObject) {
        HashMap<String, String> values = getValues();
        values.put("_id"        , jsonObject.get("id").getAsString());
        //values.put("id"         , jsonObject.get("id").getAsString());
        values.put("logo"       , jsonObject.get("logo").getAsString());
        values.put("name"       , jsonObject.get("name").getAsString());
        values.put("resource_uri", jsonObject.get("resource_uri").getAsString());
        values.put("validate"   , jsonObject.get("validate").getAsString());
        try{
            values.put("web"        , jsonObject.get("web").getAsString());
        }catch(Exception e){
            values.put("web"        , "");
        }
    }
}
