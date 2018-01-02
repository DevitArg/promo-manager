package com.devit.promomanager;


import com.devit.promomanager.api.config.ObjectMapperConfiguration;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@ActiveProfiles(value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class AbstractIT {

	private final static String KAFKA_TOPIC = "promo_available";

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, KAFKA_TOPIC);
	protected static BlockingQueue<ConsumerRecord<String, PromoDocument>> records;

	@Autowired
	protected MongoTemplate mongoTemplate;
	@Autowired
	protected ObjectMapper objectMapper;
	@LocalServerPort
	int serverPort;

	@BeforeClass
	public static void setUpKafka() throws Exception {
		System.setProperty("spring.kafka.bootstrapServers", embeddedKafka.getBrokersAsString());
		startKafkaListenerContainer();
	}

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

	private static void startKafkaListenerContainer() throws Exception {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testConsumer", "false", embeddedKafka);
		consumerProps.put("auto.offset.reset", "earliest");
		DefaultKafkaConsumerFactory<String, PromoDocument> cf =
				new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(),
						new JsonDeserializer<>(PromoDocument.class, ObjectMapperConfiguration.getObjectMapper()));
		ContainerProperties containerProps = new ContainerProperties(KAFKA_TOPIC);

		KafkaMessageListenerContainer<String, PromoDocument> container =
				new KafkaMessageListenerContainer<>(cf, containerProps);

		records = new LinkedBlockingQueue<>();

		container.setupMessageListener((MessageListener<String, PromoDocument>) records::add);

		container.setBeanName("queueTests");
		container.start();
		ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
	}

	@After
	public void tearDown() throws Exception {
		records.clear();
	}

}
