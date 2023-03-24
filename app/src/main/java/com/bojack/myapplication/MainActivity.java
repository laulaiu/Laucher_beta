package com.bojack.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bojack.myapplication.adapter.ListAdapterAppsRecent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_listaApp;
    RecyclerView rc_favoritesApp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_listaApp = findViewById(R.id.btn_listaApp);
        rc_favoritesApp = findViewById(R.id.rc_favoritesApp);

        SharedPreferences sharedPref = getSharedPreferences(
                "app_recente",
                Context.MODE_PRIVATE);


        Map<String, String> map = (Map<String, String>) sharedPref.getAll();

        List<String> list_app = new ArrayList<>(map.values());
        //List<String> list_app = Arrays.asList(map.values().toArray(new String[0]));

        /*for(String pack : map.values()){
            list_app.add(pack);
        }*/


        rc_favoritesApp.setLayoutManager(new GridLayoutManager(getApplicationContext(),4,LinearLayoutManager.HORIZONTAL,false));
        rc_favoritesApp.setAdapter(new ListAdapterAppsRecent(getApplicationContext(),list_app));

        btn_listaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListApp.class));
            }
        });
    }
}