package com.greedymap.yogi.helper;

import java.util.Date;

/**
 * Created by yogi on 3/5/17.
 */

public class IPAndTIme {
    Date accesTime;
    String IP;
    public String getIP() {
        return IP;
    }

    public Date getAccesTime() {
        return accesTime;
    }

    public void setAccesTime(Date accesTime) {
        this.accesTime = accesTime;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
