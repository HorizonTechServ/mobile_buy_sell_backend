package com.one.arpitInstituteAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.logger.LogManager;

@SpringBootApplication(scanBasePackages = "com.one")
@EnableJpaRepositories("com.one")
@EntityScan("com.one")
@ComponentScan("com.one")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableAsync
public class MobileBuySellApiApplication {
	private static final String PERFORMANCE = "PERFORMANCE";  // Compliant
	private static final String ANALYTICSKPI = "ANALYTICSKPI";  // Compliant
	

	public static void main(String[] args) {
		LogManager.setDefaultLogger(new DefaultLogger(MobileBuySellApiApplication.class));
		LogManager.setLogger(PERFORMANCE, new DefaultLogger(PERFORMANCE));
		LogManager.setLogger(ANALYTICSKPI, new DefaultLogger(ANALYTICSKPI));
		
		SpringApplication.run(MobileBuySellApiApplication.class, args);
	}

}
