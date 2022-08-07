package com.example.quanlysinhvien.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter_Fragment extends FragmentStateAdapter {
    private Fragment[] list_frag;
    public Adapter_Fragment(@NonNull FragmentActivity fragmentActivity, Fragment [] list) {
        super(fragmentActivity);
        this.list_frag = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list_frag[position];
    }

    @Override
    public int getItemCount() {
        return list_frag.length;
    }
}
