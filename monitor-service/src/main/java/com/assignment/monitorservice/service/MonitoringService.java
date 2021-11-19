package com.assignment.monitorservice.service;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.persistence.SharedData;
import com.assignment.monitorservice.storage.StatusStorage;
import com.assignment.monitorservice.util.ConfigProperties;
import com.assignment.monitorservice.util.CsvFileReader;
import com.assignment.monitorservice.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    public List<HealthStatus> loadUrls(String startDate, String endDate, String sortBy, Integer lastN, String status) throws Exception {
        List<HealthStatus> healthStatusList = SharedData.getInstance().getAllUrls();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(endDate);

        if(start != null){
            Collections.sort(healthStatusList, new Comparator<HealthStatus>() {
                @Override
                public int compare(HealthStatus lhs, HealthStatus rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getDate().after(start) ? -1 : rhs.getDate().after(start) ? -1 :  0;
                }
            });
        }

        if(end != null){
            Collections.sort(healthStatusList, new Comparator<HealthStatus>() {
                @Override
                public int compare(HealthStatus lhs, HealthStatus rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getDate().before(end) ? -1 :  rhs.getDate().before(end) ? -1 :  0;
                }
            });
        }

        if(sortBy != null){
            if(sortBy.equals("asc")){
                Collections.sort(healthStatusList, new Comparator<HealthStatus>() {
                    @Override
                    public int compare(HealthStatus lhs, HealthStatus rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return Integer.compare(rhs.errorCount, lhs.errorCount);
                    }
                });
            } else{
                Collections.sort(healthStatusList, new Comparator<HealthStatus>() {
                    @Override
                    public int compare(HealthStatus lhs, HealthStatus rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return Integer.compare(lhs.errorCount, rhs.errorCount);
                    }
                });
            }
        }

        if(status != null){
            healthStatusList = healthStatusList.stream()
                    .filter(h -> h.getStatus().equals(status.toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        }

        return Optional.ofNullable(healthStatusList).orElseThrow(()->new Exception("Something went wrong.."));
    }

    public HealthStatus addUrl(String name, String url) throws Exception {
        return Optional.ofNullable(SharedData.getInstance().addUrl(name, url)).orElseThrow(()->new Exception("Something went wrong.."));
    }
}
