package com.example.tickerwatchlistmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Pattern;

public class TickerViewModel extends ViewModel {
    private static final int MAX = 6;
    private static final Pattern TICKER_RE = Pattern.compile("^[A-Z]{1,5}$");
    // For round-robin version: Start at index 5 (6th slot)
    private int roundIndex = MAX - 1;

    private final MutableLiveData<LinkedList<String>> tickers = new MutableLiveData<>(new LinkedList<>());
    private final MutableLiveData<String> selected = new MutableLiveData<>(null);


//    public TickerViewModel() {
//        tickers = new MutableLiveData<>(new LinkedList<>());
//        selected = new MutableLiveData<>(null);
//    }

    public void defaultEntries() {
        LinkedList<String> v = tickers.getValue();
        if (v == null) {
            v = new LinkedList<>();
        }
        if (v.isEmpty()) {
            v.add("NEE");
            v.add("AAPL");
            v.add("DIS");
            tickers.setValue(v);
        }
    }

    public void select(String str) {
        selected.setValue(str);
    }

    public MutableLiveData<LinkedList<String>> getTickers() {
        return tickers;
    }

    public MutableLiveData<String> getSelected() {
        return selected;
    }

    // Round-robin replacement when full (cap = 6)
    public void addTicker(String str) {
        if (str == null) {
            return;
        }

        String t = str.trim().toUpperCase(Locale.US);
        if (!TICKER_RE.matcher(t).matches()) {
            return;
        }

        LinkedList<String> v = tickers.getValue();
        if (v == null) {
            v = new LinkedList<>();
        }

        // De-dupe: move to end if it already exits
        v.remove(t);

        if (v.size() < MAX) {
            v.add(t);
        } else {
            // Round-robin: replace at roundIndex, then advance 5->0->1->2->3->4->5->0...
            v.set(roundIndex, t);
            roundIndex = (roundIndex + 1) % MAX;
        }

//            v.set(roundIndex, t);
//            roundIndex =(roundIndex <= 0) ? MAX - 1 : roundIndex - 1;

        tickers.setValue(v);
    }

    public void removeTicker(String str) {
        if (str == null) {
            return;
        }
        String t = str.trim().toUpperCase(Locale.US);

        LinkedList<String> v = tickers.getValue();
        if (v == null || v.isEmpty()) {
            return;
        }
        if (v.remove(t)) {
            tickers.setValue(v);
        }
    }

    public void clearTickers() {
        LinkedList<String> v = new LinkedList<>();
        v.add("NEE");
        v.add("AAPL");
        v.add("DIS");
        tickers.setValue(v);
        roundIndex = MAX - 1;
        selected.setValue(null);
    }
}
