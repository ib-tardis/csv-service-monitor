package com.assignment.monitorservice.util;

import com.assignment.monitorservice.model.HealthStatus;
import com.assignment.monitorservice.persistence.SharedData;
import com.assignment.monitorservice.storage.StatusStorage;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvFileReader {

    public CsvFileReader(String directory, String filename){

        String currentDirStr = Paths.get(".").toAbsolutePath().normalize().toString();

        try (Reader reader = Files.newBufferedReader(Paths.get(currentDirStr+"/"+directory+"/"+filename))) {

            // create csv bean reader
            CsvToBean<HealthStatus> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(HealthStatus.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // convert `CsvToBean` object to list of users
            List<HealthStatus> allUrls = csvToBean.parse();
            SharedData.getInstance().loadURLs(allUrls);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
