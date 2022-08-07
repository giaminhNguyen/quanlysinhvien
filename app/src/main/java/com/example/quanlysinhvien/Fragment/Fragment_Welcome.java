package com.example.quanlysinhvien.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.quanlysinhvien.DAO.DAO_Account;
import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.Other.Action;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.SharePre.Share;

public class Fragment_Welcome extends Fragment implements View.OnClickListener {
    private View view;
    private Share share;
    private TextView firstName;
    private Button lop,nganh,webSite,logOut;
    private Action action;
    private FragmentManager manager;

    public static Fragment_Welcome newInstance() {
        Fragment_Welcome fragment = new Fragment_Welcome();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome,container,false);
        anhXa();
        other();
        init();
        return view;
    }

    private void other() {
        share = new Share(getActivity());
        action = new MainActivity();
        manager = getActivity().getSupportFragmentManager();
        firstName.setText(share.getTK());
    }

    private void init() {
        addAction();
    }

    private void addAction() {
        lop.setOnClickListener(this);
        nganh.setOnClickListener(this);
        logOut.setOnClickListener(this);
        webSite.setOnClickListener(this);
    }

    private void anhXa() {
        lop = view.findViewById(R.id.welcome_btn_lop);
        nganh = view.findViewById(R.id.welcome_btn_daoTao);
        webSite = view.findViewById(R.id.welcome_btn_webSite);
        logOut = view.findViewById(R.id.welcome_btn_logOut);
        firstName = view.findViewById(R.id.welcome_tv_firstName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.welcome_btn_lop:
                actionLop();
                break;
            case R.id.welcome_btn_daoTao:
                actionDaoTao();
                break;
            case R.id.welcome_btn_webSite:
                actionWebSite();
                break;
            case R.id.welcome_btn_logOut:
                actionLogOut();
        }
    }

    private void actionLogOut() {
        getActivity().finish();
        System.exit(0);
    }

    private void actionWebSite() {
        Toast.makeText(getActivity(), "ActionWeb", Toast.LENGTH_SHORT).show();
    }

    private void actionDaoTao() {
        action.chuyen_Fragment(manager,Fragment_DaoTao.newInstance());
    }

    private void actionLop() {
        action.chuyen_Fragment(manager,Fragment_Lop.newInstance(0));
    }
}
