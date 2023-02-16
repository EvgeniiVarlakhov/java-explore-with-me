package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.StatsDtoIn;
import ru.practicum.client.Client;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class ClientController {
    private final Client client;

    public ResponseEntity<Object> getList(String start, String end, ArrayList<String> uris, String unique) {
        if (uris != null & unique != null) {
            return client.getStatsListWithAllParam(start, end, uris, unique);
        } else if (uris == null & unique != null) {
            return client.getStatsListWithUnique(start, end, unique);
        } else if (uris != null) {
            return client.getStatsListWithUris(start, end, uris);
        } else {
            return client.getStatsListWithoutParam(start, end);
        }
    }

    public ResponseEntity<Object> createStat(StatsDtoIn statsDtoIn) {
        return client.createStat(statsDtoIn);
    }
}
