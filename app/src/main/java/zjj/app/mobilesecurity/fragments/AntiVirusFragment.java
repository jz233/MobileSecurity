package zjj.app.mobilesecurity.fragments;

import android.animation.ValueAnimator;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.HomeActivity;
import zjj.app.mobilesecurity.base.BaseHomePagerFragment;
import zjj.app.mobilesecurity.dao.AntiVirusDao;
import zjj.app.mobilesecurity.ui.AntiVirusButtonView;
import zjj.app.mobilesecurity.utils.MD5CheckUtil;
import zjj.app.mobilesecurity.utils.SystemUtils;
import zjj.app.mobilesecurity.utils.UIUtils;

public class AntiVirusFragment extends BaseHomePagerFragment {

    @BindView(R.id.ll_scanning_progress)
    LinearLayout ll_scanning_progress;
    @BindView(R.id.antivirus_topbtn)
    AntiVirusButtonView antivirus_topbtn;
    @BindView(R.id.iv_app_icon1)
    ImageView iv_app_icon1;
    @BindView(R.id.iv_app_icon2)
    ImageView iv_app_icon2;
    @BindView(R.id.iv_app_icon3)
    ImageView iv_app_icon3;
    @BindView(R.id.iv_app_icon4)
    ImageView iv_app_icon4;
    @BindView(R.id.tv_scanning)
    TextView tv_scanning;

    @OnClick(R.id.antivirus_topbtn)
    public void start(){
        antivirus_topbtn.buttonClickAnimation();
//        startScanningViruses();
    }

    private static final int INTERVAL = 500;
    private ImageView[] iconRefs;
    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private boolean scanning;
    private List<Drawable> appIcons;
    private Unbinder unbinder;

    public AntiVirusFragment() {
        // Required empty public constructor
    }

    public interface OnCheckCompleteListener {
        void onCheckComplete();
    }


    public static AntiVirusFragment newInstance() {
        AntiVirusFragment fragment = new AntiVirusFragment();
        return fragment;
    }


    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_antivirus, null);
        unbinder = ButterKnife.bind(this, view);

        iconRefs = new ImageView[]{iv_app_icon1, iv_app_icon2, iv_app_icon3, iv_app_icon4};

        return view;
    }

    @Override
    public void initListener() {
        /*antivirus_topbtn.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antivirus_topbtn.buttonClickAnimation();
                startScanningViruses();
            }
        });*/
    }


    private void startScanningViruses() {
        Log.d("AntiVirusFragment", "start scanning");
        tv_scanning.setText("正在扫描...");
        antivirus_topbtn.setClickable(false);
        antivirus_topbtn.setEnabled(false);

        new Thread(){
            @Override
            public void run() {
                super.run();
                scanning = true;
                appIcons = SystemUtils.getInstalledAppIcons(context);
                Collections.shuffle(appIcons);
                //启动App图标动画
                startScanAnimation();
            }
        }.start();
        
        scanVirusWork();
    }

    @UiThread
    private void scanAnimation(List<Drawable> icons) {
        //四个IV控件引用
        int i, len = iconRefs.length;
        Drawable iconDrawable;
        for (i = 0; i < len; i++) {
            final Animation scaling = AnimationUtils.loadAnimation(context, R.anim.icon_scaling);
            //得到其中一个控件引用
            final ImageView iconRef = iconRefs[i];
            //得到其中一个图标drawable
            iconDrawable = icons.get(i);
            iconRef.setImageDrawable(iconDrawable);
            //TODO 如果在此期间扫描完成, 停止动画, 跳出方法
            if (!scanning) {
                scaling.cancel();
                scaling.reset();
                break;
            }
            //启动其中一个图标的scale动画
            iconRef.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scaling.setDuration(1000);
                    iconRef.setAnimation(scaling);
                    scaling.start();
                    iconRef.setVisibility(View.VISIBLE);
                }
            }, INTERVAL * i);
            //最后一个scale动画结束后开始整体上移
            if (i == len - 1) {
                scaling.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startYTranslation();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }

    @UiThread
    private void startYTranslation() {
        Animation translation = AnimationUtils.loadAnimation(context, R.anim.ll_translation);
        translation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (ImageView iconRef : iconRefs) {
                    iconRef.setVisibility(View.INVISIBLE);
                }
                //随机排列所有应用图标
                Collections.shuffle(appIcons);
                startScanAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_scanning_progress.startAnimation(translation);
    }

    private void startScanAnimation() {
        if (scanning) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    scanAnimation(appIcons.subList(0, 4));
                }
            });
        }
    }

    private void scanVirusWork() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //扫描病毒
                List<PackageInfo> infos = context.getPackageManager().getInstalledPackages(0);
                AntiVirusDao dao = new AntiVirusDao(context);
                String sourceDir;
                String md5;
                for (PackageInfo info : infos) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        sourceDir = info.applicationInfo.sourceDir;
                        md5 = MD5CheckUtil.getMD5(sourceDir);
                        String description = dao.virusScan(md5);
                        if (!TextUtils.isEmpty(description)) {
                            //TODO 病毒处理
                        }
                    }
                }
                dao.closeDB();
                scanning = false;
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_scanning.setText("扫描完成, 没有发现病毒");
                        antivirus_topbtn.setClickable(true);
                        antivirus_topbtn.setEnabled(true);
                    }
                });
                Log.d("AntiVirusFragment", "scan finished");
            }
        }.start();
    }

    @Override
    public void initData() {
        antivirus_topbtn.setStatus(AntiVirusButtonView.STATUS_OK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
