package zjj.app.mobilesecurity.utils;

import android.database.Cursor;
import android.util.Log;

import zjj.app.mobilesecurity.BuildConfig;

/**
 * Created by DouJ on 30/07/2016.
 */
public class CursorUtils {

    public static void printCursor(Cursor cursor) {
        if (cursor == null) {
            Log.d("CursorUtils", "NULL");
            return;
        }


        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);

                Log.d("CursorUtils", columnName + " : " + columnValue);
            }
        }
        Log.d("CursorUtils", "===========================================");

    }
}
