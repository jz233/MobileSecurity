package zjj.app.mobilesecurity.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

    public String pkgName;
    public String appName;
    public String appSize;
    public Drawable appIcon;
    public String versionName;
    public String installedTime;
    public boolean isSystemApp;

    @Override
    public String toString() {
        return "AppInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", appName='" + appName + '\'' +
                ", appSize='" + appSize + '\'' +
                ", versionName='" + versionName + '\'' +
                ", installedTime='" + installedTime + '\'' +
                '}';
    }
}
