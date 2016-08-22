package zjj.app.mobilesecurity.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import zjj.app.mobilesecurity.db.BlackListDBHelper;
import zjj.app.mobilesecurity.domain.BlackListNumber;

public class BlackListDao {

    private BlackListDBHelper helper;
    private Context context;

    public BlackListDao(Context context) {
        this.helper = new BlackListDBHelper(context);
        this.context = context;
    }

    public boolean addNumber(String number, String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        HashSet<String> numbers = getAllNumbers();
        if(!numbers.contains(number)){
            ContentValues values = new ContentValues();
            values.put("number", number);
            values.put("name", name);
            long rowId = db.insert("blacklistinfo", null, values);

            db.close();
            return (rowId != -1);
        }else{
            db.close();
            return false;
        }
    }

    public boolean deleteByNumber(String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNum = db.delete("blacklistinfo", "number = ?", new String[]{number});
        db.close();

        return rowNum > 0;
    }
    public boolean deleteById(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNum = db.delete("blacklistinfo", "_id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowNum > 0;
    }


   /* public List<BlackListNumber> getAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacklistinfo", new String[]{"_id", "number", "name"}, null, null, null, null, null);
        ArrayList<BlackListNumber> numbers = new ArrayList<>();
        while(cursor.moveToNext()){
            BlackListNumber number = new BlackListNumber();
            number.setId(cursor.getInt(0));
            number.setNumber(cursor.getString(1));
            number.setName(cursor.getString(2));

            numbers.add(number);
        }
        cursor.close();
        db.close();

        return numbers;
    }*/
    public Cursor getAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacklistinfo", new String[]{"_id", "number", "name"}, null, null, null, null, null);

        return cursor;
    }

    public HashSet<String> getAllNumbers(){
        SQLiteDatabase db = helper.getReadableDatabase();
        HashSet<String> hs = new HashSet();
        Cursor cursor = db.query("blacklistinfo", new String[]{"number"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            String number = cursor.getString(0);
            hs.add(number);
        }
        return hs;
    }

    public String getNameByNumber(String number){
        String name = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        Cursor cursor = cr.query(uri, new String[]{"display_name"}, null, null, null);
        if(cursor != null && cursor.moveToNext()){
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public boolean hasNumber(String incomingNumber){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacklistinfo", null, "number = ?", new String[]{incomingNumber}, null, null, null);

        if(cursor.moveToNext()){
            result = true;
        }
        db.close();
        cursor.close();

        return result;
    }

}
