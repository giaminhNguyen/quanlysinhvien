package com.example.quanlysinhvien.Other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;

public class CustomToast {
    public CustomToast(){}

    public static Toast makeText(Context context,String nd,int duration,boolean check){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast,null);
        TextView tv = view.findViewById(R.id.toast_text);
        tv.setText(nd);
        Toast toast = new Toast(context);
        if(!check){
            ImageView img_type = view.findViewById(R.id.toast_icon);
            LinearLayout layout = view.findViewById(R.id.toast_Type);
            layout.setBackgroundResource(R.drawable.error);
            img_type.setImageResource(R.drawable.close_20);
        }
        toast.setDuration(duration);
        toast.setView(view);
        return toast;
    }
}
