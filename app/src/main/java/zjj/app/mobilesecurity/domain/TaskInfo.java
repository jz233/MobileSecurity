package zjj.app.mobilesecurity.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

    private String pkgName;
    private String appName;
    private int memSize;
    private Drawable appIcon;
    private boolean isUsrApp;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUsrApp() {
        return isUsrApp;
    }

    public void setUsrApp(boolean usrApp) {
        isUsrApp = usrApp;
    }

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

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", appName='" + appName + '\'' +
                ", memSize=" + memSize +
                ", appIcon=" + appIcon +
                ", isUsrApp=" + isUsrApp +
                ", isSelected=" + isSelected +
                '}';
    }
}
