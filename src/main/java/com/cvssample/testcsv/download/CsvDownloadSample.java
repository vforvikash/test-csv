package com.cvssample.testcsv.download;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;


@RestController
@RequestMapping("/sample")
public class CsvDownloadSample {

    public static final MediaType CSV_MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

    @Autowired private CsvDataRepository repository;

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World!";
    }

    @GetMapping(value = "/download",
        produces = "text/csv;charset=utf-8")
    public Flux<String> getCsvFile() {
        return Flux.just(new CsvData("Number", "Name"))
                .concatWith(repository.getCsvData())
                .map(csvData -> csvData.csvFormat())
                .delayElements(Duration.ofMillis(1500));
    }
}

@Service
class CsvDataRepository {
    private List<CsvData> all = List.of(CsvData.vikash, CsvData.jash);

    public Flux<CsvData> getCsvData() { return Flux.fromIterable(all); }
}

@Data
@AllArgsConstructor
class CsvData {
    public static final CsvData vikash = new CsvData("1", "Vikash");
    public static final CsvData jash = new CsvData("2", "Jash");

    private String code;
    private String name;

    public String csvFormat() {
        return this.code + "," + this.name + "\n";
    }
}