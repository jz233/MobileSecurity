package zjj.app.mobilesecurity.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.db.TrafficDBHelper;
import zjj.app.mobilesecurity.domain.TrafficInfo;
import zjj.app.mobilesecurity.utils.Constants;

/**
 * Created by DouJ on 15/08/2016.
 */
public class TrafficDao {

    private TrafficDBHelper helper;
    private Context context;

    public TrafficDao(Context context){
        helper = new TrafficDBHelper(context);
        this.context = context;
    }

    public void insertOrUpdateStats(String pkgName, long snd, long rcv){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c1 = db.query(Constants.TABLE_TRAFFIC, new String[]{"packagename", "snd", "rcv"}, "packagename=?", new String[]{pkgName}, null, null, null);
        if(c1 != null && c1.getCount()>=0){
            //数据库中有此包名的流量记录, 执行更新记录
            c1.moveToNext();
            long oldSnd = c1.getLong(1);
            long oldRcv = c1.getLong(2);
            ContentValues values = new ContentValues();
            values.put("snd", oldSnd + snd);
            values.put("rcv", oldRcv + rcv);
            db.update(Constants.TABLE_TRAFFIC, values, "packagename=?", new String[]{pkgName});

            c1.close();

        }else{
            ContentValues values = new ContentValues();
            values.put("packagename", pkgName);
            values.put("snd", snd);
            values.put("rcv", rcv);
            db.insert(Constants.TABLE_TRAFFIC, null, values);
        }

        db.close();
    }

    public List<TrafficInfo> getAllStats(){
        List<TrafficInfo> infos = new ArrayList<>();
        TrafficInfo info;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_TRAFFIC, new String[]{"packagename", "snd", "rcv"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            info = new TrafficInfo();
            info.setPkgName(cursor.getString(0));
            info.setSnd(cursor.getLong(1));
            info.setRcv(cursor.getLong(2));

            infos.add(info);
        }
        cursor.close();
        db.close();

        return infos;
    }
}
