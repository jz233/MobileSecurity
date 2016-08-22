package zjj.app.mobilesecurity.utils;

/**
 * Created by DouJ on 31/07/2016.
 */
public class Constants {

    public static final String URI_CONVERSATIONS = "content://sms/conversations";
    public static final String PATTERN_CORRECT = "zjj.app.pattern.correct";
    public static final int REQ_SET_PATTERN = 101;
    public static final int REQ_CONFIRM_PATTERN = 102;

    public static final String PREF_KEY_RELOCK_SCREEN_OFF = "pref_key_relock_screen_off";
    public static final String PREF_KEY_RELOCK_TIME = "pref_key_relock_time";
    public static final String PREF_KEY_AUTO_CLEAN_SCREEN_OFF = "pref_key_auto_clean_screen_off";
    public static final String PREF_KEY_WHITE_LIST = "pref_key_whitelist";


    public static final String TABLE_APP_LOCK = "applockinfo";
    public static final String TABLE_TRAFFIC = "trafficinfo";
    public static final String TABLE_WHITE_LIST = "whitelistinfo";
    public static final String TABLE_BLACK_LIST = "blacklistinfo";



    public static final String URI_LOCKEDAPPCHANGE = "content://zjj.app.lockedappchange";


}
