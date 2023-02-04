package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsDtoIn;
import ru.practicum.client.Client;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class ClientController {
    private final Client client;

    @GetMapping("/stats")
    public ResponseEntity<Object> getList(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) ArrayList<String> uris,
            @RequestParam(value = "unique", required = false) String unique) {
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

    @PostMapping("/hit")
    public ResponseEntity<Object> createStat(
            @RequestBody StatsDtoIn statsDtoIn) {
        return client.createStat(statsDtoIn);
    }

}
