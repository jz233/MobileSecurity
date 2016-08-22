package zjj.app.mobilesecurity.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.db.AppLockDBHelper;
import zjj.app.mobilesecurity.domain.AppLockInfo;
import zjj.app.mobilesecurity.utils.Constants;

public class AppLockDao {

    private AppLockDBHelper helper;
    private Context context;

    public AppLockDao(Context context) {
        this.context = context;
        helper = new AppLockDBHelper(context);
    }

    public List<String> getAll(){
        List<String> list = new ArrayList<>();
        AppLockInfo info;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_APP_LOCK, new String[]{"packagename"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public int getCount(){
        int count = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_APP_LOCK, null, null, null, null, null, null);
        if(cursor != null){
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }

    public void addLockedApp(String pkgName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", pkgName);
        values.put("timestamp", System.currentTimeMillis());
        db.insert(Constants.TABLE_APP_LOCK, null, values);
        db.close();

        //提示内容观察者数据库已变化
        context.getContentResolver().notifyChange(Uri.parse(Constants.URI_LOCKEDAPPCHANGE),null);
    }

    public void removeUnlockedApp(String pkgName){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(Constants.TABLE_APP_LOCK, "packagename=?", new String[]{pkgName});
        db.close();

        //提示内容观察者数据库已变化
        context.getContentResolver().notifyChange(Uri.parse(Constants.URI_LOCKEDAPPCHANGE),null);
    }

    public boolean isAppLocked(String pkgName){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_APP_LOCK, null, "packagename=?", new String[]{pkgName}, null, null, null);
        if(cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();

        return result;
    }

    public long getTimeByPkgName(String pkgName){
        long result = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_APP_LOCK, new String[]{"timestamp"}, "packagename=?", new String[]{pkgName}, null, null, null);

        if(cursor.moveToNext()){
            result = cursor.getLong(0);
        }
        cursor.close();
        db.close();

        return result;
    }

    public void updateTimeStampByPkgName(String pkgName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp", System.currentTimeMillis());
        db.update(Constants.TABLE_APP_LOCK, values, "packagename=?", new String[]{pkgName});
        db.close();

    }



}
