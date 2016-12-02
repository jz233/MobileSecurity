package zjj.app.mobilesecurity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;

import zjj.app.mobilesecurity.dao.BlackListDao;

public class SmsFilterReceiver extends BroadcastReceiver {
    private BlackListDao dao;

    public SmsFilterReceiver() {

    }
    public SmsFilterReceiver(BlackListDao dao) {
        this.dao = dao;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object obj : objs){
            SmsMessage msg = null;
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                 msg = SmsMessage.createFromPdu((byte[]) obj);
            }else{
                msg = SmsMessage.createFromPdu((byte[]) obj, "3gpp");
            }
            String address = msg.getOriginatingAddress();
            String body = msg.getMessageBody();
            Log.d("SmsFilterReceiver", address);
            Log.d("SmsFilterReceiver", body);

            if(dao.hasNumber(address)){
                //停止广播，手机便不会再接收到这条短信
                abortBroadcast();
            }
        }
    }
}
