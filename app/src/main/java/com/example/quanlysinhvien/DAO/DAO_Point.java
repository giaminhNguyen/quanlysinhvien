package com.example.quanlysinhvien.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.quanlysinhvien.DTO.Point;
import com.example.quanlysinhvien.Dbhelper.DataBase;
import com.example.quanlysinhvien.SharePre.Share;

import java.util.ArrayList;

public class DAO_Point {
    private DataBase dataBase;
    private SQLiteDatabase db;
    private Share share;
    private String table;
    private Context context;

    public DAO_Point(Context context){
        dataBase = new DataBase(context);
        db = dataBase.getWritableDatabase();
        share = new Share(context);
        table = share.getPOINT();
        this.context = context;
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

    private Cursor selectAllWithCondition(String ...condition){
        String select_condition = "";
        for(int i = 0; i < condition.length; i ++){
            select_condition += condition[i];
            if(condition.length - i > 1)
                select_condition += " AND ";
        }
        if(condition.length > 0)
            return db.rawQuery("SELECT * FROM " + table + " WHERE " +  select_condition,null);
        else
            return db.rawQuery("SELECT * FROM " + table,null);
    }

    private Cursor selectWithCondition(int column,String ...condition){
        String select_condition = "",select_column = "";
        for(int i = 0; i < column; i ++){
            select_column += condition[i];
            if(column - i > 1)
                select_column += ",";
        }
        for(;column < condition.length; column ++){
            select_condition += condition[column];
            if(condition.length - column > 1)
                select_condition += " AND ";
        }
        if(condition.length > column)
            return db.rawQuery("SELECT "+ select_column +" FROM" + table + " WHERE " +  select_condition,null);
        else
            return db.rawQuery("SELECT "+ select_column +" FROM" + table,null);
    }


    public void create(String ma_SV,String ma_mon,String ma_nganh){
        ContentValues values = new ContentValues();
        values.put(DataBase.MA_SV,ma_SV);
        values.put(DataBase.MA_MON,ma_mon);
        values.put(DataBase.MA_NGANH,ma_nganh);
        db.insert(table,null,values);
    }
    public void create(String ma_SV, String ma_mon,String ma_nganh, Float diem) {
        ContentValues values = getValues(DataBase.MA_SV,ma_SV,DataBase.MA_MON,ma_mon,DataBase.MA_NGANH,ma_nganh,DataBase.DIEM,diem);
        db.insert(table,null,values);
    }

    public ArrayList<Point> read(){
        ArrayList<Point> list = new ArrayList<>();
        Cursor cursor = selectAllWithCondition();
        if(cursor.moveToFirst()){
            do{
                list.add(new Point(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getFloat(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public Point readObj(String ma_SV,String ma_mon,String ma_nganh){
        Point p = new Point();
        for(Point x : read()){
            if(x.getMa_SV().equals(ma_SV) && x.getMa_mon().equals(ma_mon) && x.getMa_nganh().equals(ma_nganh)){
                p = x;
                break;
            }
        }
        return p;
    }

    public ArrayList<Point> readWithMaSV(String ma_SV){
        ArrayList<Point> list = new ArrayList<>();
        Cursor cursor = selectAllWithCondition(DataBase.MA_SV + " = " + getValuesSQL(ma_SV));
        if(cursor.moveToFirst()){
            do{
                Log.d("ccc.tao","Size: " + list.size() + "/" + cursor.getString(2));
                list.add(new Point(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getFloat(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }


    public Float readDiemMonHocWithMaSV(String ma_SV,String ma_mon){
        Cursor cursor = selectWithCondition(1,DataBase.DIEM,DataBase.MA_SV + " = " + ma_SV, DataBase.MA_MON + " = " + ma_mon);
        if(cursor.moveToFirst()){
            return cursor.getFloat(0);
        }
        return null;
    }

    private String up(String a){
        return "'" + a + "'";
    }


    public void updateDiemMonWithMaSV(String ma_SV,String ma_mon,Float diem){
        db.execSQL("UPDATE " + table  + " SET " + DataBase.DIEM + " = " + diem + " WHERE "
                + DataBase.MA_SV + " = " + up(ma_SV) + " AND " + DataBase.MA_MON + " = " + up(ma_mon));
    }


    public void updateMaMon(String ma_mon_old, String ma_mon) {
        ContentValues values = new ContentValues();
        values.put(DataBase.MA_MON,ma_mon);
        db.update(table,values,DataBase.MA_MON + " = ?",new String[]{ma_mon_old});
    }

    public void deleteMonHoc(String ma_mon){
        db.delete(table,DataBase.MA_MON + " = ?",new String[]{ma_mon});
    }

    public void deleteMonHocOfNganh(String ma_nganh,String ma_mon){
        db.execSQL("DELETE FROM [" + table + "]" + " WHERE " +  DataBase.MA_NGANH + " = " + getValuesSQL(ma_nganh) +
                " AND " + DataBase.MA_MON + " = " + getValuesSQL(ma_mon));
    }

    public void deleteWithMaSV(String ma_SV){
        db.delete(table,DataBase.MA_SV + " = ?", new String[]{ma_SV});
    }

    public void deleteMonHocWithMaSV(String ma_SV,String ma_mon){
        db.execSQL("DELETE FROM [" + table + "]" + " WHERE " +  DataBase.MA_SV + " = " + getValuesSQL(ma_SV) +
                " AND " + DataBase.MA_MON + " = " + getValuesSQL(ma_mon));
    }

    public String getValuesSQL(String code){
        return "'" + code + "'";
    }


    public void deleteWithMaNganh(String maNganh) {
        db.delete(table,DataBase.MA_NGANH + " = ?", new String[]{maNganh});
    }

}
