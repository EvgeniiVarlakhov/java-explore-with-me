package ru.practicum.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Integer> requestIds;

    @NotBlank
    String status;
}
