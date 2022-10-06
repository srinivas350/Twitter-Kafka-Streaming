package com.example.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TwitterConsumer {
	
	@KafkaListener(topics="twitter_topic",groupId="consumerGroup")
	public void getTwittesFromProducer(String getData)
	{
		System.out.println(getData);
	}

}
