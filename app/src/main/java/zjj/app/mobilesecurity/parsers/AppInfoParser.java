package zjj.app.mobilesecurity.parsers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zjj.app.mobilesecurity.domain.AppInfo;

public class AppInfoParser {

    private AppInfo appInfo;
    private PackageManager pm;
    private List<AppInfo> appInfoList;

    public List<AppInfo> getAllAppsInfo(Context context){
        pm = context.getPackageManager();
        appInfoList = new ArrayList<>();
        List<PackageInfo> infos = pm.getInstalledPackages(0);

        for(PackageInfo info: infos){
            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                appInfo = new AppInfo();

                appInfo.pkgName = info.packageName;
                appInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                appInfo.appIcon = info.applicationInfo.loadIcon(pm);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                appInfo.installedTime = format.format(new Date(info.firstInstallTime));
                appInfo.versionName = info.versionName;

                try {
                    Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                    method.invoke(pm, info.packageName, new MyPackageObserver(context, appInfo));

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                appInfoList.add(appInfo);
            }
        }

        return appInfoList;
    }

    private class MyPackageObserver extends IPackageStatsObserver.Stub{
        private AppInfo appInfo;
        private Context context;

        MyPackageObserver(Context context, AppInfo appInfo){
            this.context = context;
            this.appInfo = appInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            appInfo.appSize = Formatter.formatFileSize(context, pStats.codeSize);
        }
    }


    public List<AppInfo> getMovableAppList(Context context){
        pm = context.getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        appInfoList = new ArrayList<>();

        for(PackageInfo info: infos){
            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                AppInfo appInfo = new AppInfo();
                if(info.installLocation == PackageInfo.INSTALL_LOCATION_AUTO){
                    appInfo.pkgName = info.packageName;
                    appInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    appInfo.appIcon = info.applicationInfo.loadIcon(pm);

                    try {
                        Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        method.invoke(pm, info.packageName, new MyPackageObserver(context, appInfo));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    appInfoList.add(appInfo);

                }
            }

        }
        return appInfoList;
    }
}
