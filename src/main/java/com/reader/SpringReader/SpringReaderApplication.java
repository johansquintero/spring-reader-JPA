package com.reader.SpringReader;

import com.monitorjbl.xlsx.StreamingReader;
import com.reader.SpringReader.persistence.entites.CustomerEntity;
import com.reader.SpringReader.persistence.entites.repositories.ICustomerCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
@Slf4j
public class SpringReaderApplication {
    @Autowired
    private ICustomerCrudRepository crudRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringReaderApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            long startTimeReading = System.currentTimeMillis();
            log.info("--> Starting reading process...");
            InputStream is = new FileInputStream("../customers.xlsx");

            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(50000)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(65536)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is);

            List<CustomerEntity> customerEntityList = StreamSupport.stream(workbook.spliterator(), false)
                    .flatMap(sheet -> StreamSupport.stream(sheet.spliterator(), false)
                            .skip(1)
                            .map(row -> {
                                CustomerEntity customerEntity = new CustomerEntity();
                                customerEntity.setId((long) row.getCell(0).getNumericCellValue());
                                customerEntity.setName(row.getCell(1).getStringCellValue());
                                customerEntity.setLastName(row.getCell(2).getStringCellValue());
                                customerEntity.setAddress(row.getCell(3).getStringCellValue());
                                customerEntity.setEmail(row.getCell (4).getStringCellValue());
                                return customerEntity;
                            }))
                    .collect(Collectors.toList());


            long endTimeReading = System.currentTimeMillis();
            log.info(String.format(" ---> Finished reading process, total time taken was %s ms", (endTimeReading - startTimeReading)));

            log.info("--> Starting writing process....");
            long startTimeWriting = System.currentTimeMillis();
            this.crudRepository.saveAll(customerEntityList);
            long endTimeWriting = System.currentTimeMillis();

            log.info(String.format(" ---> Finished writing process, total time taken was %s ms", (endTimeWriting - startTimeWriting)));
        };
    }
}
