package com.tairanchina.csp.dew.core.metric;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

public class MonitorService {


    public static MonitorInfo getMonitorInfoBean() throws Exception {
        double mb = 1024 * 1024 * 1.0;
        // jvm
        double totalMemory = Runtime.getRuntime().totalMemory() / mb;
        double freeMemory = Runtime.getRuntime().freeMemory() / mb;
        double maxMemory = Runtime.getRuntime().maxMemory() / mb;
        // os
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        double  totalSwapSpaceSize = osmxb.getTotalSwapSpaceSize()/mb;
        double  freeSwapSpaceSize = osmxb.getFreeSwapSpaceSize()/mb;
        double  committedVirtualMemorySize= osmxb.getCommittedVirtualMemorySize()/mb;
        double totalMemorySize = osmxb.getTotalPhysicalMemorySize() / mb;
        double freeMemorySize = osmxb.getFreePhysicalMemorySize() / mb;
        double usedMemorySize = totalMemorySize-freeMemorySize;
        // MonitorInfo
        MonitorInfo infoBean = new MonitorInfo();
        infoBean.setTotalMemory(getIntValue(totalMemory));
        infoBean.setFreeMemory(getIntValue(freeMemory));
        infoBean.setMaxMemory(getIntValue(maxMemory));
        infoBean.setTotalSwapSpaceSize(getIntValue(totalSwapSpaceSize));
        infoBean.setFreeSwapSpaceSize(getIntValue(freeSwapSpaceSize));
        infoBean.setCommittedVirtualMemorySize(getIntValue(committedVirtualMemorySize));
        infoBean.setTotalMemorySize(getIntValue(totalMemorySize));
        infoBean.setFreeMemorySize(getIntValue(freeMemorySize));
        infoBean.setUsedMemorySize(getIntValue(usedMemorySize));
        infoBean.setProcessors(Runtime.getRuntime().availableProcessors());
        return infoBean;
    }

    /**
     * 四舍五入取整
     *
     * @param d
     * @return
     */
    private static int getIntValue(double d) {
        return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP)
                .intValue();
    }

}