package com.example.appcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appcontact.provider.ItemContentProvider;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    public static BDContact contactdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactdb = new BDContact(this);

        readcontact();

        System.out.println(ItemContentProvider.URI_ITEM);
    }

    @SuppressLint("Range")
    public void readcontact(){
        ContentResolver contentResolver  = this.getContentResolver();
        Cursor c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

        ListView listView = (ListView)findViewById(R.id.Lcontact);
        LinkedList<Phonecontact> phonecontacts = new LinkedList<>();

        if (c == null){
            Toast.makeText(MainActivity.this, "Il n'y a pas de contact enregistré", Toast.LENGTH_SHORT).show();
        }else{
            while(c.moveToNext()){
                Phonecontact contact1 =  new Phonecontact(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE)),
                        c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                phonecontacts.add(contact1);
                contactdb.insertContact(contact1);
                System.out.println(contactdb.getBdd());
            }
        }
        ContactAdaptater adaptater = new ContactAdaptater(getApplicationContext(), R.layout.item, phonecontacts);
        listView.setAdapter(adaptater);
    }



}