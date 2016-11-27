package zjj.app.mobilesecurity.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.HomeActivity;
import zjj.app.mobilesecurity.base.BaseHomePagerFragment;
import zjj.app.mobilesecurity.utils.UIUtils;

public class SpeedUpFragment extends BaseHomePagerFragment {

    private ImageView img_memory;
    private TextView tv_process_count;
    private OnCheckCompleteListener listener;
    private Resources resources;

    public SpeedUpFragment() {
        // Required empty public constructor
    }

    public interface OnCheckCompleteListener{
        void onCheckComplete();
    }

    public static SpeedUpFragment newInstance() {
        SpeedUpFragment fragment = new SpeedUpFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnCheckCompleteListener) context;
        } catch (Exception e) {
            throw new ClassCastException("OnCheckCompleteListener must be implement in this activity");
        }
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_speed_up, null);
//        img_memory = (ImageView) view.findViewById(R.id.layout_cardview_memory).findViewById(R.id.img_memory);
        tv_process_count = (TextView) view.findViewById(R.id.layout_cardview_memory).findViewById(R.id.tv_process_count);

        return view;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Log.d("BaseFragment", "onActivityCreated -- SpeedUp");

        Activity context = getActivity();
        resources = context.getResources();
//        Drawable imgMemory = resources.getDrawable(R.drawable.img_memory);
//        imgMemory.setColorFilter(Color.parseColor("#FF5722"), PorterDuff.Mode.MULTIPLY);
//        img_memory.setColorFilter(R.color.colorPrimaryMed);

        if(context instanceof HomeActivity){
            listener.onCheckComplete();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BaseFragment", "onResume -- SpeedUp");
    }
}
