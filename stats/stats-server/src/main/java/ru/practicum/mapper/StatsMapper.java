package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Stat mapToNewStat(StatsDtoIn statsDtoIn) {
        Stat stat = new Stat();
        stat.setApp(statsDtoIn.getApp());
        stat.setUri(statsDtoIn.getUri());
        stat.setIp(statsDtoIn.getIp());
        stat.setTimeStamp(LocalDateTime.parse(statsDtoIn.getTimestamp(), formatter));
        return stat;
    }

}
