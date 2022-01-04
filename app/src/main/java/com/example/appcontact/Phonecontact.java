package com.example.appcontact;


public class Phonecontact {
    private String _name;
    private String _number;


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


    public void setname (String string){this._name=string ;}
    public void setnumber(String string){this._number = string;}

    }

