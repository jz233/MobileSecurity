package zjj.app.mobilesecurity.fragments;


import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.callsmsfilter.BlackListActivity;
import zjj.app.mobilesecurity.base.BaseLogFragment;
import zjj.app.mobilesecurity.domain.CallLogNumber;
import zjj.app.mobilesecurity.parsers.CallLogParser;

public class CallLogFragment extends BaseLogFragment{

    private CallLogAdapter adapter;

    private SparseBooleanArray checkStates;
    private List<CallLogNumber> numbers;


    public CallLogFragment() {
    }

    public static CallLogFragment newInstance() {
        CallLogFragment fragment = new CallLogFragment();
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
        new GetNumberFromLogTask().execute();
    }

    @Override
    public void addNumberToBlackList() {
        //黑名单号码添加到数据库
        for(int i=0; i<numbers.size(); i++){
            if(checkStates.get(i, false)){
                CallLogNumber number = numbers.get(i);
                if(TextUtils.isEmpty(number.getName())){
                    dao.addNumber(number.getNumber(), "");
                }else{
                    dao.addNumber(number.getNumber(), number.getName());
                }
            }

        }

        //关闭fragment时更新activity黑名单列表
        FragmentActivity activity = getActivity();
        if(activity instanceof BlackListActivity){
            ((BlackListActivity)activity).initData();
        }
    }

    private class GetNumberFromLogTask extends AsyncTask<Void, Void, List<CallLogNumber>>{

        @Override
        protected List<CallLogNumber> doInBackground(Void... params) {
            numbers = CallLogParser.getCallLog(context);
            return numbers;
        }

        @Override
        protected void onPostExecute(List<CallLogNumber> numbers) {
            super.onPostExecute(numbers);

            loading.setVisibility(View.INVISIBLE);
            checkStates = new SparseBooleanArray(numbers.size());

            if(adapter == null){
                adapter = new CallLogAdapter(numbers);
                lv_log.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }



   private class CallLogAdapter extends BaseAdapter{
        private List<CallLogNumber> numbers;

        public CallLogAdapter(List<CallLogNumber> numbers){
            this.numbers = numbers;

        }

        @Override
        public int getCount() {
            return numbers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.item_call_log, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            CallLogNumber number = numbers.get(position);
            String name = number.getName();
            if(!TextUtils.isEmpty(name)){
                holder.tv_phone_number_or_name.setText(name);
            }else{
                String phone = number.getNumber();
                if("-1".equals(phone) || "-2".equals(phone)){
                    holder.tv_phone_number_or_name.setText("未知号码");
                }else{
                    holder.tv_phone_number_or_name.setText(phone);
                }
            }
            int type = number.getType();
            if(type == 1){
                holder.iv_type.setImageResource(R.drawable.call_missed);
            }else if(type == 2){
                holder.iv_type.setImageResource(R.drawable.call_out);
            }else if(type == 3){
                holder.iv_type.setImageResource(R.drawable.call_incoming);
            }
            holder.tv_date.setText(number.getDate());

            holder.cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStates.put(position, isChecked);
                }
            });
            holder.cb_selected.setChecked(checkStates.get(position,false));

            return convertView;
        }


        public void setChecked(int position, boolean checked){
            checkStates.put(position, checked);
        }
        public boolean isChecked(int position){
            return checkStates.get(position, false);
        }

    }

    private static class ViewHolder{
        private TextView tv_phone_number_or_name;
        private ImageView iv_type;
        private TextView tv_date;
        private CheckBox cb_selected;

        public ViewHolder(View view) {
            tv_phone_number_or_name = (TextView) view.findViewById(R.id.tv_phone_number_or_name);
            iv_type = (ImageView) view.findViewById(R.id.iv_type);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            cb_selected = (CheckBox) view.findViewById(R.id.cb_selected);
        }

    }


}
