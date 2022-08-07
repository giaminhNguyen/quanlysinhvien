package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.DTO.Point;
import com.example.quanlysinhvien.DTO.QLMH;
import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_SinhVien {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private DAO_QLMH db_QLMH;
    private Share share;
    private String table;
    private DAO_Point db_point;
    private ArrayList<Point> list_point;
    private ArrayList<String> list_ma;

    public DAO_SinhVien(Context context){
        dataBase = new DataBase(context);
        dataBase.createTable();
        db = dataBase.getWritableDatabase();
        db_QLMH = new DAO_QLMH(context);
        db_point = new DAO_Point(context);
        list_point = new ArrayList<>();
        list_ma = new ArrayList<>();
        share = new Share(context);
        table = share.getSV();
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

    public void create(String ma_SV,String ten_SV,String ma_lop,String ma_nganh,String SDT,String gioi_tinh,String ngay_sinh){
        ContentValues values = getValues(DataBase.MA_SV,ma_SV,
                DataBase.TEN_SV,ten_SV,DataBase.MA_LOP,ma_lop,DataBase.MA_NGANH,ma_nganh,
                DataBase.SDT,SDT,DataBase.GIOI_TINH,gioi_tinh,DataBase.NGAY_SINH,ngay_sinh);
        db.insert(table,null,values);
        for(String x : db_QLMH.readMaMon(ma_nganh)){
            db_point.create(ma_SV,x,ma_nganh);
        }
    }

    public ArrayList<SinhVien> read(){
        ArrayList<SinhVien> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table,null);
        if(cursor.moveToFirst()){
            do{
                list.add(new SinhVien(cursor.getString(0),cursor.getString(1),cursor.getString(2)
                ,cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public SinhVien readObj(String ma_SV){
        int index = getIndex(ma_SV);
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

    public ArrayList<SinhVien> readWithMaNganh(String ma_nganh){
        ArrayList<SinhVien> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(3).equals(ma_nganh))
                    list.add(new SinhVien(cursor.getString(0),cursor.getString(1),cursor.getString(2)
                            ,cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void update(String ma_SV_old,String ma_SV,String ten_SV,String ma_lop, String ma_Nganh,String SDT,String gioi_tinh,String ngay_sinh){
        ContentValues values = getValues(DataBase.TEN_SV,ten_SV,DataBase.MA_LOP,ma_lop,
                DataBase.SDT,SDT,DataBase.GIOI_TINH,gioi_tinh,DataBase.NGAY_SINH,ngay_sinh);
        if(ma_SV.equals(ma_SV_old) && ma_Nganh.equals(getObj(ma_SV_old).getMa_nganh())){
            db.update(table,values,DataBase.MA_SV + " = ?",new String[]{ma_SV_old});
        }
        else{
            values.put(DataBase.MA_SV,ma_SV);
            values.put(DataBase.MA_NGANH,ma_Nganh);
            list_point = db_point.readWithMaSV(ma_SV_old);
            list_ma = db_QLMH.readMaMon(ma_Nganh);
            db_point.deleteWithMaSV(ma_SV_old);
            db.update(table,values,DataBase.MA_SV + " = ?",new String[]{ma_SV_old});
            changeNganh_monHoc(ma_SV,ma_Nganh,list_point,list_ma);
        }
    }

    private void changeNganh_monHoc(String ma_SV,String ma_nganh,ArrayList<Point> list_point,ArrayList<String> list_ma){
        for(int i = 0; i < list_point.size(); i ++){
            if(list_ma.contains(list_point.get(i).getMa_mon())){
                db_point.create(ma_SV,list_point.get(i).getMa_mon(),ma_nganh,list_point.get(i).getDiem());
                list_ma.remove(list_point.get(i).getMa_mon());
            }
        }
        for(String x : list_ma){
            db_point.create(ma_SV,x,ma_nganh);
        }
    }
    public void delete(String ma_SV){
        db_point.deleteWithMaSV(ma_SV);
        db.delete(table,DataBase.MA_SV + " = ?",new String[]{ma_SV});
    }

    public void deleteWithLop(String ma_Lop){
        for(SinhVien x : read()){
            if(x.getMa_lop().equals(ma_Lop))
                delete(x.getMa_SV());
        }
    }

    public int getIndex(String ma){
        return readMa().indexOf(ma);
    }
    public SinhVien getObj(String ma){
        return read().get(getIndex(ma));
    }


    public void updateMaNganh(String ma_nganh_old, String ma_nganh) {
        for(SinhVien x : readWithMaNganh(ma_nganh_old)){
            update(x.getMa_SV(),x.getMa_SV(),x.getTen_SV(),x.getMa_lop(),ma_nganh,x.getSDT(),x.getGioi_tinh(),x.getNgay_sinh());
        }
    }

    public void updateMaLop(String ma_lop_old, String ma_lop) {
        db.update(table,getValues(DataBase.MA_LOP,ma_lop),DataBase.MA_LOP + " = ?",new String[]{ma_lop_old});
    }
}
