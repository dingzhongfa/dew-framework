package com.tairanchina.csp.dew;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class PressureTestServiceApplication extends DewCloudApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PressureTestServiceApplication.class).run(args);
	}
}
