package com.bojack.tv64.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefInfo {



    public List<AppsMd> getListApp(Context context){

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        List<AppsMd> listaApp = new ArrayList<>();

        for (ResolveInfo ri : ril) {
            AppsMd ap = new SharedPrefInfo().getApp(context, ri);
            listaApp.add(ap);
        }

        return listaApp;
    }

    public AppsMd getApp(Context context, ResolveInfo ri){

        String name = null;
        String packageAppName = null;
        Drawable iconVar = null;
        AppsMd ap = null;

        if (ri.activityInfo != null) {
            Resources res = null;
            try {
                res = context.getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (ri.activityInfo.labelRes != 0) {
                name = res.getString(ri.activityInfo.labelRes);
                iconVar = ri.activityInfo.loadIcon(context.getPackageManager());
                packageAppName = ri.activityInfo.packageName;

            } else {
                name = ri.activityInfo.applicationInfo.loadLabel(
                        context.getPackageManager()).toString();
                iconVar = ri.activityInfo.loadIcon(context.getPackageManager());
                packageAppName = ri.activityInfo.packageName;
            }

            ap = new AppsMd(name,packageAppName,iconVar );
        }
        return ap;
    }
}
