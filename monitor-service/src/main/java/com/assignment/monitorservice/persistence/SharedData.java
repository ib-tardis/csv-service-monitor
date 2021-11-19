package com.assignment.monitorservice.persistence;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.storage.StatusStorage;
import com.assignment.monitorservice.util.HttpUtil;

import java.io.IOException;
import java.util.List;

public class SharedData extends PersistenceAbstract{

    private static final StatusStorage statusStorage = new StatusStorage();
    private static final StatusStorage historyStorage = new StatusStorage();
    private static SharedData instance;
    private static boolean firstThread = true;

    @Override
    public HealthStatus getStatus(String name) {
        return statusStorage.getStatusByUrl(name);
    }

    @Override
    public List<HealthStatus> getAllUrls() {
        return statusStorage.getHealthStatusList();
    }

    @Override
    public HealthStatus addUrl(String name, String url) throws IOException {
        HealthStatus healthStatus = HttpUtil.getStatus(new HealthStatus(name, url));
        statusStorage.addUrl(healthStatus);
        return healthStatus;
    }

    @Override
    public void loadURLs(List<HealthStatus> healthStatusList) {
        statusStorage.setHealthStatusList(healthStatusList);
        historyStorage.setHealthStatusList(healthStatusList);
    }

    @Override
    public void addURLToHistoryStore(HealthStatus healthStatus) {
        historyStorage.addUrl(healthStatus);
    }

    @Override
    public List<HealthStatus> getHistory() {
        return historyStorage.getHealthStatusList();
    }

    public static SharedData getInstance(){
        if (firstThread) {
            firstThread = false;

            Thread.currentThread();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (SharedData.class) {
            if (instance == null) {
                instance = new SharedData();
            }
        }

        return instance;
    }

    public static boolean isFirstThread(){
        return firstThread;
    }
}
