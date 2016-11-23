package zjj.app.mobilesecurity.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class ServiceUtils {

    public static Intent startService(Context context, Class<? extends Service> cls){
        Intent serviceIntent = new Intent(context, cls);
        context.startService(serviceIntent);

        return serviceIntent;
    }

    /**
     * 检查该服务是否已经开启，如果是则停止服务
     */
    public static void stopService(Context context, Intent service, Class<? extends Service> cls){
        if(isServiceRunning(context,cls)){
            context.stopService(service);
        }
    }


    public static boolean isServiceRunning(Context context, Class<? extends Service> cls) {
        String serviceClsName = cls.getName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo service : services) {
            String clsName = service.service.getClassName();
            if (serviceClsName.equals(clsName)) {
                return true;
            }
        }
        return false;
    }

    public static void startMultiServices(Context context, Class<? extends Service>... classes) {
        for(Class cls: classes){
            boolean isRunning = isServiceRunning(context, cls);
            if(!isRunning){
                startService(context, cls);
            }
        }
    }
}
