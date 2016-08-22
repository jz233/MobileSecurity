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
import zjj.app.mobilesecurity.dao.WhiteListDao;
import zjj.app.mobilesecurity.domain.AppInfo;

public class WhiteListAdapter extends RecyclerView.Adapter<WhiteListAdapter.WhiteListHolder> {

    private WhiteListDao dao;
    private List<AppInfo> infos;
    private Context context;
    private  List<String> whiteList;

    public WhiteListAdapter(Context context, List<AppInfo> infos, List<String> whiteList) {
        this.context = context;
        this.infos = infos;
        this.whiteList = whiteList;
        dao = new WhiteListDao(context);
    }

    @Override
    public WhiteListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_app_white_list, null);
        WhiteListHolder holder = new WhiteListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WhiteListHolder holder, int position) {
        AppInfo info = infos.get(position);
        holder.iv_app_icon.setImageDrawable(info.appIcon);
        holder.tv_app_name.setText(info.appName);
        boolean enabled = dao.hasApp(info.pkgName);
        holder.cb_selected.setChecked(enabled);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    class WhiteListHolder extends RecyclerView.ViewHolder{

        private ImageView iv_app_icon;
        private TextView tv_app_name;
        private CheckBox cb_selected;

        public WhiteListHolder(View itemView) {
            super(itemView);
            iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            cb_selected = (CheckBox) itemView.findViewById(R.id.cb_selected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb_selected.toggle();
                }
            });

            cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //只有条目被点击才会触发数据库操作，避免上下滚动显示条目时执行修改数据库的方法
                    if(buttonView.isPressed()){
                        String pkgName = infos.get(getAdapterPosition()).pkgName;
                        if(isChecked){
                            dao.add(pkgName);
                        }else{
                            dao.delete(pkgName);
                        }
                    }
                }
            });
        }
    }
}
