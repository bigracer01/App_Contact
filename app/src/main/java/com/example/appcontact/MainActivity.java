package com.example.appcontact;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appcontact.provider.ItemContentProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static BDContact contactdb;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static final String EXTRA_NAME = "1";
    public static final String EXTRA_NUMBER = "2";
    private FileOutputStream fos;


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactdb = new BDContact(this);

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "test.txt");
        try {
            fos = new FileOutputStream(file);
            requestPermissions();
            readcontact(fos);
            fos.close();

            Toast.makeText(MainActivity.this, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        System.out.println(ItemContentProvider.URI_ITEM);
    }

    @Override
    protected void onResume(){
        super.onResume();

        try {
            readcontact(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            writer.write("{".getBytes());
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
                writer.write("{\"name\": \"".getBytes());
                writer.write(contact1.get_name().getBytes());
                writer.write("\",\"number\" : \"".getBytes());
                writer.write(contact1.get_number().getBytes());
                writer.write("\"},".getBytes());
                contactdb.insertContact(contact1);
                System.out.println(contactdb.getBdd());
                System.out.println(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
            }
            writer.write("}".getBytes());
        }
        ContactAdaptater adaptater = new ContactAdaptater(getApplicationContext(), R.layout.item, phonecontacts);
        listView.setAdapter(adaptater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                String contactname = phonecontacts.get(position).get_name();
                String contactphone = phonecontacts.get(position).get_number();
                intent.putExtra(EXTRA_NAME,contactname);
                intent.putExtra(EXTRA_NUMBER, contactphone);
                startActivity(intent);
            }
        });
    }

    public static LinearLayout.LayoutParams getLayoutParam(){
        return new LinearLayout.LayoutParams(displayMetrics.widthPixels / 4, displayMetrics.heightPixels/7);
    }



    private void requestPermissions() {
        // below line is use to request
        // permission in the current activity.
        Dexter.withActivity(this)
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(Manifest.permission.READ_CONTACTS,
                        // below is the list of permissions
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS)
                // after adding permissions we are
                // calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some
                        // permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            // this method is use to handle error
            // in runtime permissions
            @Override
            public void onError(DexterError error) {
                // we are displaying a toast message for error message.
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                // below line is use to run the permissions
                // on same thread and to check the permissions
                .onSameThread().check();
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel();
                // below is the intent from which we
                // are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when
                // user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used
        // to display our dialog
        builder.show();
    }


}