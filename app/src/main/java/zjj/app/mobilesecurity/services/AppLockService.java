package zjj.app.mobilesecurity.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.BuildConfig;
import zjj.app.mobilesecurity.activities.applock.AppLockConfirmPattern2Activity;
import zjj.app.mobilesecurity.dao.AppLockDao;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;
import zjj.app.mobilesecurity.utils.SystemUtils;

public class AppLockService extends Service {

    private List<String> lockedList;
    private List<String> whiteList;
    private AppLockDao dao;
    private ActivityManager am;
    private List<ActivityManager.RunningTaskInfo> tasks;
    private ActivityManager.RunningTaskInfo taskInfo;
    private String pkgName;
    private Intent confirmPatternIntent;
    private IntentFilter filter;
    private LockScreenReceiver receiver;
    private boolean flag;
    private String lastUsedPkgName;
    private ContentObserver observer;
    private ContentResolver cr;

    public AppLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        dao = new AppLockDao(getApplicationContext());

        lockedList = dao.getAll();
        whiteList = new ArrayList<>();

        startListeningApps();

        //注册广播接收者, 捕获锁屏事件 和 密码验证成功事件
        filter = new IntentFilter(Constants.PATTERN_CORRECT);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        receiver = new LockScreenReceiver();
        registerReceiver(receiver, filter);

        //内容观察者监听程序锁数据库变化,及时重新查询程序列表
        //注意在操作数据库的位置使用notifyChange方法提醒Observer数据库内容已改变
        observer = new AppLockObserver(new Handler());
        cr = getContentResolver();
        cr.registerContentObserver(Uri.parse(Constants.URI_LOCKEDAPPCHANGE), false, observer);

    }

    private class AppLockObserver extends ContentObserver{

        public AppLockObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            lockedList = dao.getAll();
            if (BuildConfig.DEBUG)
                Log.d("AppLockService", "ApppLock list changed. total -- " + dao.getCount());
        }
    }

    private void startListeningApps() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                flag = true;
                //无限循环，监听手机所有启动的进程
                while (flag){
                    String pkgName = SystemUtils.getTopPackageName(getApplicationContext());

                    if(pkgName != null && lockedList.contains(pkgName)){
                        if(!whiteList.contains(pkgName)){
                            startPatternCheckActivity(pkgName);
                        }else{

                            //如果设置了自动锁定时间，则获取时间
                            int relock_time = SharedPreferencesUtils.getInt(getApplicationContext(), Constants.PREF_KEY_RELOCK_TIME, 0);
                            long time = dao.getTimeByPkgName(pkgName);
                            //超过了设置的重锁时间，程序重新加上密码锁
                            if((relock_time != 0) && ((System.currentTimeMillis() - time) > relock_time*1000)){
                                //超过重锁时间，并且该进程不是一直保持在最前端的(刚刚启动或者从别的进程切换来的)
                                if(!pkgName.equals(lastUsedPkgName)){
                                    whiteList.remove(pkgName);
                                    startPatternCheckActivity(pkgName);
                                }
                            }
                        }
                    }
                    //记录上一个时间点，最前端进程的包名
                    setLastUsedPkgName(pkgName);

                    SystemClock.sleep(200);

                }
            }
        }.start();

    }

    /**
     * 启动密码验证界面
     * @param pkgName
     */
    private void startPatternCheckActivity(String pkgName) {
        confirmPatternIntent = new Intent(this, AppLockConfirmPattern2Activity.class);
        confirmPatternIntent.putExtra("packagename", pkgName);
        confirmPatternIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        confirmPatternIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //服务中无法使用startActivityForResult();
        startActivity(confirmPatternIntent);
    }

    private class LockScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Constants.PATTERN_CORRECT.equals(action)){
                String pkgName = intent.getStringExtra("packagename");

                //通过密码验证的包名加入临时白名单
                whiteList.add(pkgName);
                //更新时间戳
                dao.updateTimeStampByPkgName(pkgName);
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                //读取sp里存储的设置信息
                boolean enabled = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.PREF_KEY_RELOCK_SCREEN_OFF, false);
                if(enabled){
                    //锁屏后清空白名单, 停止监听最前端进程
                    whiteList.clear();
                }
                flag = false;

            }else if(Intent.ACTION_SCREEN_ON.equals(action)){
                //确保只启动一次监听
                if(!flag){
                    //解锁后重新开启监听
                    startListeningApps();
                }
            }

        }
    }

    /**
     * 记录上个时间点，最前端进程的包名
     * @param pkgName
     */
    private void setLastUsedPkgName(String pkgName) {
        lastUsedPkgName = pkgName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        flag = false;
        unregisterReceiver(receiver);
        receiver = null;

        cr.unregisterContentObserver(observer);
        observer = null;

    }
}
