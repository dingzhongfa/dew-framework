package com.tairanchina.csp.dew;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

//@SpringBootApplication
public class MybatisplusExampleApplication extends DewBootApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(MybatisplusExampleApplication.class, args);
//	}

	public static void main(String[] args) throws InterruptedException {
		new SpringApplicationBuilder(MybatisplusExampleApplication.class).run(args);
	}
}
