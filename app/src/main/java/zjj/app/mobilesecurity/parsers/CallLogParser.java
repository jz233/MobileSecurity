package zjj.app.mobilesecurity.parsers;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zjj.app.mobilesecurity.domain.CallLogNumber;

public class CallLogParser {

    public static List<CallLogNumber> getCallLog(Context context) {
        long start = System.currentTimeMillis();

        List<CallLogNumber> numbers = new ArrayList<>();
        CallLogNumber number;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");;

        ContentResolver cr = context.getContentResolver();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE, CallLog.Calls.DATE}, null, null, CallLog.Calls.DATE + " DESC");

        while(cursor.moveToNext()){
            number =  new CallLogNumber();
            number.setNumber(cursor.getString(0));

            if(!numbers.contains(number)){
                number.setName(cursor.getString(1));
                number.setType(cursor.getInt(2));
                long dateInMillis = cursor.getLong(3);
                number.setDate(format.format(new Date(dateInMillis)));

                numbers.add(number);
            }
            //---  else continue;
        }
        cursor.close();

        long end = System.currentTimeMillis();
        long time = end - start;
        if(time < 1000){
            try {
                Thread.sleep(1000-time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return numbers;
    }

}
