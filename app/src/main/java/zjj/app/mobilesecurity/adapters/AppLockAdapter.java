package zjj.app.mobilesecurity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.dao.AppLockDao;
import zjj.app.mobilesecurity.domain.AppInfo;

public class AppLockAdapter extends RecyclerView.Adapter<AppLockAdapter.AppLockHolder>{

    private List<AppInfo> infos;
    private Context context;
    private AppLockDao dao;
    private int count = 0;
    private OnAppCountChangedListener listener;

    public AppLockAdapter(Context context, List<AppInfo> infos, OnAppCountChangedListener listener){
        this.context = context;
        this.infos = infos;
        this.listener = listener;
        dao = new AppLockDao(context);
    }


    @Override
    public AppLockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_app_lock_list, null);
        AppLockHolder holder = new AppLockHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AppLockHolder holder, int position) {
        AppInfo info = infos.get(position);
        holder.iv_app_icon.setImageDrawable(info.appIcon);
        holder.tv_app_name.setText(info.appName);

        String pkgName = infos.get(position).pkgName;
        boolean isLocked = dao.isAppLocked(pkgName);
        if(isLocked)
            count++;

        holder.cb_locked.setChecked(isLocked);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public interface OnAppCountChangedListener{
        void onAppCountChanged(boolean isPositive);
    }



    class AppLockHolder extends RecyclerView.ViewHolder{

        public ImageView iv_app_icon;
        public TextView tv_app_name;
        public TextView tv_app_desc;
        public CheckBox cb_locked;

        public AppLockHolder(View itemView) {
            super(itemView);

            iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            tv_app_desc = (TextView) itemView.findViewById(R.id.tv_app_desc);
            cb_locked = (CheckBox) itemView.findViewById(R.id.cb_locked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb_locked.toggle();
                }
            });

            cb_locked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //防止初始化时重复操作数据库
                    if(buttonView.isPressed()){
                        int position = getAdapterPosition();
                        if(isChecked){
                            dao.addLockedApp(infos.get(position).pkgName);
                        }else{
                            dao.removeUnlockedApp(infos.get(position).pkgName);
                        }
                        listener.onAppCountChanged(isChecked);
                    }
                }
            });

        }
    }
}
