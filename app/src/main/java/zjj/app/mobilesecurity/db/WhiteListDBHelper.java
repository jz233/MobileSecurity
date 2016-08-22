package zjj.app.mobilesecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zjj.app.mobilesecurity.utils.Constants;

/**
 * Created by DouJ on 19/08/2016.
 */
public class WhiteListDBHelper extends SQLiteOpenHelper {

    public WhiteListDBHelper(Context context) {
        super(context, "whitelist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Constants.TABLE_WHITE_LIST + " (_id integer primary key autoincrement, packagename varchar (48))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
