package com.example.appcontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BDContact {
    private final static int VERSION_BDD = 1;
    private SQLiteDatabase bdd;
    private ContactSQLite maBaseSQLite;
    public int id = 0;

    public BDContact(Context context){
        //On crée la BDD et sa table :
        maBaseSQLite = new ContactSQLite(context,"Contact.db",null,VERSION_BDD);
    }
    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();

    }


    public void close(){

        maBaseSQLite.close();
    }

    public SQLiteDatabase getBdd(){
        return bdd;
    }

    public long insertContact(Phonecontact phonecontact){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        open();
        ContentValues values = new ContentValues();
        values.put(ContactSQLite.COLUMN_CONTACT_NAME,phonecontact.get_name());
        values.put(ContactSQLite.COLUMN_CONTACT_NUMBER,phonecontact.get_number());
        long id = bdd.insert(ContactSQLite.TABLE_CONTACT,null,values);
        close();
        return id;
    }

    public Phonecontact cursortocontact(Cursor cursor){
        if (cursor.getCount() == 0){
            return null;}
        else{
            cursor.moveToFirst();
            Phonecontact contact1 = new Phonecontact();
            contact1.setname(cursor.getString(0));
            contact1.setnumber(cursor.getString(1));
            System.out.println(contact1);
            return contact1;
        }
    }


}
