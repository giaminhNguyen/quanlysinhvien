package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_MonHoc {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private DAO_QLMH db_QLMH;
    private Share share;
    private String code;

    public DAO_MonHoc(Context context){
        dataBase = new DataBase(context);
        dataBase.createTable();
        db = dataBase.getWritableDatabase();
        db_QLMH = new DAO_QLMH(context);
        share = new Share(context);
        code = share.getMON_HOC();
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

    public void create(String ma_mon,String ten_mon,int tinChi){
        ContentValues values = getValues(DataBase.MA_MON,ma_mon,DataBase.TEN_MON,ten_mon,DataBase.TIN_CHI,tinChi);
        db.insert(code,null,values);
    }

    public ArrayList<MonHoc> read(){
        ArrayList<MonHoc> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        if(cursor.moveToFirst()){
            do{
                list.add(new MonHoc(cursor.getString(0),cursor.getString(1),cursor.getInt(2)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public MonHoc readObj(String ma_mon_hoc){
        int index = getIndex(ma_mon_hoc);
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


    public ArrayList<String> readAdapter(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + code,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0) + "-" + cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public String readDongWithMa(String maMon){
        if(getIndex(maMon) >= 0){
            MonHoc obj = readObj(maMon);
            return obj.getMa_mon() + "-" + obj.getTen_mon();
        }
        return null;
    }

    public ArrayList<MonHoc> readObjMa(ArrayList<String> maMon){
        ArrayList<MonHoc> list = new ArrayList<>();
        for(String x : maMon){
            list.add(readObj(x));
        }
        return list;
    }

    public void update(String ma_mon_old,String ma_mon,String ten_mon,int tinChi){
        ContentValues values = getValues(DataBase.TEN_MON,ten_mon,DataBase.TIN_CHI,tinChi);
        if(ma_mon_old.equals(ma_mon)){
            db.update(code,values,DataBase.MA_MON + " = ?",new String[]{ma_mon_old});
        }else{
            values.put(DataBase.MA_MON,ma_mon);
            db.update(code,values,DataBase.MA_MON + " = ?",new String[]{ma_mon_old});
            db_QLMH.updateMaMon(ma_mon_old,ma_mon);
        }
    }

    public void delete(String ma_mon){
        db_QLMH.deleteWithMonHoc(ma_mon);
        db.delete(code,DataBase.MA_MON + " = ?",new String[]{ma_mon});

    }
    public int getIndex(String ma){
        return readMa().indexOf(ma);
    }
}
