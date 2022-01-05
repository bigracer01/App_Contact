package com.example.appcontact;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static BDContact contactdb;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactdb = new BDContact(this);

        String fileName = "MyFile";
        String content = "hello world";

        FileOutputStream outputStream = null;

        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            readcontact(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(this.getFilesDir(), fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                System.out.println(strLine);
            }
            br.close();
            in.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
    public void readcontact(FileOutputStream writer) throws IOException {

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
                writer.write(contact1.get_name().getBytes());
                writer.write("\n".getBytes());
                writer.write(contact1.get_number().getBytes());
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