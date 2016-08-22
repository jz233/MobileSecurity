package zjj.app.mobilesecurity.domain;

public class AppLockInfo {
    private int id;
    private String pkgName;
    private long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "AppLockInfo{" +
                "id=" + id +
                ", pkgName='" + pkgName + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
