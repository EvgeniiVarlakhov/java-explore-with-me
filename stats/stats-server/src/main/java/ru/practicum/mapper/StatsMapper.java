package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.Constant;
import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {

    public static Stat mapToNewStat(StatsDtoIn statsDtoIn) {
        Stat stat = new Stat();
        stat.setApp(statsDtoIn.getApp());
        stat.setUri(statsDtoIn.getUri());
        stat.setIp(statsDtoIn.getIp());
        stat.setTimeStamp(
                LocalDateTime.parse(statsDtoIn.getTimestamp(), DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        return stat;
    }

}