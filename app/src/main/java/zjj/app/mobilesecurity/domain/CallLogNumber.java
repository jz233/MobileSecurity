package zjj.app.mobilesecurity.domain;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DouJ on 20/07/2016.
 */
public class CallLogNumber {

    private int id;
    private String number;
    private String name;
    private int type;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CallLogNumber{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o != null && o instanceof CallLogNumber) {
            isEqual = this.number.equals(((CallLogNumber) o).getNumber());
        }
        return isEqual;
    }

    public static CallLogNumber fromCursor(Cursor cursor) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        CallLogNumber number = new CallLogNumber();
        number.setId(cursor.getInt(0));
        number.setNumber(cursor.getString(1));
        number.setName(cursor.getString(2));
        number.setType(cursor.getInt(3));
        number.setDate(format.format(new Date(Long.parseLong(cursor.getString(4)))));

        return number;
    }
}
