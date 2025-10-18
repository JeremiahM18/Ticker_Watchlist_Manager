package com.example.tickerwatchlistmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

        // Request RECEIVE_SMS at runtime if needed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            String[] perm = new String[]{Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, perm, 67);

        }

        vm = new ViewModelProvider(this).get(TickerViewModel.class);
        vm.defaultEntries();

        if (savedInstanceState == null) {
            fg = getSupportFragmentManager();
            FragmentTransaction trans = fg.beginTransaction();
            trans.add(R.id.listFragment, new ListFragment(), "TickerListFragment");
            trans.add(R.id.webFragment, new InfoWebFragment(), "InfoWebFragment");
            trans.commit();
        }

        // handle if launched by receiver
        handleSmsIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);             // so getIntent() returns the latest
        handleSmsIntent(intent);       // process new extras
    }

    private void handleSmsIntent(Intent intent) {
        if (intent == null) return;
        boolean fromSms = intent.getBooleanExtra("from_sms", false);
        if (!fromSms) return;

        String event = intent.getStringExtra("sms_event");
        String ticker = intent.getStringExtra("ticker");

        if ("no_watchlist".equals(event)) {
            android.widget.Toast.makeText(this, "No valid watchlist entry found.", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if ("invalid_ticker".equals(event)) {
            android.widget.Toast.makeText(this, "Invalid ticker: " +
                    (ticker == null ? "" : ticker), android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if ("valid_ticker".equals(event)) {
            if (ticker != null && !ticker.isEmpty()) {
                vm.addTicker(ticker);
                vm.select(ticker);
                android.widget.Toast.makeText(this, "Added " + ticker +
                        " to watchlist.", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                android.widget.Toast.makeText(this, "No ticker found.", android.widget.Toast.LENGTH_SHORT).show();
            }
        }

    }
}