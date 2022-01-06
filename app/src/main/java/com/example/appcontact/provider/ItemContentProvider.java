package com.example.appcontact.provider;

import static com.example.appcontact.ContactSQLite.TABLE_CONTACT;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appcontact.BDContact;
import com.example.appcontact.ContactSQLite;
import com.example.appcontact.MainActivity;

import java.time.DayOfWeek;

public class ItemContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.appcontact.provider";
    public static final String TABLE_NAME = TABLE_CONTACT;
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private ContentResolver contentResolver ;

    
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor c = null;
        if (getContext() != null){
            c = BDContact.getBdd().query(TABLE_CONTACT, new String[]{ContactSQLite.COLUMN_CONTACT_NAME, ContactSQLite.COLUMN_CONTACT_NUMBER}, null, null, null, null, null);
        }
        return  c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "type";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
