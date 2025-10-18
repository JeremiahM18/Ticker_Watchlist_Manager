package com.example.tickerwatchlistmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InfoWebFragment extends Fragment {

    private static final String ARG_TICKER ="arg_ticker";
    private static final String HOME = "https://seekingalpha.com";
    private static final String SYMBOL = "https://seekingalpha.com/symbol/";
    private WebView web;
    private TickerViewModel vm;

//    public InfoWebFragment() {
//        // Required empty public constructor
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_web, container, false);
        web = v.findViewById(R.id.web);

        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);

        web.setWebViewClient(new WebViewClient());

        // If we're restoring after rotate, restore WebView's back/forward/history too
        if(savedInstanceState != null){
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl(HOME);
        }
        //web.loadUrl(home);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // If a ticker was passed as an argument (portrait WebActivity), load it once.
        String argTicker = getArguments() != null ? getArguments().getString(ARG_TICKER) : null;
        if(argTicker != null && !argTicker.isEmpty()){
            web.loadUrl(SYMBOL + argTicker);
            return; // no need to observe  in this case
        }

        // Otherwise (landscape two-pane), observe selection and update live
        vm = new ViewModelProvider(requireActivity()).get(TickerViewModel.class);
        vm.getSelected().observe(getViewLifecycleOwner(), symbol -> {
            if(symbol != null && !symbol.isEmpty()){
                web.loadUrl(SYMBOL + symbol);
//                ((WebView) view.findViewById(R.id.web))
//                        .loadUrl(seekingAlphaSymbol + symbol);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(web != null) web.saveState(outState);
    }

    @Override
    public void onDestroyView(){
        // Avoid leaks
        if(web != null){
            web.stopLoading();
            web.setWebViewClient(null);
            web.destroy();
            web = null;
        }
        super.onDestroyView();
        }
}