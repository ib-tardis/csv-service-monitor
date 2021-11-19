package com.assignment.monitorservice.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class HealthStatus {
    @CsvBindByName(column = "Name")
    public String name;

    @CsvBindByName(column = "URL")
    public String url;

    public String status="Failed";
    public int httpCode=0;
    public String responseMessage="";
    public Date date = new Date();
    public int errorCount=0;
    public int runCount=0;

    public HealthStatus(String name, String url) {
        this.name = name;
        this.url = url;
        this.status = status;
        this.httpCode = httpCode;
        this.responseMessage = responseMessage;
        this.date = date;
        this.errorCount = 0;
        this.runCount = 0;
    }

    public HealthStatus() {}
}
