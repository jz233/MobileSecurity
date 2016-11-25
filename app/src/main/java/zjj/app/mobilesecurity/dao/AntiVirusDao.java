package zjj.app.mobilesecurity.dao;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AntiVirusDao {

    private SQLiteDatabase db;

    public AntiVirusDao(Context context) {
        db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath() + "/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
    }

    public String virusScan(String md5) {
        String description = null;
        Cursor cursor = db.query("datable", new String[]{"desc"}, "md5 = ?", new String[]{md5}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
             description = cursor.getString(0);
        }
        cursor.close();

        return description;
    }

    public void closeDB() {
        db.close();
    }
}
