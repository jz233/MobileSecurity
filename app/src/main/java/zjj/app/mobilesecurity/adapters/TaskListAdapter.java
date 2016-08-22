package zjj.app.mobilesecurity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zjj.app.mobilesecurity.BuildConfig;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.domain.TaskInfo;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    private List<TaskInfo> infos;
    private Context context;
    private List<TaskInfo> clearList;

    public TaskListAdapter(Context context, List<TaskInfo> infos) {
        this.infos = infos;
        this.context = context;
        clearList = new ArrayList<>();
    }

    public List<TaskInfo> getClearList() {
        return clearList;
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_task_list, null);
        TaskHolder holder = new TaskHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        TaskInfo info = infos.get(position);
        holder.iv_task_icon.setImageDrawable(info.getAppIcon());
        holder.tv_task_name.setText(info.getAppName());
        holder.tv_task_size.setText(Formatter.formatFileSize(context, info.getMemSize()*1024));
        holder.cb_selected.setChecked(info.isSelected());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    class TaskHolder extends RecyclerView.ViewHolder{
        public ImageView iv_task_icon;
        public TextView tv_task_name;
        public TextView tv_task_size;
        public CheckBox cb_selected;

        public TaskHolder(View itemView) {
            super(itemView);
            iv_task_icon = (ImageView) itemView.findViewById(R.id.iv_task_icon);
            tv_task_name = (TextView) itemView.findViewById(R.id.tv_task_name);
            tv_task_size = (TextView) itemView.findViewById(R.id.tv_task_size);
            cb_selected = (CheckBox) itemView.findViewById(R.id.cb_selected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb_selected.toggle();
                    TaskInfo info = infos.get(getAdapterPosition());
                    boolean isSelected = info.isSelected();

                    cb_selected.setChecked(!isSelected);
                    info.setSelected(!isSelected);

                }
            });

            cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (BuildConfig.DEBUG) Log.d("TaskHolder", "isChecked:" + isChecked);
                    TaskInfo info = infos.get(getAdapterPosition());
                    if(isChecked){
                        clearList.add(info);
                    }else{
                        clearList.remove(info);
                    }
                }
            });

        }
    }

}

