package com.example.quanlysinhvien.SharePre;

import android.content.Context;
import android.content.SharedPreferences;

public class Share {
    private SharedPreferences share_pre;
    private SharedPreferences.Editor editor;
    private final String NAME_FILE = "ACCOUNT", TK = "TK" ,MK = "MK", CHECK = "CHECK",
    LOP = "LOP",SV = "SV", NGANH = "NGANH", QLMH = "QLMH",MON_HOC = "MON_HOC",POINT = "POINT",USER_NAME = "USER_NAME";
    public Share(Context context){
        share_pre = context.getSharedPreferences(NAME_FILE,context.MODE_PRIVATE);
        editor = share_pre.edit();
    }

    public void putAccount(String user_name,String pass_word){
        editor.putString(TK,user_name);
        editor.putString(MK,pass_word);
        editor.putBoolean(CHECK,true);
        editor.apply();
    }
    public void putData(String name){
        String n = name.toUpperCase();
        editor.putString(LOP,"LOP_" + n);
        editor.putString(SV,"SV_" + n);
        editor.putString(NGANH,"NGANH_" + n);
        editor.putString(QLMH,"QLMH_" + n);
        editor.putString(MON_HOC,"MON_HOC_" + n);
        editor.putString(POINT,"POINT_" + n);
        editor.putString(USER_NAME,name);
        editor.apply();
    }
    public String getLop(){
        return share_pre.getString(LOP,"");
    }

    public String getTK() {
        return share_pre.getString(TK,"");
    }

    public String getMK() {
        return share_pre.getString(MK,"");
    }

    public String getSV() {
        return share_pre.getString(SV,"");
    }

    public String getNGANH() {
        return share_pre.getString(NGANH,"");
    }

    public String getQLMH() {
        return share_pre.getString(QLMH,"");
    }

    public String getPOINT(){ return share_pre.getString(POINT,"");}

    public String getMON_HOC() {
        return share_pre.getString(MON_HOC,"");
    }
    public boolean getCheck(){
        return  share_pre.getBoolean(CHECK,false);
    }
    public String getUSER_NAME(){return share_pre.getString(USER_NAME,"");}
}
