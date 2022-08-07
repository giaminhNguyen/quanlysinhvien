package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlysinhvien.DTO.Point;
import com.example.quanlysinhvien.DTO.QLMH;
import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DAO_QLMH {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private Share share;
    private String table;
    private DAO_Point db_point;

    public DAO_QLMH(Context context){
        dataBase = new DataBase(context);
        db = dataBase.getWritableDatabase();
        db_point = new DAO_Point(context);
        share = new Share(context);
        table = share.getQLMH();
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

    public void create(String maNgang, String maMon){
        ArrayList<String> list_ma = new ArrayList<>();
        ContentValues values = getValues(DataBase.MA_NGANH,maNgang,DataBase.MA_MON,maMon);
        db.insert(table,null,values);
        Cursor cursor = db.rawQuery("SELECT * FROM " +  share.getSV() + " WHERE " + DataBase.MA_NGANH + " = " + getValuesSQL(maNgang),null);
        if(cursor.moveToFirst()){
            do{
                db_point.create(cursor.getString(0),maMon,maNgang);
            }while (cursor.moveToNext());
        }
    }

    public ArrayList<QLMH> read(){
        ArrayList<QLMH> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " +  table,null);

        if(cursor.moveToFirst()){
            do{
                list.add(new QLMH(cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<String> readMaMon(String maNganh){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " +  table,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(1).equals(maNganh))
                    list.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void updateMaMon(String ma_mon_old, String ma_mon) {
        db_point.updateMaMon(ma_mon_old, ma_mon);
        db.update(table,getValues(DataBase.MA_MON,ma_mon),DataBase.MA_MON + " = ?",new String[]{ma_mon_old});
    }

    public void delete(String maNganh,String maMon){
        db_point.deleteMonHocOfNganh(maNganh,maMon);
        db.execSQL("DELETE FROM [" + table + "] WHERE " + DataBase.MA_NGANH + " = " + getValuesSQL(maNganh)  + " AND "
                + DataBase.MA_MON + " = " + getValuesSQL(maMon));
    }

    private String getValuesSQL(String ma){
        return "'"+ ma +"'";
    }

    public void deleteWithMonHoc(String maMon){
        db_point.deleteMonHoc(maMon);
        db.delete(table,DataBase.MA_MON + " = ?",new String[]{maMon});

    }

    public void deleteWithMaNganh(String maNganh){
        db_point.deleteWithMaNganh(maNganh);
        db.delete(table,DataBase.MA_NGANH + " = ?",new String[]{maNganh});
    }


    public void updateMaNganh(String ma_nganh_old, String ma_nganh) {
        db.update(table,getValues(DataBase.MA_NGANH,ma_nganh),DataBase.MA_NGANH + " = ?",new String[]{ma_nganh_old});
    }
}
