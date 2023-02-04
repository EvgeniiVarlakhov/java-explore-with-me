package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsDtoIn;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.InvalidValidationException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public void createStat(StatsDtoIn statsDtoIn) {
        Stat newStat = statsRepository.save(StatsMapper.mapToNewStat(statsDtoIn));
        log.info("Записаны данные статистики = {}", newStat);
    }

    @Override
    public Collection<ViewStatsDto> getStatsWithParams(
            String start, String end, ArrayList<String> uris, String unique) {
        LocalDateTime startTime = validateTimeFormat(start);
        LocalDateTime endTime = validateTimeFormat(end);
        validateUniqueParam(unique);
        Collection<ViewStatsDto> viewStatsDtos;
        if (unique.equals("true") & uris != null) {
            viewStatsDtos = statsRepository.findAllStatsByTimeAndIpAndListOfUris(startTime, endTime, uris);
        } else if (unique.equals("false") & uris != null) {
            viewStatsDtos = statsRepository.findAllStatsByTimeAndListOfUris(startTime, endTime, uris);
        } else if (unique.equals("true")) {
            viewStatsDtos = statsRepository.findAllStatsByTimeAndUniqueIp(startTime, endTime);
        } else {
            viewStatsDtos = statsRepository.findAllStatsByStartAndEndTime(startTime, endTime);
        }
        log.info("Статистика собрана:{}", viewStatsDtos);
        return viewStatsDtos;
    }

    private LocalDateTime validateTimeFormat(String time) {
        LocalDateTime checkTime;
        try {
            checkTime = LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException ex) {
            throw new InvalidValidationException("Время должно быть указано в формате \"yyyy-MM-dd HH:mm:ss\".");
        }
        return checkTime;
    }

    private void validateUniqueParam(String param) {
        switch (param) {
            case "true":
                break;
            case "false":
                break;
            default:
                throw new InvalidValidationException("Значение параметра unique должно быть true/false.");
        }
    }

}
