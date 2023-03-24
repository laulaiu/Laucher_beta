package com.bojack.myapplication.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class SharedPrefInfo {



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
