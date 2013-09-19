package com.project.app.model.interfaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.List;

public abstract interface Model{

    public Context getContext();


    // The name of the Id Field
    public String getIdField();

    // The content of the Id Field
    public long getIdValue();
    public void setIdValue(long value);

    // Attributes
    public void setValue(String key, String value);
    public void setValue(String key, int value);
    public void setValue(String key, long value);

    public String getValue(String key);

    // Actions against Data Structure
    public boolean save(Model model); // Add or Update.
    public boolean delete(long id);
    public Model read(long paramLong);
    public List<Model> find(String paramString);

    // Parsers
    public List<Model> cursor2ListOptions(Cursor paramCursor);
    public void parseCursor(Cursor paramCursor);
    public ContentValues parse2ContentValues();

    // Populate
    public View populateItem(View view);
    public View populateListItem(View view);

    //public abstract String getSelectSQL(long paramLong);
}