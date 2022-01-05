package com.example.appcontact;


import android.graphics.Bitmap;

public class Phonecontact {
    private String _name;
    private String _number;
    private Bitmap _photo_uri = null;


    public Phonecontact(String name, String number){
        _name = name;
        _number = number;

    }

    public Phonecontact() {

    }

    public String get_name(){
        return _name;
    }

    public String get_number(){
        return _number;
    }

    public Bitmap get_photo_uri() {
        return this._photo_uri;
    }

    public void set_photo_uri(Bitmap bp){this._photo_uri = bp;}


    public void setname (String string){this._name=string ;}
    public void setnumber(String string){this._number = string;}

    }

