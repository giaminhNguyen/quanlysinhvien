package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.DTO.Account;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_Account {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private String code;

    public DAO_Account(Context context){
        dataBase = new DataBase(context);
        db = dataBase.getWritableDatabase();
        code = DataBase.LOGIN;
    }
    private ContentValues getValues(Object ... value){
        ContentValues values = new ContentValues();
        String colunm;
        for(int i = 0; i < value.length; i += 2){
            colunm = (String)value[i];
            switch (value[i + 1].getClass().getSimpleName()){
                case "String":
                    values.put(colunm,(String)value[i + 1]);
                    break;
                case "Integer":
                    values.put(colunm,(int)value[i + 1]);
                    break;
                case "Float":
                    values.put(colunm,(float)value[i + 1]);
                    break;
                case "Double":
                    values.put(colunm,(double)value[i + 1]);
                    break;
            }
        }
        return values;
    }

    public void create(String user_name,String pass_word){
        ContentValues values = getValues(DataBase.USER_NAME,user_name,DataBase.PASS_WORD,pass_word);
        db.insert(code,null,values);
    }

    public ArrayList<Account> read(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        ArrayList<Account> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                list.add(new Account(cursor.getString(0),cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public Account readObj(String user_name){
        int index = getIndex(user_name);
        if(index >= 0)
            return read().get(index);
        return null;
    }

    public ArrayList<String> readMa(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        ArrayList<String> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void update(String user_name_old,String user_name,String pass_word){
        ContentValues values = getValues(DataBase.USER_NAME,user_name,DataBase.PASS_WORD,pass_word);
        db.update(code,values,DataBase.USER_NAME + " = ?",new String[]{user_name_old});
    }

    public void delete(String user_name){
        db.delete(code,DataBase.USER_NAME + " = ?",new String[]{user_name});
    }

    public int getIndex(String user_name){
        return readMa().indexOf(user_name);
    }

}
