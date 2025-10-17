package com.example.tickerwatchlistmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InfoWebFragment extends Fragment {

    private static final String seekingAlphaHome = "https://seekingalpha.com";
    private static final String seekingAlphaSymbol = "https://seekingalpha.com/symbol/";
    private WebView web;
    private TickerViewModel vm;

//    public InfoWebFragment() {
//        // Required empty public constructor
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_web, container, false);
        web = v.findViewById(R.id.web);

        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        web.setWebViewClient(new WebViewClient());

        web.loadUrl(seekingAlphaHome);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(TickerViewModel.class);

        vm.getSelected().observe(getViewLifecycleOwner(), symbol -> {
            if(symbol != null && !symbol.isEmpty()){
                ((WebView) view.findViewById(R.id.web))
                        .loadUrl(seekingAlphaSymbol + symbol);
            }
        });

    }
}