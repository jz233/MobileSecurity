package zjj.app.mobilesecurity.activities;

import android.content.Intent;

import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.utils.Constants;

/**
 * Created by DouJ on 12/08/2016.
 */
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQ_CONFIRM_PATTERN && resultCode == RESULT_OK){
            finish();
        }else{

        }
    }
}
