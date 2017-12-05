package com.example.hellen.smartmarket.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hellen.smartmarket.R;

/**
 * Created by yuyip on 12/4/2017.
 */

public class ScannerTab extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.scanner_tab, container, false);
        return v;
    }
}
