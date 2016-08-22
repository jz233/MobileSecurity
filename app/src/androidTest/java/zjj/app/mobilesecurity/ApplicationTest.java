package zjj.app.mobilesecurity;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import zjj.app.mobilesecurity.db.BlackListDBHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private Context context;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
    }


    public void testRebuildDB() throws Exception{
        BlackListDBHelper helper = new BlackListDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("drop table if exists blacklistinfo");
        db.execSQL("create table blacklistinfo (_id integer primary key autoincrement, number varchar(32), name varchar(32))");

        assertEquals(true, true);
    }


}