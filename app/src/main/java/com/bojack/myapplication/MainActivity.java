package com.bojack.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bojack.myapplication.adapter.ListAdapterAppsRecent;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_listaApp;
    RecyclerView rc_favoritesApp;
    ImageView wifiStatus_img;
    TextView hora_tv;


    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_listaApp = findViewById(R.id.btn_listaApp);
        rc_favoritesApp = findViewById(R.id.rc_favoritesApp);
        hora_tv = findViewById(R.id.hora_tv);
        wifiStatus_img = findViewById(R.id.wifiStatus_img);

        clockInfo();

        btn_listaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListApp.class));
            }
        });
    }

    private void clockInfo() {

        int secund = 2;
        int tempo = secund * 1000;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            getStatusInfo(hora_tv , wifiStatus_img);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, tempo, tempo);

    }


    private void getStatusInfo(TextView hora_tv, ImageView wifiStatus_img) {

        LocalDateTime dataHoraAtual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dataHoraAtual = LocalDateTime.now();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hora_tv.setText(dataHoraAtual.getHour()+":"+dataHoraAtual.getMinute());
        }
        ConnectivityManager connManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            wifiStatus_img.setImageResource(R.drawable.icons_wifi);
        }else{
            wifiStatus_img.setImageResource(R.drawable.icons_wifi_erro);
        }

    }

    private void updateRecycler() {
        rc_favoritesApp.setLayoutManager(new GridLayoutManager(getApplicationContext(),4,LinearLayoutManager.VERTICAL,false));
        rc_favoritesApp.setAdapter(new ListAdapterAppsRecent(MainActivity.this));
    }


}