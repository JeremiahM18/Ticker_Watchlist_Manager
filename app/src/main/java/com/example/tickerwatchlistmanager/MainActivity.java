package com.example.tickerwatchlistmanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private TickerViewModel vm;
    FragmentManager fg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(TickerViewModel.class);
        vm.defaultEntries();

        if(savedInstanceState == null){
            fg = getSupportFragmentManager();
            FragmentTransaction trans = fg.beginTransaction();
            trans.add(R.id.listFragment, new ListFragment(), "TickerListFragment");
            trans.add(R.id.webFragment, new InfoWebFragment(), "InfoWebFragment");
            trans.commit();
        }

    }
}