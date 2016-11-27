package zjj.app.mobilesecurity.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import zjj.app.mobilesecurity.BuildConfig;
import zjj.app.mobilesecurity.domain.AppInfo;
import zjj.app.mobilesecurity.domain.TaskInfo;

public class SystemUtils {

    public static List<TaskInfo> getRunningTasks(Context context) {
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos;
        List<TaskInfo> tasks = new ArrayList<>();
        TaskInfo task;

        //API<21 && API>=21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            infos = am.getRunningAppProcesses();
        } else {
            infos = AndroidProcesses.getRunningAppProcessInfo(context);
        }

        for (ActivityManager.RunningAppProcessInfo info : infos) {
            String pkgName = info.processName;

            if (pkgName.startsWith("com.android")) {
                continue;
            }
            task = new TaskInfo();
            try {
                PackageInfo pInfos = pm.getPackageInfo(pkgName, 0);
                String appName = pInfos.applicationInfo.loadLabel(pm).toString();
                if (BuildConfig.DEBUG) Log.d("SystemUtils", appName);
                Drawable appIcon = pInfos.applicationInfo.loadIcon(pm);
                boolean isUserApp = (pInfos.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;

             /*   boolean isUserApp = (pInfos.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
                if(isUserApp){
                    continue;
                }*/
                Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{info.pid});
                int memSize = memoryInfos[0].getTotalPrivateDirty();

                task.setPkgName(pkgName);
                task.setAppName(appName);
                task.setAppIcon(appIcon);
                task.setUsrApp(isUserApp);
                task.setMemSize(memSize);

            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
                continue;
            }

            tasks.add(task);
        }
        return tasks;

    }

    public static List<String> getRunningTaskNames(Context context) {
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos;
        List<String> tasks = new ArrayList<>();

        //API<21 && API>=21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            infos = am.getRunningAppProcesses();
        } else {
            infos = AndroidProcesses.getRunningAppProcessInfo(context);
        }

        for (ActivityManager.RunningAppProcessInfo info : infos) {
            String pkgName = info.processName;
            if (pkgName.startsWith("com.android")) {
                continue;
            }
            tasks.add(pkgName);
        }
        return tasks;

    }

    public static String getTopPackageName(Context context){
        String pkgName = null;
        long endTime;
        long startTime;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

            ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
            //得到最前端的activity所属的包名
            pkgName = taskInfo.topActivity.getPackageName();
//            Log.d("AppLockService", pkgName);

        }else{
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            endTime = calendar.getTimeInMillis();
            //向前数一个月
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            startTime =  calendar.getTimeInMillis();

            long time = System.currentTimeMillis();
            List<UsageStats> statses = usm.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, startTime, endTime);
            if(statses != null){
                TreeMap<Long, UsageStats> map = new TreeMap<>();
                for(UsageStats stats : statses){
                    map.put(stats.getLastTimeUsed(), stats);
                }
                if(map != null && !map.isEmpty()){
                    pkgName = map.get(map.lastKey()).getPackageName();
//                    Log.d("AppLockService", pkgName);
                }
            }
        }

        return pkgName;
    }

    public static List<AppInfo> getInstalledPackagesInfo(Context context){
        List<AppInfo> list = new ArrayList<>();
        AppInfo appInfo;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for(PackageInfo info : infos){
            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                appInfo = new AppInfo();
                appInfo.pkgName = info.packageName;
                appInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                appInfo.appIcon = info.applicationInfo.loadIcon(pm);

                list.add(appInfo);
            }else{
                continue;
            }
        }
        return list;
    }

    public static List<Drawable> getInstalledAppIcons(Context context) {
        List<Drawable> appIcons = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (PackageInfo info : infos) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appIcons.add(info.applicationInfo.loadIcon(pm));
            }
        }
        return appIcons;
    }

    public static void killBackgroundProcessList(Context context, List<String> list){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(String pkgName : list){
            am.killBackgroundProcesses(pkgName);
        }
    }



}
