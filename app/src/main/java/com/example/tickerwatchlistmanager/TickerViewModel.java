package com.example.tickerwatchlistmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;

public class TickerViewModel extends ViewModel {
    private static final int MAX = 6;

    private MutableLiveData<LinkedList<String>> tickers;
    private MutableLiveData<String> selected;

    public TickerViewModel() {
        tickers = new MutableLiveData<>(new LinkedList<>());
        selected = new MutableLiveData<>(null);
    }

    public void defaultEntries(){
        LinkedList<String> v = tickers.getValue();
        if(v == null){
            v = new LinkedList<>();
        }
        if(v.isEmpty()){
            v.add("NEE");
            v.add("AAPL");
            v.add("DIS");
        }
        tickers.setValue(v);
    }

    public void select(String str){
        selected.setValue(str);
    }

    public MutableLiveData<LinkedList<String>> getTickers() {
        return tickers;
    }

    public MutableLiveData<String> getSelected() {
        return selected;
    }

    public void addTicker(String str){
        if(str == null || str.isEmpty()){
            return;
        }

        LinkedList<String> v = new LinkedList<>();
        v.remove(str);

        if(v.size() < MAX){
            v.add(str);
        } else{
            v.set(MAX - 1, str);
        }
        tickers.setValue(v);

    }
}
