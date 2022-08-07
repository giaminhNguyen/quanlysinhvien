package com.example.quanlysinhvien.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlysinhvien.Adapter.Adapter_Fragment;
import com.example.quanlysinhvien.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Fragment_Nganh extends Fragment {
    private View view;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Adapter_Fragment fragmentAdapter;

    public static Fragment_Nganh newInstance() {
        Fragment_Nganh fragment = new Fragment_Nganh();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nganh,container,false);
        anhXa();
        addTabLayout();
        return view;
    }

    private void addTabLayout() {
        fragmentAdapter = new Adapter_Fragment(getActivity(),new Fragment[]{Fragment_ChuyenNganh.newInstance(),Fragment_DanhSachMon.newInstance()});
        viewPager.setAdapter(fragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position == 0)
                tab.setText("Chuyên Ngành");
            else
                tab.setText("Môn Chuyên Ngành");
        }).attach();
    }

    private void anhXa() {
        viewPager = view.findViewById(R.id.frag_nganh_viewPager);
        tabLayout = view.findViewById(R.id.frag_nganh_tabLayout);
    }
}
