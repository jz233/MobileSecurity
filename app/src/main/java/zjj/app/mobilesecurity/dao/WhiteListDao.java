package zjj.app.mobilesecurity.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.db.WhiteListDBHelper;
import zjj.app.mobilesecurity.utils.Constants;

public class WhiteListDao {

    private WhiteListDBHelper helper;

    public WhiteListDao(Context context){
        helper = new WhiteListDBHelper(context);
    }

    public void add(String pkgName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", pkgName);
        db.insert(Constants.TABLE_WHITE_LIST, null, values);

        db.close();
    }

    public int delete(String pkgName){
        int rows = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        rows = db.delete(Constants.TABLE_WHITE_LIST, "packagename=?", new String[]{pkgName});

        db.close();

        return rows;
    }

    public List<String> getAll(){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_WHITE_LIST, new String[]{"packagename"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        return list;
    }

    public boolean hasApp(String pkgName){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_WHITE_LIST, null, "packagename=?", new String[]{pkgName}, null, null, null);
        if(cursor != null && cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();

        return result;
    }


}
