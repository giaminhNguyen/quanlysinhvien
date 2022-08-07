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

public class Fragment_DaoTao extends Fragment {
    private BottomNavigationView naviB;
    private ViewPager2 viewPager2;
    private Adapter_Fragment adapter_fragment;

    public static Fragment_DaoTao newInstance() {
        Fragment_DaoTao fragment = new Fragment_DaoTao();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dao_tao,container,false);
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

    private void addViewPager() {
        adapter_fragment = new Adapter_Fragment(getActivity(),new Fragment[]{Fragment_Nganh.newInstance(), Fragment_MonHoc.newInstance()});
        viewPager2.setAdapter(adapter_fragment);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        naviB.setSelectedItemId(R.id.dao_tao_navib_nganh);
                        break;
                    case 1:
                        naviB.setSelectedItemId(R.id.dao_tao_navib_monHoc);
                        break;
                }
            }
        });
    }

    private void addNaviB() {
        naviB.setItemIconTintList(null);
        naviB.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dao_tao_navib_nganh:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.dao_tao_navib_monHoc:
                    viewPager2.setCurrentItem(1);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        naviB = view.findViewById(R.id.fragment_dao_tao_naviB);
        viewPager2 = view.findViewById(R.id.fragment_dao_tao_viewPager);
    }
}
