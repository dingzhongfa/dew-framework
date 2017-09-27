package com.tairanchina.csp.dew;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PressureTestApiApplication extends DewCloudApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PressureTestApiApplication.class).run(args);
	}
}
