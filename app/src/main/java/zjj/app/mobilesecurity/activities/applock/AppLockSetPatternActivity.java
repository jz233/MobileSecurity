package zjj.app.mobilesecurity.activities.applock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;
import zjj.app.mobilesecurity.BuildConfig;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockSetPatternActivity extends SetPatternActivity{

    private SharedPreferences sp;

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        super.onSetPattern(pattern);
        String sha1String = PatternUtils.patternToSha1String(pattern);
        SharedPreferencesUtils.putString(this, "pattern", sha1String);

        //有确定按钮，无需在此处finish()
    }
}
