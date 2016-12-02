package zjj.app.mobilesecurity.activities.applock;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockConfirmPattern2Activity extends ConfirmPatternActivity {
    @Override
    protected boolean isStealthModeEnabled() {
        //暂时不用
        return false;
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        String pStr = SharedPreferencesUtils.getString(this, "pattern", null);
        return TextUtils.equals(pStr, PatternUtils.patternToSha1String(pattern));
    }

    @Override
    protected void onForgotPassword() {
        super.onForgotPassword();

        Toast.makeText(this, "onForgotPassword", Toast.LENGTH_SHORT).show();
    }

    //点击"取消"按钮
    //覆盖父类方法并不执行super.onCancel()，因为父类中会执行finish()
    @Override
    protected void onCancel() {
        super.onCancel();
        returnToHomeScreen();
    }

    @Override
    protected void onConfirmed() {
        String pkgName = getIntent().getStringExtra("packagename");
        //发送自定义广播，提示密码正确
        Intent intent = new Intent(Constants.PATTERN_CORRECT);
        intent.putExtra("packagename", pkgName);
        sendBroadcast(intent);
        super.onConfirmed();
    }

    //点返回键同样返回桌面
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onCancel();
    }

    private void returnToHomeScreen() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
