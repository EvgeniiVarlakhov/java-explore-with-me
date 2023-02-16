package ru.practicum.service;

import ru.practicum.StatsDtoIn;
import ru.practicum.ViewStatsDto;

import java.util.ArrayList;
import java.util.List;

public interface StatsService {
    void createStat(StatsDtoIn statsDtoIn);

    List<ViewStatsDto> getStatsWithParams(String start, String end, ArrayList<String> uris, String unique);
}
