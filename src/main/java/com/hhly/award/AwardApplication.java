package com.hhly.award;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
@Configuration
@MapperScan("com.hhly.award.persistence.dao")
@ImportResource("classpath:transaction.xml")
public class AwardApplication {
	public static void main(String[] args) {
        SpringApplication.run(AwardApplication.class, args);
    }
}
