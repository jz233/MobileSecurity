package zjj.app.mobilesecurity.domain;

public class TrafficInfo {
    private String pkgName;
    private long snd;
    private long rcv;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getSnd() {
        return snd;
    }

    public void setSnd(long snd) {
        this.snd = snd;
    }

    public long getRcv() {
        return rcv;
    }

    public void setRcv(long rcv) {
        this.rcv = rcv;
    }


    @Override
    public String toString() {
        return "TrafficInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", snd=" + snd +
                ", rcv=" + rcv +
                '}';
    }

}
