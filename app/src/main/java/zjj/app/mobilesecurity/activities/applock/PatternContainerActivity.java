package zjj.app.mobilesecurity.activities.applock;

import android.content.Intent;

import zjj.app.mobilesecurity.activities.applock.AppLockConfirmPattern2Activity;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.utils.Constants;

public class PatternContainerActivity extends BaseActivity {
    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        startActivityForResult(new Intent(this, AppLockConfirmPattern2Activity.class), Constants.REQ_CONFIRM_PATTERN);
    }

    @Override
    public void setAppTheme() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQ_CONFIRM_PATTERN && resultCode == RESULT_OK){
            finish();
        }else{

        }
    }
}
