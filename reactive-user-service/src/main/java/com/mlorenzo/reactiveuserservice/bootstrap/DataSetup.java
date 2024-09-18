package com.mlorenzo.reactiveuserservice.bootstrap;


import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class DataSetup implements CommandLineRunner {

	@Value("classpath:h2/init.sql")
	private Resource initSql;
	
	@Autowired
	private R2dbcEntityTemplate entityTemplate;
	
	@Override
	public void run(String... args) throws Exception {
		String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);
		System.out.println(query);
		
		entityTemplate
			.getDatabaseClient()
			.sql(query)
			.then()
			.subscribe();
	}

}
