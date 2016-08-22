package zjj.app.mobilesecurity.handlers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import zjj.app.mobilesecurity.utils.CursorUtils;

public class SmsLogQueryHandler extends AsyncQueryHandler {

    private OnLogQueryCompleteListener listener;

    public SmsLogQueryHandler(ContentResolver cr, OnLogQueryCompleteListener listener) {
        super(cr);
        this.listener = listener;
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);

//        CursorUtils.printCursor(cursor);
        listener.onLogQueryComplete(cursor.getCount());

        if(cookie != null && cookie instanceof CursorAdapter){
            ((CursorAdapter) cookie).changeCursor(cursor);
        }

    }

    public interface OnLogQueryCompleteListener{
        void onLogQueryComplete(int count);
    }

}
