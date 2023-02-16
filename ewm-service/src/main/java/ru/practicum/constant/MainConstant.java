package ru.practicum.constant;

import java.time.format.DateTimeFormatter;

public class MainConstant {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    public static final String URI_EVENT = "/event/";
}
