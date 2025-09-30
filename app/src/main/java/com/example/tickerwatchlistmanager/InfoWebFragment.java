package com.example.tickerwatchlistmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class InfoWebFragment extends Fragment {

    public InfoWebFragment() {
        // Required empty public constructor
    }

    private WebView webview;
    private TickerViewModel vm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_web, container, false);
    }
}