package zjj.app.mobilesecurity.fragments;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.callsmsfilter.BlackListActivity;
import zjj.app.mobilesecurity.base.BaseLogFragment;
import zjj.app.mobilesecurity.domain.ContactInfo;
import zjj.app.mobilesecurity.parsers.ContactParser;

public class ContactsFragment extends BaseLogFragment {

    private SparseBooleanArray checkStates;
    private ContactsTask task;
    private List<ContactInfo> list;
    private ContactsAdapter adapter;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
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
        task = new ContactsTask();
        task.execute();

    }

    @Override
    public void addNumberToBlackList() {
        //黑名单号码添加到数据库
        for(int i=0; i<list.size(); i++){
            if(checkStates.get(i, false)){
                ContactInfo info = list.get(i);
                dao.addNumber(info.getNumber(), info.getName());
            }

        }

        //关闭fragment时更新activity黑名单列表
        FragmentActivity activity = getActivity();
        if(activity instanceof BlackListActivity){
            ((BlackListActivity)activity).initData();
        }
    }


    class ContactsTask extends AsyncTask<Void, Void, List<ContactInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ContactInfo> doInBackground(Void... params) {
            list = ContactParser.getAllContacts(context.getContentResolver());
            return list;
        }

        @Override
        protected void onPostExecute(List<ContactInfo> list) {
            super.onPostExecute(list);

            checkStates = new SparseBooleanArray(list.size());
            loading.setVisibility(View.INVISIBLE);
            if(adapter == null){
                adapter = new ContactsAdapter();
                lv_log.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }


    class ContactsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.item_contact, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_number.setText(list.get(position).getNumber());
            holder.cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStates.put(position, isChecked);
                }
            });

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    static class ViewHolder{
        public TextView tv_name;
        public TextView tv_number;
        public CheckBox cb_selected;

        public ViewHolder(View view){
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_number = (TextView) view.findViewById(R.id.tv_number);
            cb_selected = (CheckBox) view.findViewById(R.id.cb_selected);
        }
    }



}
