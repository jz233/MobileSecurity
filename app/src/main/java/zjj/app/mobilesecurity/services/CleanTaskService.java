package zjj.app.mobilesecurity.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

import zjj.app.mobilesecurity.dao.WhiteListDao;
import zjj.app.mobilesecurity.domain.TaskInfo;
import zjj.app.mobilesecurity.utils.SystemUtils;

public class CleanTaskService extends Service {

    private LockScreenReceiver receiver;
    private IntentFilter filter;
    private WhiteListDao dao;

    public CleanTaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new WhiteListDao(getApplicationContext());

        receiver = new LockScreenReceiver();
        filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);

    }

    private class LockScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)){
                List<String> whiteList = dao.getAll();
                List<String> tasks = SystemUtils.getRunningTaskNames(getApplicationContext());
                //过滤掉白名单中的包名
                tasks.removeAll(whiteList);

                SystemUtils.killBackgroundProcessList(getApplicationContext(),tasks);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }
}
