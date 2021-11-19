package com.assignment.monitorservice.storage;

import com.assignment.monitorservice.model.HealthStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatusStorage {
    @Getter
    @Setter
    List<HealthStatus> healthStatusList;

    public StatusStorage(List<HealthStatus> healthStatusList){
        this.healthStatusList=healthStatusList;
    }

    public StatusStorage() {}

    public void addUrl(HealthStatus healthStatus){
        this.healthStatusList.add(healthStatus);
    }

    public HealthStatus getStatusByUrl(String name){
        OptionalInt index = IntStream.range(0, this.healthStatusList.size())
                .filter(i -> this.healthStatusList.get(i).getName().equals(name))
                .findFirst();
        return this.healthStatusList.get(index.getAsInt());
    }
}
