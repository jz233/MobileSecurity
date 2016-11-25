package zjj.app.mobilesecurity.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseFragment;
import zjj.app.mobilesecurity.domain.AppInfo;
import zjj.app.mobilesecurity.parsers.AppInfoParser;

public class MoveAppFragment extends BaseFragment {

    private static final int STATUS_FINISHED = 1;
    private RecyclerView rv_movable_app_list;
    private LinearLayout ll_loading;
    private MoveAppAdapter adapter;
    private AppInfoParser parser;
    private List<AppInfo> movableAppList;
    private MoveAppTask task;
    private TextView tv_no_result;

    public MoveAppFragment() {
    }

    public static MoveAppFragment newInstance() {
        MoveAppFragment fragment = new MoveAppFragment();
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_move_app, null);
        rv_movable_app_list = (RecyclerView) view.findViewById(R.id.rv_movable_app_list);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        tv_no_result = (TextView) view.findViewById(R.id.tv_no_result);
        return view;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        task = new MoveAppTask();
        task.execute();

    }


    private class MoveAppTask extends AsyncTask<Void, Void, List<AppInfo>>{

        @Override
        protected List<AppInfo> doInBackground(Void... params) {
            parser = new AppInfoParser();
            movableAppList = parser.getMovableAppList(context);
            return movableAppList;
        }

        @Override
        protected void onPostExecute(List<AppInfo> movableAppList) {
            super.onPostExecute(movableAppList);
            if(movableAppList.size() == 0){
                ll_loading.setVisibility(View.INVISIBLE);
                tv_no_result.setVisibility(View.VISIBLE);
            }else{
                ll_loading.setVisibility(View.INVISIBLE);
                if (adapter == null) {
                    rv_movable_app_list.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new MoveAppAdapter();
                    rv_movable_app_list.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class MoveAppAdapter extends RecyclerView.Adapter<MovableAppItem> {

        @Override
        public MovableAppItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_movable_app_list, null);
            return new MovableAppItem(view);
        }

        @Override
        public void onBindViewHolder(MovableAppItem holder, int position) {
            AppInfo appInfo = movableAppList.get(position);
            holder.iv_app_icon.setImageDrawable(appInfo.appIcon);
            holder.tv_app_name.setText(appInfo.appName);
            holder.tv_app_size.setText(appInfo.appSize);
        }

        @Override
        public int getItemCount() {
            return movableAppList.size();
        }
    }

    private class MovableAppItem extends RecyclerView.ViewHolder {

        private final ImageView iv_app_icon;
        private final TextView tv_app_name;
        private final TextView tv_app_size;
        private final CardView cv_movable_app;


        public MovableAppItem(View itemView) {
            super(itemView);
            iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon1);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            tv_app_size = (TextView) itemView.findViewById(R.id.tv_app_size);
            cv_movable_app = (CardView) itemView.findViewById(R.id.cv_movable_app);

            cv_movable_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pkgName = movableAppList.get(getAdapterPosition()).pkgName;
                    moveApp(pkgName);
                }
            });
        }

    }

    private void moveApp(String pkgName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + pkgName));

        startActivity(intent);
    }
}
