package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT (s.ip))" +
            "FROM Stat AS s " +
            "WHERE s.timeStamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip)DESC ")
    Collection<ViewStatsDto> findAllStatsByStartAndEndTime(LocalDateTime star, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT (DISTINCT s.ip))" +
            "FROM Stat AS s " +
            "WHERE s.timeStamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip)DESC ")
    Collection<ViewStatsDto> findAllStatsByTimeAndUniqueIp(LocalDateTime star, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT (DISTINCT s.ip))" +
            "FROM Stat AS s " +
            "WHERE s.timeStamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip)DESC ")
    Collection<ViewStatsDto> findAllStatsByTimeAndIpAndListOfUris(LocalDateTime star, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ViewStatsDto(s.app, s.uri, COUNT (s.ip))" +
            "FROM Stat AS s " +
            "WHERE s.timeStamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip)DESC ")
    Collection<ViewStatsDto> findAllStatsByTimeAndListOfUris(LocalDateTime star, LocalDateTime end, List<String> uris);
}
