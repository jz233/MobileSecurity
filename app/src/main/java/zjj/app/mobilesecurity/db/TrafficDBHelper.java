package zjj.app.mobilesecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zjj.app.mobilesecurity.utils.Constants;

public class TrafficDBHelper extends SQLiteOpenHelper {

    public TrafficDBHelper(Context context) {
        super(context, "traffic.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Constants.TABLE_TRAFFIC + " (_id integer primary key autoincrement, packagename varchar (48), snd integer, rcv integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
