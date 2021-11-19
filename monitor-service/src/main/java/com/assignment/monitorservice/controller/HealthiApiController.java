package com.assignment.monitorservice.controller;

import com.assignment.monitorservice.util.ConfigProperties;
import com.assignment.monitorservice.util.CsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthiApiController {

	@GetMapping
    public ResponseEntity<String> getHealthAPI() {
		return new ResponseEntity<>("{}",HttpStatus.OK);
	}
	 

}
