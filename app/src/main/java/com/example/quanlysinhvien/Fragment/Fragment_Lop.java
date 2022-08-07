package com.example.quanlysinhvien.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlysinhvien.Adapter.Adapter_Fragment;
import com.example.quanlysinhvien.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Fragment_Lop extends Fragment {
    private View view;
    private ViewPager2 viewPager;
    private BottomNavigationView naviB;
    private Adapter_Fragment fragAdapter;
    private int index;

    public static Fragment_Lop newInstance(int index) {
        Fragment_Lop fragment = new Fragment_Lop(index);
        return fragment;
    }

    public Fragment_Lop(int index){
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lop,container,false);
        anhXa();
        fragAdapter = new Adapter_Fragment(getActivity(),new Fragment[]{new Fragment_LopHoc(),new Fragment_SinhVien()});
        viewPager.setAdapter(fragAdapter);
        chuyenSeleted();
        return view;
    }
    private void anhXa() {
        viewPager = view.findViewById(R.id.frag_lop_viewPager);
        naviB = view.findViewById(R.id.frag_lop_naviBottom);
    }
    private void chuyenSeleted() {
        if(index == 0){
            naviB.setSelectedItemId(R.id.navib_lopHoc);
            viewPager.setCurrentItem(0);
        }
        else if(index == 1){
            naviB.setSelectedItemId(R.id.navib_sinhVien);
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoad();
    }

    private void onLoad() {
        init();
    }

    private void init() {
        addNaviB();
        addViewPager();

    }

    private void addNaviB() {
        naviB.setItemIconTintList(null);
        naviB.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navib_lopHoc:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navib_sinhVien:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });
    }

    private void addViewPager() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        naviB.setSelectedItemId(R.id.navib_lopHoc);
                        break;
                    case 1:
                        naviB.setSelectedItemId(R.id.navib_sinhVien);
                        break;
                }
            }
        });
    }
}
