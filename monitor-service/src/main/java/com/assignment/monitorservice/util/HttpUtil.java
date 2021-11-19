package com.assignment.monitorservice.util;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.persistence.SharedData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    public static HealthStatus getStatus(HealthStatus healthStatus) throws IOException {

        Date oneHourBack = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));

        URL url = new URL(healthStatus.getUrl());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();

        int code = connection.getResponseCode();

        healthStatus.setHttpCode(code);
        healthStatus.setStatus(code==200?"SUCCESS":"FAILED");
        healthStatus.setResponseMessage(connection.getResponseMessage());
        healthStatus.setErrorCount(code==200?healthStatus.getErrorCount():healthStatus.getErrorCount()+1);
        healthStatus.setRunCount(healthStatus.getRunCount()+1);

        if((!oneHourBack.after(healthStatus.getDate()) && healthStatus.getDate().before(new Date())) && healthStatus.getErrorCount() >= 3){
            System.out.println("ALERT: "+healthStatus.getUrl()+"id failed for more than 3 times...");
        }

//        SharedData.getInstance().addURLToHistoryStore(healthStatus);

        return healthStatus;
    }
}
