package zjj.app.mobilesecurity.domain;

import android.graphics.drawable.Drawable;

public class ApkInfo {

    private String pkgName;
    private String appName;
    private String versionName;
    private String sourceDir;
    private long apkSize;
    private Drawable appIcon;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "ApkInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", appName='" + appName + '\'' +
                ", sourceDir='" + sourceDir + '\'' +
                '}';
    }
}
