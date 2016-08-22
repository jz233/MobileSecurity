package zjj.app.mobilesecurity.parsers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.domain.ContactInfo;

/**
 * Created by DouJ on 4/08/2016.
 */
public class ContactParser {

    public static List<ContactInfo> getAllContacts(ContentResolver cr){

        ContactInfo info = null;
        List<ContactInfo> list = new ArrayList<>();

        Cursor c = cr.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
        Cursor cursor = null;
        while(c.moveToNext()){
            String contact_id = c.getString(0);
            cursor = cr.query(Uri.parse("content://com.android.contacts/data"), new String[]{"mimetype", "data1"}, "contact_id=?", new String[]{contact_id}, null);
            String number = null;
            String name = null;
            while(cursor.moveToNext()){
                String mimetype = cursor.getString(0);
                String data1 = cursor.getString(1);
                if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                    number = data1;
                }else if("vnd.android.cursor.item/name".equals(mimetype)){
                    name = data1;
                }
            }
            if(!TextUtils.isEmpty(number)){
                info = new ContactInfo();
                info.setName(name);;
                info.setNumber(number);

                list.add(info);
            }
            cursor.close();
        }
        c.close();

        return list;
    }

}
