package zjj.app.mobilesecurity.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.callsmsfilter.BlackListActivity;
import zjj.app.mobilesecurity.base.BaseLogFragment;
import zjj.app.mobilesecurity.domain.SmsLogNumber;
import zjj.app.mobilesecurity.handlers.SmsLogQueryHandler;
import zjj.app.mobilesecurity.utils.Constants;

public class SmsLogFragment extends BaseLogFragment {

    private SmsLogQueryHandler handler;
    private SmsLogAdapter adapter;
    private SparseBooleanArray checkStates;
    private int listSize;

    public SmsLogFragment() {
    }

    public static SmsLogFragment newInstance() {
        SmsLogFragment fragment = new SmsLogFragment();
        return fragment;
    }



    @Override
    public void setupListListener() {
        lv_log.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.cb_selected.toggle();

            }
        });
    }

    @Override
    public void setupListData() {
        adapter = new SmsLogAdapter(context, null);
        lv_log.setAdapter(adapter);

        handler = new SmsLogQueryHandler(context.getContentResolver(), new SmsLogQueryHandler.OnLogQueryCompleteListener() {
            @Override
            public void onLogQueryComplete(int count) {
                loading.setVisibility(View.INVISIBLE);
                listSize = count;
            }
        });
        String[] projection = {"sms.thread_id AS _id", "address AS address", "sms.body AS body",  "date AS date"};
        handler.startQuery(0, adapter, Uri.parse(Constants.URI_CONVERSATIONS), projection, null, null, "date DESC");
    }

    @Override
    public void addNumberToBlackList() {
        for(int i=0; i<listSize; i++){
            if(checkStates.get(i, false)){
                Log.d("SmsLogFragment", adapter.getItem(i).toString());
                String number = adapter.getItem(i);
                String name = dao.getNameByNumber(number);
                dao.addNumber(number, name);
            }
        }
        //关闭fragment时更新activity黑名单列表
        FragmentActivity activity = getActivity();
        if(activity instanceof BlackListActivity){
            ((BlackListActivity)activity).initData();
        }


    }

    class SmsLogAdapter extends CursorAdapter {

        private ViewHolder holder;

        public SmsLogAdapter(Context context, Cursor c) {
            super(context, c);
            checkStates = new SparseBooleanArray();
        }

        @Override
        public String getItem(int position) {
            Cursor cursor = getCursor();
            SmsLogNumber number;
            if(cursor.moveToPosition(position)){
                number = SmsLogNumber.fromCursor(cursor);
                return number.getAddress();
            }

            return null;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_sms_log, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            //该cursor中没有查询name
            SmsLogNumber number = SmsLogNumber.fromCursor(cursor);

            final int position = cursor.getPosition();

            holder = (ViewHolder) view.getTag();
            if(holder == null){
                holder = new ViewHolder(view);
                view.setTag(holder);
            }

            String address = number.getAddress();
            //初始查询中name均为null, 要逐个查询名字
            String name = dao.getNameByNumber(address);
            if(!TextUtils.isEmpty(name)){

                holder.tv_phone_number_or_name.setText(name);
            }else{
                holder.tv_phone_number_or_name.setText(address);
            }

            holder.tv_msg_body.setText(number.getBody());
            holder.cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStates.put(position, isChecked);
                    Log.d("onCheckedChanged", "position: "+position + " --- " + isChecked);
                }
            });

            //初始化时checkStates为空
            holder.cb_selected.setChecked(checkStates.get(position, false));

        }
    }
    static class ViewHolder{
        public TextView tv_phone_number_or_name;
        public TextView tv_msg_body;
        public CheckBox cb_selected;

        public ViewHolder(View view){
            tv_phone_number_or_name = (TextView) view.findViewById(R.id.tv_phone_number_or_name);
            tv_msg_body = (TextView) view.findViewById(R.id.tv_msg_body);
            cb_selected = (CheckBox) view.findViewById(R.id.cb_selected);
        }

    }

}
