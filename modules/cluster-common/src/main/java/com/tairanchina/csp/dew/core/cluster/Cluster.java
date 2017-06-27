package com.tairanchina.csp.dew.core.cluster;

import java.util.UUID;

public class Cluster {

    public static final String CLASS_LOAD_UNIQUE_FLAG= UUID.randomUUID().toString();

    public ClusterMQ mq;

    public ClusterDist dist;

    public ClusterCache cache;

}
