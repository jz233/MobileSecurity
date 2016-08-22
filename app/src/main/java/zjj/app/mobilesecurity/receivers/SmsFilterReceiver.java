package zjj.app.mobilesecurity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import zjj.app.mobilesecurity.dao.BlackListDao;

public class SmsFilterReceiver extends BroadcastReceiver {
    private BlackListDao dao;
    public SmsFilterReceiver(BlackListDao dao) {
        this.dao = dao;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object obj : objs){
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) obj);
            String address = msg.getOriginatingAddress();
//            String body = msg.getMessageBody();

            if(dao.hasNumber(address)){
                //停止广播，手机便不会再接收到这条短信
                abortBroadcast();
            }
        }
    }
}
