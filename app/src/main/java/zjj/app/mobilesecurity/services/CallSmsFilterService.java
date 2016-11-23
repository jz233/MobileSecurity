package zjj.app.mobilesecurity.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import zjj.app.mobilesecurity.dao.BlackListDao;
import zjj.app.mobilesecurity.receivers.SmsFilterReceiver;

public class CallSmsFilterService extends Service {

    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private BlackListDao dao;
    private CallLogObserver observer;
    private SmsFilterReceiver receiver;
    private IntentFilter filter;

    public CallSmsFilterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        dao = new BlackListDao(getApplicationContext());
        //监听电话
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //监听短信(广播)
        receiver = new SmsFilterReceiver(dao);
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);  //-1000 - 1000, 1000即可
        registerReceiver(receiver, filter);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregister listener
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (dao.hasNumber(incomingNumber)) {
                        //先注册删除通话记录的内容观察者
                        deleteCallLog(incomingNumber);
                        //挂断电话
                        terminateCall();

                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;

            }
        }
    }


    private void terminateCall() {
        try {
            //反射挂电话
            Class cls = getClassLoader().loadClass("android.os.ServiceManager");
            Method method = cls.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void deleteCallLog(String incomingNumber) {
        observer = new CallLogObserver(new Handler(), incomingNumber);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, observer);

    }

    private class CallLogObserver extends ContentObserver {

        private String incomingNumber;

        public CallLogObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //已发现通话记录的改变，可以注销观察者
            getContentResolver().unregisterContentObserver(this);
            //子线程删除通话记录
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getContentResolver().delete(CallLog.Calls.CONTENT_URI, "number = ?", new String[]{incomingNumber});
                }
            }.start();
        }

    }

}
