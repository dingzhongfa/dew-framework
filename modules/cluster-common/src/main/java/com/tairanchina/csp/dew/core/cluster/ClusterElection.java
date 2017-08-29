package com.tairanchina.csp.dew.core.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ClusterElection {

    Logger logger = LoggerFactory.getLogger(ClusterElection.class);

    void election() throws Exception;

    void quit() throws Exception;

    boolean isLeader();

}
