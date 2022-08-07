package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.quanlysinhvien.DTO.ChuyenNganh;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_ChuyenNganh {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private Share share;
    private String code;
    private DAO_LopHoc db_lopHoc;
    private DAO_QLMH db_QLMH;
    private DAO_SinhVien db_sinhVien;

    public DAO_ChuyenNganh(Context context){
        dataBase = new DataBase(context);
        dataBase.createTable();
        db = dataBase.getWritableDatabase();
        db_lopHoc = new DAO_LopHoc(context);
        db_QLMH = new DAO_QLMH(context);
        db_sinhVien = new DAO_SinhVien(context);
        share = new Share(context);
        code = share.getNGANH();
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

    public void create(String ma_nganh,String ngang_hoc){
        db.insert(code,null,getValues(DataBase.MA_NGANH,ma_nganh,DataBase.NGANH_HOC,ngang_hoc));
    }

    public ArrayList<ChuyenNganh> read(){
        ArrayList<ChuyenNganh> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        if(cursor.moveToFirst()){
            do{
                list.add(new ChuyenNganh(cursor.getString(0),cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<String> readAdapter(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0) + "-" +cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public String readDongOfMa(String ma_nganh){
        ChuyenNganh get = readObj(ma_nganh);
        return get.getMa_nganh() + "-" + get.getNganh_hoc();
    }

    public ChuyenNganh readObj(String ma_nganh){
        int index = getIndex(ma_nganh);
        if(index >= 0)
            return read().get(index);
        return null;
    }

    public ArrayList<String> readMa(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public String readMaOfIndex(int index){
        return read().get(index).getMa_nganh();
    }

    public void update(String ma_nganh_old,String ma_nganh,String ngang_hoc){
        ContentValues values = getValues(DataBase.NGANH_HOC,ngang_hoc);
        if(ma_nganh_old.equals(ma_nganh)){
            db.update(code, values,DataBase.MA_NGANH + " = ?",new String[]{ma_nganh_old});
        }else{
            values.put(DataBase.MA_NGANH,ma_nganh);
            db.update(code, values,DataBase.MA_NGANH + " = ?",new String[]{ma_nganh_old});
            db_QLMH.updateMaNganh(ma_nganh_old,ma_nganh);
            db_lopHoc.updateMaNganh(ma_nganh_old,ma_nganh);
            db_sinhVien.updateMaNganh(ma_nganh_old,ma_nganh);
        }
    }

    public void delete(String ma_nganh){
        db_lopHoc.deleteWithMaNganh(ma_nganh);
        db_QLMH.deleteWithMaNganh(ma_nganh);
        db.delete(code,DataBase.MA_NGANH + " = ?",new String[]{ma_nganh});
    }

    public int getIndex(String ma){
        return readMa().indexOf(ma);
    }

}
