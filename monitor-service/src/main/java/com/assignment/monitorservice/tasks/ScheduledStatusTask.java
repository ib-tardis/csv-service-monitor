package com.assignment.monitorservice.tasks;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.persistence.SharedData;
import com.assignment.monitorservice.util.AsyncIterator;
import com.assignment.monitorservice.util.ConfigProperties;
import com.assignment.monitorservice.util.CsvFileReader;
import com.assignment.monitorservice.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Component
@EnableAsync
public class ScheduledStatusTask {

    @Autowired
    ConfigProperties configProperties;

    @Async
    @Scheduled(fixedDelayString = "${config.interval.sec}000")
    public void checkStatus(){
        if(SharedData.isFirstThread()){
            CsvFileReader csvFileReader = new CsvFileReader(configProperties.getDataDir(),configProperties.getFileName());
        }
        List<HealthStatus> healthStatusList = SharedData.getInstance().getAllUrls();

        healthStatusList.parallelStream()
                .forEach(e -> {
                    try {
                        e = HttpUtil.getStatus(e);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

        System.out.println("healthStatusList.size: "+healthStatusList.size());
    }
}
