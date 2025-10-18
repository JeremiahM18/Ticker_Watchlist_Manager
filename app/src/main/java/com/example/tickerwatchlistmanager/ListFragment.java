package com.example.tickerwatchlistmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private TickerViewModel vm;
    private ArrayAdapter<String> adapter;
    //private final ArrayList<String> data = new ArrayList<>();


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = v.findViewById(R.id.tickerList);
        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String symbol = adapter.getItem(position);
            if(symbol != null){
                vm.select(symbol);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(TickerViewModel.class);

        vm.getTickers().observe(getViewLifecycleOwner(), this::render);
    }

    private void render(List<String> list){
        if(list == null){
            return;
        }
        adapter.clear();
        adapter.addAll(list);           // VM already enforces max = 6
        adapter.notifyDataSetChanged();

    }
}