package ru.practicum.event.comparator;

import ru.practicum.event.dto.EventShortDto;

import java.util.Comparator;

public class EventViewComparator implements Comparator<EventShortDto> {

    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        return o1.getViews() - o2.getViews();
    }

}
