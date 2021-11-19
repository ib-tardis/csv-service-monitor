package com.assignment.monitorservice.persistence;

import com.assignment.monitorservice.model.HealthStatus;

import java.io.IOException;
import java.util.List;

public abstract class PersistenceAbstract {

    abstract public HealthStatus getStatus(String name);

    abstract public List<HealthStatus> getAllUrls();

    abstract public HealthStatus addUrl(String name, String url) throws IOException;

    abstract public void loadURLs(List<HealthStatus> healthStatusList);

    abstract public void addURLToHistoryStore(HealthStatus healthStatus);

    abstract public List<HealthStatus> getHistory();
}
