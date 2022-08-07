package com.example.quanlysinhvien.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_Spinner extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private String check;

    public Adapter_Spinner(Context context,ArrayList<String> list,String check){
        this.context = context;
        this.list = list;
        this.check = check;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv;
        if(check != null){
            view = View.inflate(context, R.layout.item_spinner_1,null);
            tv = view.findViewById(R.id.color_spinner_1);
        }
        else{
            view = View.inflate(context, R.layout.item_spinner_2,null);
             tv = view.findViewById(R.id.color_spinner_2);
        }
        tv.setText(list.get(i));
        return view;
    }
}
