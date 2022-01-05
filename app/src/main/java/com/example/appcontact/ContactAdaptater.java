package com.example.appcontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class ContactAdaptater extends ArrayAdapter<Phonecontact> {

    private final Context _context;
    private LinkedList<Phonecontact> _phonecontact;

    public ContactAdaptater(Context context, int resource, LinkedList<Phonecontact> phonecontacts) {
        super(context, resource, phonecontacts);
        _context = context;
        _phonecontact = phonecontacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
        } else {
            convertView = (LinearLayout) convertView;
        }


        TextView viewName = (TextView) convertView.findViewById(R.id.Name);
        viewName.setText(_phonecontact.get(position).get_name());
        TextView viewNumber = (TextView) convertView.findViewById(R.id.number);
        viewNumber.setText(_phonecontact.get(position).get_number());
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.photo);
        viewImage.setImageBitmap(_phonecontact.get(position).get_photo_uri());


        return convertView;
    }
}
