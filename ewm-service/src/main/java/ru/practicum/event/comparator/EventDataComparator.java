package ru.practicum.event.comparator;

import ru.practicum.constant.MainConstant;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EventDataComparator implements Comparator<EventShortDto> {

    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        int i;
        LocalDateTime dateFirst = LocalDateTime.parse(
                o1.getEventDate(),
                MainConstant.FORMATTER
        );

        LocalDateTime dateSecond = LocalDateTime.parse(
                o2.getEventDate(),
                MainConstant.FORMATTER
        );

        if (dateFirst.isBefore(dateSecond)) {
            i = 1;
        } else if (dateFirst.isEqual(dateSecond)) {
            i = 0;
        } else {
            i = -1;
        }
        return i;
    }

}
