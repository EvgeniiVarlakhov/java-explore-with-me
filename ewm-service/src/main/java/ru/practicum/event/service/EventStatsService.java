package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.constant.MainConstant;
import ru.practicum.controller.ClientController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStatsService {
    private final ClientController clientController;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    public int getStatViewEvents(Integer eventId) {
        ArrayList<String> uris = new ArrayList<>();
        uris.add(MainConstant.URI_EVENT + eventId);
        List<ViewStatsDto> list = new ArrayList<>();
        ResponseEntity<Object> response = clientController.getList(
                LocalDateTime.now().minusDays(100).format(MainConstant.FORMATTER),
                LocalDateTime.now().format(MainConstant.FORMATTER),
                uris,
                "false"
        );
        Object body = response.getBody();
        String json = gson.toJson(body);
        if (body != null) {
            TypeReference<List<ViewStatsDto>> typeRef = new TypeReference<>() {
            };
            try {
                list = objectMapper.readValue(json, typeRef);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if (list.size() == 0) {
            return 0;
        } else {
            return list.get(0).getHits().intValue();
        }
    }

}

