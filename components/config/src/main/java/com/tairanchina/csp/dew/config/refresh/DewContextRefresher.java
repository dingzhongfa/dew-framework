package com.tairanchina.csp.dew.config.refresh;

/*
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.*;

*/
/**
 * desription:
 * Created by ding on 2017/11/8.
 *//*

@Component
public class DewContextRefresher extends ContextRefresher {

    private static final String REFRESH_ARGS_PROPERTY_SOURCE = "refreshArgs";

    private Set<String> standardSources = new HashSet<String>(
            Arrays.asList(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,
                    StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                    StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME,
                    StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME,
                    StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME));

    private ConfigurableApplicationContext context;
    private RefreshScope scope;

    public DewContextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        super(context, scope);
    }
    @Value("${bootstrap.path:/users/ding/Desktop/config/bootstrap.yml}")
    private String bootstrapPath;

    @Override
    public synchronized Set<String> refresh() {
        Map<String, Object> before = extract(
                this.context.getEnvironment().getPropertySources());
        addConfigFilesToEnvironment();
        Map<String, Object> repositoryChange = extract(this.context.getEnvironment().getPropertySources());
        // 读取配置文件，增加变量
      */
/*  try {
            Yaml yaml = new Yaml();
            HashMap ml = yaml.loadAs(new FileInputStream(bootstrapPath), HashMap.class);
            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*//*

        Set<String> keys = changes(before,repositoryChange ).keySet();
        this.context.publishEvent(new EnvironmentChangeEvent(keys));
        this.scope.refreshAll();
        return keys;
    }

    private void addConfigFilesToEnvironment() {
        ConfigurableApplicationContext capture = null;
        try {
            StandardEnvironment environment = copyEnvironment(
                    this.context.getEnvironment());
            SpringApplicationBuilder builder = new SpringApplicationBuilder(Empty.class)
                    .bannerMode(Banner.Mode.OFF).web(false).environment(environment);
            // Just the listeners that affect the environment (e.g. excluding logging
            // listener because it has side effects)
            builder.application()
                    .setListeners(Arrays.asList(new BootstrapApplicationListener(),
                            new ConfigFileApplicationListener()));
            capture = builder.run();
            if (environment.getPropertySources().contains(REFRESH_ARGS_PROPERTY_SOURCE)) {
                environment.getPropertySources().remove(REFRESH_ARGS_PROPERTY_SOURCE);
            }
            MutablePropertySources target = this.context.getEnvironment()
                    .getPropertySources();
            String targetName = null;
            for (PropertySource<?> source : environment.getPropertySources()) {
                String name = source.getName();
                if (target.contains(name)) {
                    targetName = name;
                }
                if (!this.standardSources.contains(name)) {
                    if (target.contains(name)) {
                        target.replace(name, source);
                    }
                    else {
                        if (targetName != null) {
                            target.addAfter(targetName, source);
                        }
                        else {
                            if (target.contains("defaultProperties")) {
                                target.addBefore("defaultProperties", source);
                            }
                            else {
                                target.addLast(source);
                            }
                        }
                    }
                }
            }
        }
        finally {
            ConfigurableApplicationContext closeable = capture;
            closeable.close();
        }

    }

    // Don't use ConfigurableEnvironment.merge() in case there are clashes with property
    // source names
    private StandardEnvironment copyEnvironment(ConfigurableEnvironment input) {
        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources capturedPropertySources = environment.getPropertySources();
        for (PropertySource<?> source : capturedPropertySources) {
            capturedPropertySources.remove(source.getName());
        }
        for (PropertySource<?> source : input.getPropertySources()) {
            capturedPropertySources.addLast(source);
        }
        environment.setActiveProfiles(input.getActiveProfiles());
        environment.setDefaultProfiles(input.getDefaultProfiles());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("spring.jmx.enabled", false);
        map.put("spring.main.sources", "");
        capturedPropertySources
                .addFirst(new MapPropertySource(REFRESH_ARGS_PROPERTY_SOURCE, map));
        return environment;
    }

    private Map<String, Object> changes(Map<String, Object> before,
                                        Map<String, Object> after) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : before.keySet()) {
            if (!after.containsKey(key)) {
                result.put(key, null);
            }
            else if (!equal(before.get(key), after.get(key))) {
                result.put(key, after.get(key));
            }
        }
        for (String key : after.keySet()) {
            if (!before.containsKey(key)) {
                result.put(key, after.get(key));
            }
        }
        return result;
    }

    private boolean equal(Object one, Object two) {
        if (one == null && two == null) {
            return true;
        }
        if (one == null || two == null) {
            return false;
        }
        return one.equals(two);
    }

    private Map<String, Object> extract(MutablePropertySources propertySources) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<PropertySource<?>> sources = new ArrayList<PropertySource<?>>();
        for (PropertySource<?> source : propertySources) {
            sources.add(0, source);
        }
        for (PropertySource<?> source : sources) {
            if (!this.standardSources.contains(source.getName())) {
                extract(source, result);
            }
        }
        return result;
    }

    private void extract(PropertySource<?> parent, Map<String, Object> result) {
        if (parent instanceof CompositePropertySource) {
            try {
                List<PropertySource<?>> sources = new ArrayList<PropertySource<?>>();
                for (PropertySource<?> source : ((CompositePropertySource) parent)
                        .getPropertySources()) {
                    sources.add(0, source);
                }
                for (PropertySource<?> source : sources) {
                    extract(source, result);
                }
            }
            catch (Exception e) {
                return;
            }
        }
        else if (parent instanceof EnumerablePropertySource) {
            for (String key : ((EnumerablePropertySource<?>) parent).getPropertyNames()) {
                result.put(key, parent.getProperty(key));
            }
        }
    }

    public ConfigurableApplicationContext getContext() {
        return context;
    }

    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public RefreshScope getScope() {
        return scope;
    }

    public void setScope(RefreshScope scope) {
        this.scope = scope;
    }
}
*/
