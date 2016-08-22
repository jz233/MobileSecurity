package zjj.app.mobilesecurity.parsers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.BuildConfig;
import zjj.app.mobilesecurity.domain.ApkInfo;

public class ApkInfoParser {

    public static List<ApkInfo> getApkInfos(Context context){
        PackageManager pm = context.getPackageManager();

        List<PackageInfo> infos = pm.getInstalledPackages(0);
        List<ApkInfo> apkInfoList = new ArrayList<>();
        ApkInfo apk;
        String pkgName = null;
        for(PackageInfo info : infos){
            apk = new ApkInfo();

            try {
                pkgName =  info.packageName;
                ApplicationInfo appInfo = pm.getApplicationInfo(pkgName,0);
                //如果是系统应用(的apk),跳过不添加到列表
                if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    continue;
                }
                String appName = info.applicationInfo.loadLabel(pm).toString();
                String versionName = info.versionName;
                Drawable appIcon = info.applicationInfo.loadIcon(pm);

                String sourceDir = appInfo.sourceDir;

                apk.setPkgName(pkgName);
                apk.setAppName(appName);
                apk.setAppIcon(appIcon);
                apk.setVersionName(versionName);
                apk.setSourceDir(sourceDir);

                if (BuildConfig.DEBUG) Log.d("ApkInfoParser", apk.toString());


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                if (BuildConfig.DEBUG) Log.d("ApkInfoParser", pkgName + "not installed");

                continue;
            }

            apkInfoList.add(apk);
        }

        return apkInfoList;
    }


}
