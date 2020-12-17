package com.onejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.onejava.service") // The Package where proxy interfaces are defined
public class GuestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestApplication.class, args);
	}

}
