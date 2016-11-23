package zjj.app.mobilesecurity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import zjj.app.mobilesecurity.services.AppLockService;
import zjj.app.mobilesecurity.services.CallSmsFilterService;
import zjj.app.mobilesecurity.services.CleanTaskService;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.ServiceUtils;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

/**
 * 管理开机自启动项
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootCompleteReceiver", "Boot completed");
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            //黑名单拦截服务
            ServiceUtils.startService(context, CallSmsFilterService.class);

            //应用锁服务
            ServiceUtils.startService(context, AppLockService.class);

            //内存清理白名单服务(根据设置项)
            boolean enabled = SharedPreferencesUtils.getBoolean(context, Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF, false);
            //手机刚启动，无需判断是否停止服务
            if (enabled) {
                ServiceUtils.startService(context, CleanTaskService.class);
            }
        }
    }
}
