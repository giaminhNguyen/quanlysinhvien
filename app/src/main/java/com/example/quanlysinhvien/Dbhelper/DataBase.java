package com.example.quanlysinhvien.Dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quanlysinhvien.SharePre.Share;

public class DataBase extends SQLiteOpenHelper {
    public static final String NAME_DB = "QLSV.db";
    private Context context;
    public static final String LOGIN = "LOGIN",USER_NAME = "USER_NAME",
    PASS_WORD = "PASS_WORD",NGAY_SINH = "NGAY_SINH",GIOI_TINH = "GIOI_TINH",
    SDT = "SDT",MA_LOP = "MA_LOP",MA_NGANH = "MA_NGANH"
    ,MA_SV = "MSV",TEN_SV = "TEN_SV",NGANH_HOC = "NGANH_HOC",MA_QLMH = "MA_QLMH",
    MA_MON = "MA_MON",TEN_MON = "TEN_MON",TIN_CHI = "TIN_CHI",MA_POINT = "MA_POINT",DIEM = "DIEM";
    private SQLiteDatabase db;
    public DataBase(Context context) {
        super(context, NAME_DB, null, 1);
        this.context = context;
        db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_login = "CREATE TABLE IF NOT EXISTS "+ LOGIN + "("+USER_NAME + " TEXT PRIMARY KEY,"
                 + PASS_WORD + " TEXT NOT NULL)";
        db.execSQL(table_login);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void createTable(){
        Share SHARE = new Share(context);
        String table_mon_hoc = "CREATE TABLE IF NOT EXISTS "+ SHARE.getMON_HOC() + "("+
                MA_MON + " TEXT PRIMARY KEY," +
                TEN_MON + " TEXT NOT NULL," + TIN_CHI + " INTEGER NOT NULL)";
        db.execSQL(table_mon_hoc);

        String table_nganh_hoc = "CREATE TABLE IF NOT EXISTS "+ SHARE.getNGANH() + "("+
                MA_NGANH + " TEXT PRIMARY KEY,"
                + NGANH_HOC + " TEXT NOT NULL)";
        db.execSQL(table_nganh_hoc);

        String table_QLHM = "CREATE TABLE IF NOT EXISTS " + SHARE.getQLMH() + "(" +
                MA_QLMH + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MA_NGANH + " TEXT NOT NULL REFERENCES " + SHARE.getNGANH() + " ("+MA_NGANH+"),"+
                MA_MON + " TEXT NOT NULL REFERENCES " + SHARE.getMON_HOC() + " ("+MA_MON +"))";
        db.execSQL(table_QLHM);

        String table_lop = "CREATE TABLE IF NOT EXISTS " + SHARE.getLop() + "(" +
                MA_LOP + " TEXT PRIMARY KEY," +
                MA_NGANH + " TEXT NOT NULL REFERENCES "+ SHARE.getNGANH() + "(" + MA_NGANH +"))";
        db.execSQL(table_lop);

        String table_SV = "CREATE TABLE IF NOT EXISTS "+ SHARE.getSV() + "("+
                MA_SV + " TEXT PRIMARY KEY," +
                TEN_SV + " TEXT NOT NULL," +
                MA_LOP + " TEXT NOT NULL,"+
                MA_NGANH + " TEXT NOT NULL REFERENCES " + SHARE.getNGANH() + "(" + MA_NGANH +")," +
                SDT + " TEXT NOT NULL," +
                GIOI_TINH + " TEXT NOT NULL," +
                NGAY_SINH + " TEXT NOT NULL)";
        db.execSQL(table_SV);

        String table_Point = "CREATE TABLE IF NOT EXISTS " + SHARE.getPOINT() + "( "+
                MA_POINT +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                MA_SV + " TEXT NOT NULL REFERENCES " + SHARE.getSV() + "(" + MA_SV + "),"+
                MA_MON + " TEXT NOT NULL REFERENCES "  + SHARE.getMON_HOC() + "(" + MA_MON + ")," +
                MA_NGANH + " TEXT NOT NULL REFERENCES " + SHARE.getNGANH() + "(" + MA_NGANH +"), "  +
                DIEM + " REAL DEFAULT 0.0)";
        db.execSQL(table_Point);
    }
}
