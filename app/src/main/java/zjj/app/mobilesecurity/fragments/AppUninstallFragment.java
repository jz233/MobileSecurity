package zjj.app.mobilesecurity.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class AppUninstallFragment extends BaseFragment {

    private static final int STATUS_FINISHED = 2;
    private RecyclerView rv_app_list;
    private AppListAdapter adapter;
    private List<AppInfo> appInfoList;
    private LinearLayout ll_loading;

    private UninstallAppReceiver receiver;
    private int current_selected_item_position = -1;
    private IntentFilter filter;
    private AppUninstallTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AppUninstallFragment", "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("AppUninstallFragment", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AppUninstallFragment", "onResume");

        //避免多次注册
        if(receiver == null){
            receiver = new UninstallAppReceiver();

            filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
            filter.setPriority(Integer.MAX_VALUE);
            filter.addDataScheme("package");

            context.registerReceiver(receiver, filter);
        }
    }

    //跳转至卸载界面时调用
    @Override
    public void onPause() {
        super.onPause();
        Log.d("AppUninstallFragment", "onPause");
    }

    //跳转至卸载界面时调用
    @Override
    public void onStop() {
        super.onStop();
        Log.d("AppUninstallFragment", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("AppUninstallFragment", "onDestroyView");
        task.cancel(true);
        context.unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AppUninstallFragment", "onDestroy");
    }

    public AppUninstallFragment() {
    }

    public static AppUninstallFragment newInstance() {
        AppUninstallFragment fragment = new AppUninstallFragment();
        return fragment;
    }


    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_app_uninstall, null);
        rv_app_list = (RecyclerView) view.findViewById(R.id.rv_app_list);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);

        return view;
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        task = new AppUninstallTask();
        task.execute();

    }

    private class UninstallAppReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("AppUninstallFragment", intent.getAction());
            if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
                Log.d("AppUninstallFragment", "onReceive");
                appInfoList.remove(current_selected_item_position);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class AppListAdapter extends RecyclerView.Adapter<AppViewHolder>{

        private Context context;
        private List<AppInfo> appInfoList;

        public AppListAdapter(Context context, List<AppInfo> appInfoList){
            this.context = context;
            this.appInfoList = appInfoList;
        }

        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_app_list, null);
            AppViewHolder holder = new AppViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(AppViewHolder holder, int position) {
            AppInfo info = appInfoList.get(position);
            holder.iv_app_icon.setImageDrawable(info.appIcon);
            holder.tv_app_name.setText(info.appName);
            holder.tv_app_size.setText(info.appSize);

        }

        @Override
        public int getItemCount() {
            return appInfoList.size();
        }
    }

    private class AppViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_app_icon;
        private TextView tv_app_name;
        private TextView tv_app_size;
        private ImageView iv_uninstall_app;

        public AppViewHolder(final View itemView) {
            super(itemView);

            iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon1);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            tv_app_size = (TextView) itemView.findViewById(R.id.tv_app_size);
            iv_uninstall_app = (ImageView) itemView.findViewById(R.id.iv_uninstall_app);

            iv_uninstall_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_selected_item_position = getAdapterPosition();
                    AppInfo appInfo = appInfoList.get(current_selected_item_position);
                    uninstallApp(appInfo.pkgName);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_selected_item_position = getAdapterPosition();
                    final AppInfo appInfo = appInfoList.get(current_selected_item_position);

                    Log.d("AppUninstallFragment", appInfo.toString());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setView();
                    builder.setIcon(appInfo.appIcon);
                    builder.setTitle(appInfo.appName);
                    builder.setMessage("大小: " + appInfo.appSize + "\n 版本: " + appInfo.versionName + "\n 包名: " + appInfo.pkgName + "\n 安装时间: " + appInfo.installedTime);


                    builder.setPositiveButton("卸载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uninstallApp(appInfo.pkgName);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }
    }

    private void uninstallApp(String pkgName) {
        Intent unInstallIntent = new Intent();
        unInstallIntent.setAction(Intent.ACTION_DELETE);
        unInstallIntent.setData(Uri.parse("package:" + pkgName));

        startActivity(unInstallIntent);
    }

    private class AppUninstallTask extends AsyncTask<Void, Void, List<AppInfo>>{

        @Override
        protected List<AppInfo> doInBackground(Void... params) {

            AppInfoParser parser = new AppInfoParser();
            appInfoList = parser.getAllAppsInfo(context);

            return appInfoList;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfoList) {
            super.onPostExecute(appInfoList);

            ll_loading.setVisibility(View.INVISIBLE);
            if(adapter == null){
                rv_app_list.setLayoutManager(new LinearLayoutManager(context));
                adapter = new AppListAdapter(context, appInfoList);
                rv_app_list.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }

        }
    }

}
