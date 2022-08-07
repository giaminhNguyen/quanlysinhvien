package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.DTO.LopHoc;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_LopHoc {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private Share SHARE;
    private String table;
    private DAO_SinhVien db_sinhVien;

    public DAO_LopHoc(Context context){
        dataBase = new DataBase(context);
        dataBase.createTable();
        db = dataBase.getWritableDatabase();
        db_sinhVien = new DAO_SinhVien(context);
        SHARE = new Share(context);
        table = SHARE.getLop();
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
    public void create(String ma_lop,String ma_nganh){
        db.insert(table,null,getValues(DataBase.MA_LOP,ma_lop,DataBase.MA_NGANH,ma_nganh));
    }

    public ArrayList<LopHoc> read(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + table,null);
        ArrayList<LopHoc> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                list.add(new LopHoc(cursor.getString(0),cursor.getString(1)));
            }while(cursor.moveToNext());
        }
        return list;
    }

    public LopHoc readObj(String ma_lop){
        int index = getIndex(ma_lop);
        if(index >= 0)
            return read().get(index);
        return null;
    }


    public ArrayList<String> readMa(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return list;
    }


    public void update(String ma_lop_old,String ma_lop,String ma_nganh){
        ContentValues values  = getValues(DataBase.MA_NGANH,ma_nganh);
        if(ma_lop_old.equals(ma_lop)){
            db.update(table,values,DataBase.MA_LOP + " = ?",new String[]{ma_lop_old});
        }else{
            values.put(DataBase.MA_LOP,ma_lop);
            db.update(table,values,DataBase.MA_LOP + " = ?",new String[]{ma_lop_old});
            db_sinhVien.updateMaLop(ma_lop_old,ma_lop);
        }
    }

    public void updateMaNganh(String ma_nganh_old, String ma_nganh) {
        db.update(table,getValues(DataBase.MA_NGANH,ma_nganh),
                DataBase.MA_NGANH + " = ?",new String[]{ma_nganh_old});
    }

    public void delete(String ma_lop){
        db_sinhVien.deleteWithLop(ma_lop);
        db.delete(table,DataBase.MA_LOP + " = ?",new String[]{ma_lop});
    }


    public void deleteWithMaNganh(String ma_nganh){
        for(LopHoc x : read()){
            if(x.getMa_nganh().equals(ma_nganh))
                delete(x.getMa_lop());
        }
    }

    public int getIndex(String ma_lop){
        return readMa().indexOf(ma_lop);
    }

}
