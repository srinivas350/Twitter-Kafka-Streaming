package com.example.producer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;  

@Service
public class TwitterProducer {
	
		@Value("${twitter.consumerKey}")
	 	String consumerKey ;
		@Value("${twitter.consumerSecret}")
	    String consumerSecret ;  
		@Value("${twitter.token}")
	    String token ;  
		@Value("${twitter.secret}")
	    String secret ;  
		@Value("${STREAM_HOST}")
		String stream_host;
	  
	  
	    
	    public void build() {  

	        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000)  ;
	        Client client = tweetclient(msgQueue);  
	        client.connect();
	        KafkaProducer<String,String> producer=createKafkaProducer();  
	  
	        while (!client.isDone()) {  
	            String msg = null;  
	            try {  
	                msg = msgQueue.poll(5, TimeUnit.SECONDS);
	            } catch (InterruptedException e) {  
	                e.printStackTrace();  
	                client.stop();  
	            }  
	            if (msg != null) {    
	                producer.send(new ProducerRecord<>("twitter_topic", msg));
	            }
	        }   
	    }  
	  
	    public Client tweetclient(BlockingQueue<String> msgQueue) {  
	  
	        Hosts hosebirdHosts = new HttpHosts(stream_host);  
	        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();  
	        List<String> terms = Lists.newArrayList("Army");
	        hosebirdEndpoint.trackTerms(terms);  
	        Authentication hosebirdAuth = new OAuth1(consumerKey,consumerSecret,token,secret);  
	        ClientBuilder builder = new ClientBuilder()  
	                .name("Hosebird-Client-01")
	                .hosts(hosebirdHosts)  
	                .authentication(hosebirdAuth)  
	                .endpoint(hosebirdEndpoint)  
	                .processor(new StringDelimitedProcessor(msgQueue));  
	  
	  
	        Client hosebirdClient = builder.build();  
	        return hosebirdClient;
	 }  
	    public KafkaProducer<String,String> createKafkaProducer(){      
	        String bootstrapServers="127.0.0.1:9092";  
	        Properties properties= new Properties();  
	        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,    bootstrapServers);  
	        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());  
	        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());  
	  
	        KafkaProducer<String,String> first_producer = new KafkaProducer<String, String>(properties);  
	        return first_producer;  
	  
	    }  

}
