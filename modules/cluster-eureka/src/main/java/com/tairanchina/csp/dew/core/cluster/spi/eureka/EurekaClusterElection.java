package com.tairanchina.csp.dew.core.cluster.spi.eureka;

import com.ecfront.dew.common.$;
import com.fasterxml.jackson.databind.JsonNode;
import com.tairanchina.csp.dew.core.cluster.ClusterElection;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnExpression("#{'${dew.cluster.election}'=='eureka'}")
public class EurekaClusterElection implements ClusterElection {

    private static boolean leader = false;

    @Value("${dew.cluster.election.config.election-period-sec:60}")
    private int electionPeriodSec;
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private EurekaClientConfigBean eurekaClientConfigBean;

    @Override
    @PostConstruct
    public void election() throws Exception {
        $.timer.periodic(electionPeriodSec, false, () -> {
            List<String> urls = eurekaClientConfigBean.getServiceUrl().values().stream()
                    .flatMap(url -> Arrays.stream(url.split(","))).collect(Collectors.toList());
            for (String url : urls) {
                try {
                    String res = $.http.get(url + "/apps/" + applicationName + "/", new HashMap<String, String>() {{
                        put("Content-Type", "application/json");
                        put("Accept", "application/json");
                    }});
                    logger.trace("Fetch cluster data:" + res);
                    if (!StringUtils.isEmpty(res)) {
                       JsonNode instances= $.json.toJson(res).get("application").get("instance");
                       for (JsonNode instance : instances){
                           if(instance.get("status").asText("").equals("UP")){
                               // First online instance

                           }
                       }
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void quit() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public boolean isLeader() {
        return leader;
    }
}
