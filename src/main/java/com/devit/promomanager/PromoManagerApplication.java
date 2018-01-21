package com.devit.promomanager;

import org.dozer.DozerBeanMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"io.swagger", "com.devit"})
@EnableDiscoveryClient
public class PromoManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromoManagerApplication.class, args);
	}

	@Bean
	public DozerBeanMapper dozerBeanMapper() {
		List<String> mappingFiles = new ArrayList<>();
		mappingFiles.add("dozer-mappings.xml");
		DozerBeanMapper mapper = new DozerBeanMapper(mappingFiles);
		return mapper;
	}

}
