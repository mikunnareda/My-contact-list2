package com.example.mycontactlist;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;
    public ContactDataSource(Context context){
        dbHelper = new ContactDBHelper(context) ;
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
}
