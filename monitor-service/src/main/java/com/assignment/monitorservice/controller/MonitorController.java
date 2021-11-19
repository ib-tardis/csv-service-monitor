package com.assignment.monitorservice.controller;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping
    public List<HealthStatus> getAllUrls(@RequestParam(value = "start", required=false) String start,
                                         @RequestParam(value = "end", required=false) String end,
                                         @RequestParam(value="sortBy", required = false) String sortBy,
                                         @RequestParam(value="lastN", required = false) Integer lastN,
                                         @RequestParam(value="status", required = false) String status,
                                         WebRequest request) throws Exception {
        return  monitoringService.loadUrls(start, end, sortBy, lastN, status);
    }

    @PostMapping("/add")
    public HealthStatus addUrl(@RequestBody HealthStatus healthStatus, WebRequest request) throws Exception {
        return  monitoringService.addUrl(healthStatus.getName(), healthStatus.getUrl());
    }
}
