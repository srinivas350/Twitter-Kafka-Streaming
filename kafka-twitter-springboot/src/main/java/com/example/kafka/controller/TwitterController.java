package com.example.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer.TwitterProducer;

@RestController
@RequestMapping("/twitter")
public class TwitterController {
	
	@Autowired
	TwitterProducer producer;
	
	@GetMapping("/getData")
	public String getData()
	{
		producer.build();
		return "Data Returned";
	}

}
