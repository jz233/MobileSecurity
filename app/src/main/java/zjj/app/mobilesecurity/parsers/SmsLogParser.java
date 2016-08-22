package zjj.app.mobilesecurity.parsers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import zjj.app.mobilesecurity.BuildConfig;

public class SmsLogParser {

    public static void getSmsLog(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://sms/conversations"), new String[]{"thread_id", "snippet"}, null, null, "date DESC");

//   select sms.body AS snippet, sms.thread_id AS thread_id, groups.msg_count AS msg_count from sms,
//   (SELECT thread_id AS group_thread_id, MAX(date)AS group_date,COUNT(*) AS msg_count FROM sms GROUP BY thread_id) AS groups
//   where sms.thread_id = groups.group_thread_id AND sms.date = groups.group_date;


        while (cursor.moveToNext()){
            String msgBody = cursor.getString(1);
            if (BuildConfig.DEBUG) Log.d("SmsLogParser", msgBody);
        }

        cursor.close();
    }

}
