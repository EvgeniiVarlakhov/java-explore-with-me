package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatsDtoIn;

import java.util.ArrayList;
import java.util.Map;

@Service
public class Client extends BaseClient {
    private static final String API_PREFIX = "";

    @Autowired
    public Client(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStatsListWithoutParam(String startTime, String endTime) {
        Map<String, Object> parameters = Map.of(
                "start", startTime,
                "end", endTime
        );
        return get("/stats?start={start}&end={end}", parameters);
    }

    public ResponseEntity<Object> getStatsListWithUris(String startTime, String endTime, ArrayList<String> uris) {
        int i = 0;
        String uriParams = "";
        Map<String, Object> parameters = Map.of(
                "start", startTime,
                "end", endTime
        );
        while (i < uris.size()) {
            uriParams = uriParams + "&uris=" + uris.get(i);
            i++;
        }
        return get("/stats?start={start}&end={end}" + uriParams, parameters);
    }

    public ResponseEntity<Object> getStatsListWithUnique(String startTime, String endTime, String unique) {
        Map<String, Object> parameters = Map.of(
                "start", startTime,
                "end", endTime,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStatsListWithAllParam(String startTime, String endTime, ArrayList<String> uris, String unique) {
        int i = 0;
        String uriParams = "";
        Map<String, Object> parameters = Map.of(
                "start", startTime,
                "end", endTime,
                "unique", unique
        );
        while (i < uris.size()) {
            uriParams = uriParams + "&uris=" + uris.get(i);
            i++;
        }
        return get("/stats?start={start}&end={end}" + uriParams + "&unique={unique}", parameters);
    }

    public ResponseEntity<Object> createStat(StatsDtoIn body) {
        return post("/hit", body);
    }

}
