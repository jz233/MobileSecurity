package zjj.app.mobilesecurity.activities.applock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockConfirmPatternActivity extends ConfirmPatternActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_PatternLock);
        super.onCreate(savedInstanceState);
    }

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

}
