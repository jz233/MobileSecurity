package zjj.app.mobilesecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zjj.app.mobilesecurity.utils.Constants;

public class BlackListDBHelper extends SQLiteOpenHelper{

    public BlackListDBHelper(Context context) {
        super(context, "blacklist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Constants.TABLE_BLACK_LIST + " (_id integer primary key autoincrement, number varchar(32), name varchar(32))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
