package zjj.app.mobilesecurity.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.dao.BlackListDao;

public class BlackListAdapter extends CursorAdapter {
    private ViewHolder holder;
    private BlackListDao dao;
    private OnImageClickListener listener;

    public BlackListAdapter(Context context, Cursor c, OnImageClickListener listener) {
        super(context, c);
        dao = new BlackListDao(context);
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_black_list, null);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        final int id = cursor.getInt(0);
        final String number = cursor.getString(1);
        String name = cursor.getString(2);

        holder = (ViewHolder) view.getTag();
        if(holder == null){
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        //初始化时就要把位置设置好
        final int position = cursor.getPosition();
        if(!TextUtils.isEmpty(name)){
            holder.tv_number_or_name.setText(name + "(" + number + ")");
        }else{
            holder.tv_number_or_name.setText(number);
        }
        holder.iv_remove_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = dao.deleteByNumber(number);
                if(result){
                    listener.onImageClicked();
                }
            }
        });
    }

    static class ViewHolder{
        private TextView tv_number_or_name;
        private ImageView iv_remove_number;

        public ViewHolder(View view) {
            tv_number_or_name = (TextView) view.findViewById(R.id.tv_number_or_name);
            iv_remove_number = (ImageView) view.findViewById(R.id.iv_remove_number);
        }
    }

    public interface OnImageClickListener{
        void onImageClicked();
    }
}
