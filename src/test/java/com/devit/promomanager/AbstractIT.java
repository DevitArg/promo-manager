package com.devit.promomanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@ActiveProfiles(value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class AbstractIT {

	@Autowired
	protected MongoTemplate mongoTemplate;
	@Autowired
	protected ObjectMapper objectMapper;
	@LocalServerPort
	int serverPort;

	@Before
	public void setUp() {
		initRestAssured();
	}

	private void initRestAssured() {
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.requestSpecification = new RequestSpecBuilder()
				.setPort(serverPort)
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)
				.build();
		RestAssured.responseSpecification = new ResponseSpecBuilder()
				.setDefaultParser(Parser.JSON)
				.build();
	}

	@After
	public void tearDown() throws Exception {
	}

}
