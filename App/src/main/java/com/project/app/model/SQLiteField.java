package com.project.app.model;

public class SQLiteField {

    public static final int TEXT = 0;
    public static final int INTEGER = 1;

    private int fieldType;
    private String value = "";

    public SQLiteField(int fieldType){
        this(fieldType, "");
    }

    public SQLiteField(int fieldType, String value){
        this.fieldType = fieldType;
        this.value = value;
    }

    public int getFieldType(){
        return this.fieldType;
    }

    public void setValue(String value){
        this.value = value;
    }

    public void setValue(int value){
        this.value = Integer.toString(value);
    }

    public String get(){
        return this.value;
    }
    /*
    public int get(){
        return Integer.parseInt(this.value);
    }
    */
}
