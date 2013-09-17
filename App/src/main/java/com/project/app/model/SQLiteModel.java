package com.project.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import com.project.app.AppConfig;
import com.project.app.db.Database;
import com.project.app.model.interfaces.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SQLiteModel implements Model {

    public String FIELD_ID = "_id";
    public String TAG = AppConfig.LOG_TAG;
    Context context;
    long id;
    long _id;
    Database mDbHelper;
    protected HashMap<String, SQLiteField> fields;
    protected HashMap<String, String> values;

    //public SQLiteModel(){}

    public SQLiteModel(Context paramContext){
        this.context = paramContext;
        fill(null);
    }

    public void setContext(Context paramContext){
        this.context = paramContext;
    }

    public void clearTable(){
        this.mDbHelper = new Database(this.context);
        try{
            this.mDbHelper.getWritableDatabase().execSQL("DELETE FROM " + getTableName());
        } finally {
            this.mDbHelper.close();
        }
    }


    public List<Model> find(String paramString){
        this.mDbHelper = new Database(this.context);
        List<Model> localList = null;
        try{
            Cursor localCursor;
            localCursor = this.mDbHelper.getReadableDatabase().rawQuery(paramString, null);
            localList = cursor2ListOptions(localCursor);
            localCursor.close();
        } finally {
            this.mDbHelper.close();
        }
        return localList;
    }

    public List<Model> findAll(){
        return find("SELECT * FROM " + getTableName());
    }

    public boolean delete(long id){
        this.mDbHelper = new Database(this.context);
        String deleteQuery = getTableName() + "." + getIdField() + " = ?";
        String[] values = {Long.toString(id)};
        try{
            this.mDbHelper.getWritableDatabase().delete(getTableName(), deleteQuery, values);
        } finally {
            this.mDbHelper.close();
        }
        return true;
    }

    public long getIdValue() {
        return Long.valueOf(getValues().get(getIdField()));
    }

    public String getIdField(){
        return this.FIELD_ID;
    }

    public String getSelectSQL(long paramLong){
        return null;
    }

    public String getSqlWithCondition(String field, long value){
        String str = " SELECT *  FROM " + getTableName() + " WHERE " + getTableName() + "." + field + "=" + value;
        return str;
    }

    public String getSqlWithCondition(String field, String value){
        String str = " SELECT *  FROM " + getTableName() + " WHERE " + getTableName() + "." + field + "='" + value + "'";
        return str;
    }

    public String getSqlWithCondition(String field, long value, String field2, long value2){
        String str = " SELECT *  FROM " + getTableName()
                + " WHERE " + getTableName() + "." + field + "=" + value
                + " AND " + getTableName() + "." + field2 + "=" + value2;
        return str;
    }

    public long parseToLong(String paramString){
        long l2 = 0;
        try{
            l2 = Long.parseLong(paramString);
        } catch (Exception ignored){

        }
        return l2;
    }

    public Model read(long paramLong){
        this.mDbHelper = new Database(this.context);
        try{
            Cursor localCursor = this.mDbHelper.getReadableDatabase().rawQuery(getSqlWithCondition(getIdField(), paramLong), null);
            localCursor.moveToFirst();
            parseCursor(localCursor);
            localCursor.close();
            return this;
        }catch(Exception e){
            this._id = -1;
            //Configuration.log(TAG, "SQLiteModel : " + e.toString());
        } finally {
            this.mDbHelper.close();
        }
        return this;
    }

    public Model readFirst(String sql){
        this.mDbHelper = new Database(this.context);
        try{
            Cursor localCursor = this.mDbHelper.getReadableDatabase().rawQuery(sql, null);
            localCursor.moveToFirst();
            parseCursor(localCursor);
            localCursor.close();
            return this;
        }catch(Exception e){
            this._id = -1;
            AppConfig.log(TAG, "SQLiteModel : " + e.toString());
        } finally {
            this.mDbHelper.close();
        }
        return this;
    }

    public int updateQuery(String table, ContentValues values, String whereClause, String[] whereArgs){
        int rows = 0;
        this.mDbHelper = new Database(this.context);
        try{
            rows = this.mDbHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);
        } finally {
            this.mDbHelper.close();
        }
        return rows;
    }

    public int deleteQuery(String table, String whereClause, String[] whereArgs){
        int rows = 0;
        this.mDbHelper = new Database(this.context);
        try{
            rows = this.mDbHelper.getWritableDatabase().delete(table, whereClause, whereArgs);
        } finally {
            this.mDbHelper.close();
        }
        return rows;
    }

    public boolean save(Model model){
        boolean success = false;
        this.mDbHelper = new Database(this.context);
        try{
            SQLiteDatabase localSQLiteDatabase = this.mDbHelper.getWritableDatabase();
            ContentValues localContentValues = model.parse2ContentValues();
            // ADD
            try{
                this.id = localSQLiteDatabase.insertOrThrow(getTableName(), null, localContentValues);
            }catch(SQLiteConstraintException e){
                this.id = -1;
            }
            // UPDATE
            if(this.id == -1){
                if( (localSQLiteDatabase.update(getTableName(), localContentValues, model.getIdField() + " = " + model.getIdValue(), null) == 1 ))
                    this.id = model.getIdValue();
            } else {
                this.id = model.getIdValue();
            }
            if (this.id == -1)
                success = false;
            else
                success = true;
        }catch (Exception e){
            AppConfig.log(TAG, e.toString());
            AppConfig.log(TAG, e.getMessage());
            this.mDbHelper.close();
        }finally{
            this.mDbHelper.close();
        }
        if(success == true)
            this.read(this.id);
        return success;
    }

    public void setTextView(View paramView, int paramInt, String paramString){
        TextView localTextView = (TextView)paramView.findViewById(paramInt);
        if (localTextView != null)
            localTextView.setText(paramString);
    }

    public ContentValues parse2ContentValues() {
        ContentValues cv = new ContentValues();
        for (Map.Entry<String, SQLiteField> entry : fields.entrySet()){
            String fieldName = entry.getKey();

            if(getValues().get(fieldName).equals("")){
                if(fieldName.equals(getIdField())){ continue; }
                cv.put(fieldName, "");
                continue;
            }else{
                cv.put(fieldName, getValues().get(fieldName));
            }
            AppConfig.log(fieldName + "/" + cv.get(fieldName));
        }
        return cv;
    }

    public HashMap<String, String> getValues(){
        if(this.values != null){
            return this.values;
        }
        this.values = new HashMap<String, String>();
        fill(null);
        return this.values;
    }

    public void setValue(String key, String value){
        getValues().put(key, value);
    }

    public void setValue(String key, int value){
        getValues().put(key, Integer.toString(value));
    }

    public String getValue(String key){
        return getValues().get(key);
    }

    public void fill(HashMap<String, String> values){
        if(values == null){ values = new HashMap<String, String>(); }
        for (Map.Entry<String, SQLiteField> entry : getFields().entrySet()){
            String fieldName = entry.getKey();
            String value = "";
            if(values.containsKey(fieldName)){
                value = values.get(fieldName);
                getValues().put(fieldName, value);
            }else{
                getValues().put(fieldName, value);
            }
        }
    }

    public void parseCursor(Cursor cursor) {
        for (Map.Entry<String, SQLiteField> entry : getFields().entrySet()){
            String fieldName = entry.getKey();
            int fieldType = entry.getValue().getFieldType();

            HashMap<String, String> values = new HashMap<String, String>();
            String value = "";
            switch(fieldType){
                case SQLiteField.TEXT:
                    value = cursor.getString(cursor.getColumnIndex(fieldName));
                    break;
                case SQLiteField.INTEGER:
                    value = Integer.toString(cursor.getInt(cursor.getColumnIndex(fieldName)));
                    break;
            }
            getValues().put(fieldName, value);
        }
    }

    public String getCreateSql(){
        HashMap<String, SQLiteField> fields = getFields();

        String[] fieldColumns = new String[fields.size()];
        int index = 0;
        for (Map.Entry<String, SQLiteField> entry : fields.entrySet()){
            String fieldName = entry.getKey();
            String columnType = entry.getValue().getColumnType();
            if(fieldName.equals(getIdField())){
                fieldColumns[index] =  fieldName + " integer primary key autoincrement";
            }else{
                fieldColumns[index] = fieldName + " " + columnType;
            }
            index++;
        }
        return "CREATE TABLE " + getTableName() + " (" + implode(fieldColumns, ", ") + " );";
    }

    public static String implode(String[] array, String glue) {
        String out = "";
        for(int i=0; i<array.length; i++) {
            if(i!=0) { out += glue; }
            out += array[i];
        }
        return out;
    }

    public HashMap<String, SQLiteField> getFields(){
        return new HashMap<String, SQLiteField>();
    };

    @Override
    public String toString(){
        String toString = "\n" + "Entidad: " + getTableName() + "\n";
        HashMap<String, String> values = getValues();

        for (Map.Entry<String, String> entry : values.entrySet()){
            String fieldName = entry.getKey();
            String value = entry.getValue();
            toString+= "\t" + fieldName + ": " + value + "\n";
        }
        return toString;
    }

    public abstract String getTableName();
    public abstract View populateItem(View view);
    public abstract View populateListItem(View view);
    public abstract List<Model> cursor2ListOptions(Cursor paramCursor);

}
