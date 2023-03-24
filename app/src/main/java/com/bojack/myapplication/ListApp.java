package com.bojack.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bojack.myapplication.adapter.ListAdapterApps;
import com.bojack.myapplication.model.AppsMd;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListApp extends AppCompatActivity {

    RecyclerView app_instalados;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);


        app_instalados = findViewById(R.id.app_instalados);


        try {
            getallapps2();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getallapps2() throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
        String name = null;
        String packageAppName = null;
        Drawable iconVar = null;
        int i = 0;

        String[] apps = new String[ril.size()];
        List<AppsMd> listaApp = new ArrayList<>();
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);

                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);
                    iconVar = ri.activityInfo.loadIcon(getPackageManager());
                    packageAppName = ri.activityInfo.packageName;

                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                    iconVar = ri.activityInfo.loadIcon(getPackageManager());
                    packageAppName = ri.activityInfo.packageName;
                }

                AppsMd ap = new AppsMd(name,packageAppName,iconVar );
                listaApp.add(ap);

                apps[i] = name;
                i++;
            }
        }

        app_instalados.setLayoutManager(new LinearLayoutManager(ListApp.this));
        app_instalados.setAdapter(new ListAdapterApps(ListApp.this, listaApp));

    }

}