package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.util.List;

public class CompMapper {

    public static Compilation mapToCompilation(NewCompilationDto newDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newDto.getPinned());
        compilation.setTitle(newDto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto mapToDto(Compilation compilation, List<EventShortDto> listEventDto) {
        CompilationDto newDto = new CompilationDto();
        newDto.setId(compilation.getId());
        newDto.setPinned(compilation.isPinned());
        newDto.setTitle(compilation.getTitle());
        newDto.setEvents(listEventDto);
        return newDto;
    }

    public static Compilation mapUpdateComp(UpdateCompilationRequest upComp, Compilation comp) {
        if (upComp.getPinned() != null) {
            comp.setPinned(upComp.getPinned());
        }
        if (upComp.getTitle() != null) {
            comp.setTitle(upComp.getTitle());
        }
        return comp;
    }

}
