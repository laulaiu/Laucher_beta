package com.bojack.tv64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.bojack.tv64.adapter.ListAdapterApps;
import com.bojack.tv64.model.AppsMd;
import com.bojack.tv64.model.SharedPrefInfo;

import java.util.ArrayList;
import java.util.List;

public class ListApp extends AppCompatActivity {

    RecyclerView app_instalados;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);

        app_instalados = findViewById(R.id.app_instalados);

        try {
            getallapps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getallapps() throws PackageManager.NameNotFoundException {

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
        List<AppsMd> listaApp = new ArrayList<>();

        for (ResolveInfo ri : ril) {
            AppsMd ap = new SharedPrefInfo().getApp(getApplicationContext(), ri);
            listaApp.add(ap);
        }

        app_instalados.setLayoutManager(new LinearLayoutManager(ListApp.this));
        app_instalados.setAdapter(new ListAdapterApps(ListApp.this, listaApp));
    }
}