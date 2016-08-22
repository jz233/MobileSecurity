package zjj.app.mobilesecurity.domain;

import android.database.Cursor;

/**
 * Created by DouJ on 29/07/2016.
 */
public class SmsLogNumber {

    private int id;
    private String address;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "SmsLogNumber{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public static SmsLogNumber fromCursor(Cursor cursor){

        SmsLogNumber log = new SmsLogNumber();
        log.setId(cursor.getInt(0));
        log.setAddress(cursor.getString(1));
        log.setBody(cursor.getString(2));

        return log;
    }


}
