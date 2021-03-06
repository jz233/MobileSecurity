package zjj.app.mobilesecurity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();
    public abstract void setAppTheme();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme();

        super.onCreate(savedInstanceState);

        initView();
        initListener();
        initData();
    }

}
