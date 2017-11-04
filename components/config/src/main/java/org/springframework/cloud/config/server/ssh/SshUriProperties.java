package org.springframework.cloud.config.server.ssh;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties("spring.cloud.config.server.git")
@Validated
@PrivateKeyIsValid
@HostKeyAndAlgoBothExist
@HostKeyAlgoSupported
@RefreshScope
public class SshUriProperties extends SshUri {
    private Map<String, SshUriNestedRepoProperties> repos = new LinkedHashMap();

    public SshUriProperties() {
    }

    public Map<String, SshUriProperties.SshUriNestedRepoProperties> getRepos() {
        return this.repos;
    }

    public void setRepos(Map<String, SshUriProperties.SshUriNestedRepoProperties> repos) {
        this.repos = repos;
    }

    public void addRepo(String repoName, SshUriProperties.SshUriNestedRepoProperties properties) {
        this.repos.put(repoName, properties);
    }

    public String toString() {
        return super.toString() + "{repos=" + this.repos + "}";
    }

    public static class SshUriNestedRepoProperties extends SshUri {
        public SshUriNestedRepoProperties() {
        }
    }
}
