package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsDtoIn;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStatsWithParams(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) ArrayList<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") String unique) {
        return statsService.getStatsWithParams(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody StatsDtoIn statsDtoIn) {
        statsService.createStat(statsDtoIn);
    }

}