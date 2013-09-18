package com.project.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.TextView;

import com.project.app.AppConfig;
import com.project.app.db.Database;
import com.project.app.model.interfaces.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SQLiteModel implements Model {

    private static SQLiteOpenHelper mDbHelper;
    public static String TAG = "ATREIDES";

    public static void setDatabase(SQLiteOpenHelper database){
        mDbHelper = database;
    }
    public static SQLiteOpenHelper getDatabase(Context context){
        if(mDbHelper == null){
            mDbHelper = new Database(context);
        }
        return mDbHelper;
    }

    // Database Static Methods
    public static void clearTable(SQLiteModel model){
        mDbHelper = getDatabase(model.getContext());
        try{
            mDbHelper.getWritableDatabase().execSQL("DELETE FROM " + model.getTableName());
        } finally {
            mDbHelper.close();
        }
    }
    public static List<Model> find(SQLiteModel model, String sqlSentence){
        mDbHelper = getDatabase(model.getContext());
        List<Model> localList = null;
        try{
            Cursor localCursor;
            localCursor = mDbHelper.getReadableDatabase().rawQuery(sqlSentence, null);
            localList = model.cursor2ListOptions(localCursor);
            localCursor.close();
        } finally {
            mDbHelper.close();
        }
        return localList;
    }
    public static Model read(SQLiteModel model, long paramLong){
        mDbHelper = getDatabase(model.getContext());
        try{
            Cursor localCursor = mDbHelper.getReadableDatabase().rawQuery(model.getSqlWithCondition(model.getIdField(), paramLong), null);
            localCursor.moveToFirst();
            model.parseCursor(localCursor);
            localCursor.close();
            return model;
        }catch(Exception e){
            AppConfig.log(TAG, "SQLiteModel : " + e.toString());
            model.setIdValue(-1);
        } finally {
            mDbHelper.close();
        }
        return model;
    }
    public static Model findFirst(SQLiteModel model, String sqlSentence){
        // Igual que find pero con el TOP 1?
        mDbHelper = getDatabase(model.getContext());
        try{
            Cursor localCursor = mDbHelper.getReadableDatabase().rawQuery(sqlSentence, null);
            localCursor.moveToFirst();
            model.parseCursor(localCursor);
            localCursor.close();
            return model;
        }catch(Exception e){
            model.setIdValue(-1);
        } finally {
            mDbHelper.close();
        }
        return model;
    }
    public static int updateQuery(SQLiteModel model, String table, ContentValues values, String whereClause, String[] whereArgs){
        int rows = 0;
        mDbHelper = getDatabase(model.getContext());
        try{
            rows = mDbHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);
        } finally {
            mDbHelper.close();
        }
        return rows;
    }
    public static int deleteQuery(SQLiteModel model, String table, String whereClause, String[] whereArgs){
        int rows = 0;
        mDbHelper = getDatabase(model.getContext());
        try{
            rows = mDbHelper.getWritableDatabase().delete(table, whereClause, whereArgs);
        } finally {
            mDbHelper.close();
        }
        return rows;
    }
    public static boolean save(SQLiteModel model){
        boolean success = false;
        mDbHelper = getDatabase(model.getContext());
        try{
            SQLiteDatabase localSQLiteDatabase = mDbHelper.getWritableDatabase();
            ContentValues localContentValues = model.parse2ContentValues();
            long idValue = model.getIdValue();
            int rowsAffected = 0;
            // UPDATE
            if(idValue > 0){
                rowsAffected = localSQLiteDatabase.update(model.getTableName(), localContentValues, model.getIdField() + " = " + model.getIdValue(), null);
            }
            // ADD
            if(rowsAffected == 0){
                try{
                    model.setIdValue(localSQLiteDatabase.insertOrThrow(model.getTableName(), null, localContentValues));
                }catch(SQLiteConstraintException e){
                    model.setIdValue(-1);
                }
            }
            if (model.getIdValue() == -1)
                success = false;
            else
                success = true;
        }catch (Exception e){
            AppConfig.log(TAG, e.toString());
            AppConfig.log(TAG, e.getMessage());
            mDbHelper.close();
        }finally{
            mDbHelper.close();
        }
        return success;
    }
    public static boolean delete(SQLiteModel model, long id){
        mDbHelper = getDatabase(model.getContext());
        String deleteQuery = model.getTableName() + "." + model.getIdField() + " = ?";
        String[] values = {Long.toString(id)};
        try{
            mDbHelper.getWritableDatabase().delete(model.getTableName(), deleteQuery, values);
        } finally {
            mDbHelper.close();
        }
        return true;
    }
    // SQLBuilder
    public static String getSqlWithAndConditions(SQLiteModel model, String[] conditions){
        String whereClause = "";
        if(conditions.length > 0){
            whereClause+= " WHERE " + implode(conditions, " AND ");
        }
        String str = " SELECT *  FROM " + model.getTableName() + whereClause;
        return str;
    }
    // Helper
    public static String implode(String[] array, String glue) {
        String out = "";
        for(int i=0; i<array.length; i++) {
            if(i!=0) { out += glue; }
            out += array[i];
        }
        return out;
    }

    // Class
    public Context context;
    public String FIELD_ID = "_id";
    protected HashMap<String, SQLiteField> fields;
    protected HashMap<String, String> values;

    public SQLiteModel(Context paramContext){
        setContext(paramContext);
        fill(null);
    }

    // Getters/Setters
    public void setContext(Context paramContext){
        this.context = paramContext;
    }
    public Context getContext(){
        return this.context;
    }

    public void setIdField(String idFieldName){
        this.FIELD_ID = idFieldName;
    }
    public String getIdField(){
        return this.FIELD_ID;
    }

    public long getIdValue() {
        return Long.valueOf(getValues().get(getIdField()));
    }
    public void  setIdValue(long value) {
        setValue(getIdField(), value);
    }

    public String getValue(String key){
        return getValues().get(key);
    }
    public void setValue(String key, String value){
        getValues().put(key, value);
    }
    public void setValue(String key, int value){
        getValues().put(key, Integer.toString(value));
    }
    public void setValue(String key, long value){
        getValues().put(key, Long.toString(value));
    }


    // SQLBuilders
    public String getSelectSQL(long paramLong){
        return null;
    }
    public String getSqlWithCondition(String field, String value){
        return getSqlWithAndConditions(this, new String[]{getTableName() + "." + field + "='" + value + "'"});
    }
    public String getSqlWithCondition(String field, long value){
        return getSqlWithAndConditions(this, new String[]{getTableName() + "." + field + "='" + value + "'"});
    }
    public String getSqlWithCondition(String field, int value){
        return getSqlWithAndConditions(this, new String[]{getTableName() + "." + field + "='" + value + "'"});
    }
    public String getSqlWithCondition(String field, long value, String field2, long value2){
        String[] conditions = new String[]{
                getTableName() + "." + field + "=" + value,
                getTableName() + "." + field2 + "=" + value2
        };
        return getSqlWithAndConditions(this, conditions);
    }

    // Parsers
    public long parseToLong(String paramString){
        long longValue = 0;
        try{
            longValue = Long.parseLong(paramString);
        } catch (Exception ignored){

        }
        return longValue;
    }

    // Database
    public void clearTable(){
        clearTable(this);
    }
    public List<Model> find(String sqlSentence){
        return find(this, sqlSentence);
    }
    public List<Model> findAll(){
        return find(this, "SELECT * FROM " + getTableName());
    }
    public Model read(long paramLong){
        return read(this, paramLong);
    }
    public Model readFirst(String sqlSentence){
        return findFirst(this, sqlSentence);
    }
    public int updateQuery(ContentValues values, String whereClause, String[] whereArgs){
        return updateQuery(this, this.getTableName(), values, whereClause, whereArgs);
    }
    public int deleteQuery(String table, String whereClause, String[] whereArgs){
        return deleteQuery(this, this.getTableName(), whereClause, whereArgs);
    }
    public boolean save(Model model){
        return SQLiteModel.save((SQLiteModel) model);
    }
    public boolean delete(long id){
        return delete(this, id);
    }

    // General Model
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
    public HashMap<String, SQLiteField> getFields(){
        HashMap<String, SQLiteField> fields = new HashMap<String, SQLiteField>();
        fields.put(getIdField(), new SQLiteField(SQLiteField.INTEGER));
        return fields;
    };
    public HashMap<String, String> getValues(){
        if(this.values != null){
            return this.values;
        }
        this.values = new HashMap<String, String>();
        fill(null);
        return this.values;
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

    // Other
    public void setTextView(View paramView, int paramInt, String paramString){
        TextView localTextView = (TextView)paramView.findViewById(paramInt);
        if (localTextView != null)
            localTextView.setText(paramString);
    }
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

    // Abstract
    public abstract String getTableName();
    public abstract View populateItem(View view);
    public abstract View populateListItem(View view);
    public abstract List<Model> cursor2ListOptions(Cursor paramCursor);

}
