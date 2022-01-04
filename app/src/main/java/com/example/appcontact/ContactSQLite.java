package com.example.appcontact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactSQLite extends SQLiteOpenHelper {

    public static final String TABLE_CONTACT = "T_Contacts";
    public static final String COLUMN_CONTACT_ID ="_id";
    public static final String COLUMN_CONTACT_NAME ="Contacts_Name";
    public static final String COLUMN_CONTACT_NUMBER ="Contacts_Number";

    private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT + " ("+COLUMN_CONTACT_ID+" INTEGER PRIMARY KEY, "
            + COLUMN_CONTACT_NAME + " VARCHAR(255), " + COLUMN_CONTACT_NUMBER + " VARCHAR(225) );";


    public ContactSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
