/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.R;

import java.util.List;

public class RutokenServicePackageHelper {

    static public boolean isInstalledRutokenService(@NonNull final Context context) {
        return isInstalled(context, context.getString(R.string.rutoken_service_package));
    }

    static public boolean isInstalled(@NonNull final Context context, @NonNull final String packageName) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfoList = pm.getInstalledApplications(pm.GET_META_DATA);
        boolean isAppInstalled = false;
        for (ApplicationInfo appInfo : appInfoList) {
            if (packageName.equals(appInfo.packageName)) {
                isAppInstalled = true;
                break;
            }
        }
        return isAppInstalled;
    }

    static public void installRutokenService(@NonNull final Context context) {
        final String appPackageName = context.getString(R.string.rutoken_service_package);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
