package com.example.appcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appcontact.provider.ItemContentProvider;

import java.io.IOException;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    public static BDContact contactdb;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactdb = new BDContact(this);

        try {
            readcontact();
        } catch (IOException e) {
            e.printStackTrace();
        }


        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        System.out.println(ItemContentProvider.URI_ITEM);
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @SuppressLint("Range")
    public void readcontact() throws IOException {

        Bitmap bp = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image);
        ContentResolver contentResolver  = this.getContentResolver();

        Cursor c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI}, null, null, null);

        ListView listView = (ListView)findViewById(R.id.Lcontact);
        LinkedList<Phonecontact> phonecontacts = new LinkedList<>();

        if (c == null){
            Toast.makeText(MainActivity.this, "Il n'y a pas de contact enregistré", Toast.LENGTH_SHORT).show();
        }else{
            while(c.moveToNext()){
                Phonecontact contact1 =  new Phonecontact(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE)),
                        c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                phonecontacts.add(contact1);
                String image_uri = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
                if ( image_uri ==null){
                    contact1.set_photo_uri(bp);
                }else{
                    contact1.set_photo_uri(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image_uri)));
                }
                contactdb.insertContact(contact1);
                System.out.println(contactdb.getBdd());
                System.out.println(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
            }
        }
        ContactAdaptater adaptater = new ContactAdaptater(getApplicationContext(), R.layout.item, phonecontacts);
        listView.setAdapter(adaptater);
    }

    public static LinearLayout.LayoutParams getLayoutParam(){
        return new LinearLayout.LayoutParams(displayMetrics.widthPixels / 4, displayMetrics.heightPixels/7);
    }



}