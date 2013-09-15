package com.project.app.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.project.app.model.interfaces.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Party extends SQLiteModel implements Model {

    public static final String TABLE_NAME = "parties";

    public HashMap<String, SQLiteField> getFields(){
        if(fields != null){
            return fields;
        }
        fields = new HashMap<String, SQLiteField>();
        fields.put("_id"        , new SQLiteField(SQLiteField.INTEGER));
        fields.put("id"         , new SQLiteField(SQLiteField.INTEGER));
        fields.put("logo"       , new SQLiteField(SQLiteField.TEXT));
        fields.put("name"       , new SQLiteField(SQLiteField.TEXT));
        fields.put("resource_id", new SQLiteField(SQLiteField.TEXT));
        fields.put("validate"   , new SQLiteField(SQLiteField.TEXT));
        fields.put("web"        , new SQLiteField(SQLiteField.TEXT));
        return fields;
    }

    public Party(Context paramContext) {
        super(paramContext);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public View populateItem(View view) {
        return null;
    }

    @Override
    public View populateListItem(View view) {
        return null;
    }

    @Override
    public long getIdValue() {
        return 0;
    }

    public List<Model> cursor2ListOptions(Cursor cursor) {
        ArrayList<Model> list = new ArrayList<Model>();
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Model party = new Party(this.context);
                party.parseCursor(cursor);
                list.add(party);
                cursor.moveToNext();
            }
        }
        return list;
    }

}
