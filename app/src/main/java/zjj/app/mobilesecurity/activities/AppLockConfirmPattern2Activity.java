package zjj.app.mobilesecurity.activities;

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

    //覆盖父类方法并不执行super.onCancel()，因为父类中会执行finish()
    @Override
    protected void onCancel() {
        super.onCancel();
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
}
