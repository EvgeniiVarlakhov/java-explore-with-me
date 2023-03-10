package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.CompMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompRepository;
import ru.practicum.event.EventMapper;
import ru.practicum.event.service.EventStatsService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.RequestState;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompServiceImpl implements CompService {
    private final CompRepository compRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventStatsService statsService;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Integer> eventIds = new HashSet<>(newCompilationDto.getEvents());
        List<Event> events = eventRepository.findAllByIds(eventIds);
        Compilation saveComp = compRepository.save(CompMapper.mapToCompilation(newCompilationDto, events));
        Collection<Request> confirmList = new ArrayList<>();
        List<EventShortDto> eventShortDtos = EventMapper.getListOfEventShortDto(saveComp.getEvents(), confirmList);
        log.info("Создана новая коллекция: {}", saveComp);
        return CompMapper.mapToDto(saveComp, eventShortDtos);
    }

    @Transactional
    @Override
    public void deleteCompilation(int compId) {
        Optional<Compilation> compilation = compRepository.findById(compId);
        if (compilation.isEmpty()) {
            throw new ObjectNotFoundException("Подборки с ID = " + compId + " нет.");
        }
        compRepository.deleteById(compId);
        log.info("Удалена подборка с ID = {}", compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompByAdmin(int compId, UpdateCompilationRequest updateComp) {
        Optional<Compilation> oldComp = compRepository.findById(compId);
        if (oldComp.isEmpty()) {
            throw new ObjectNotFoundException("Подборки с ID = " + compId + " не найдено.");
        }
        if (updateComp.getEvents() != null) {
            List<Event> newEvents = eventRepository.findAllByIds(updateComp.getEvents());
            oldComp.get().setEvents(newEvents);
        }
        Compilation newComp = compRepository.save(CompMapper.mapUpdateComp(updateComp, oldComp.get()));
        Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                newComp.getEvents(),
                RequestState.CONFIRMED);
        List<EventShortDto> eventShortDtos = EventMapper.getListOfEventShortDto(newComp.getEvents(), confirmList);
        log.info("Обновлена подборка ID = {}. {}", compId, newComp);
        return CompMapper.mapToDto(newComp, eventShortDtos);
    }

    @Override
    public Collection<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Compilation> comp;
        if (pinned == null) {
            comp = compRepository.findAll(pageable).getContent();
        } else {
            comp = compRepository.findAllByPinnedIs(pinned, pageable).getContent();
        }
        Collection<CompilationDto> compDtoList = new ArrayList<>();
        for (Compilation compilation : comp) {
            Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                    compilation.getEvents(),
                    RequestState.CONFIRMED);
            List<EventShortDto> eShortDtos = EventMapper.getListOfEventShortDto(compilation.getEvents(), confirmList);
            getListEventDtoWithStats(eShortDtos);
            compDtoList.add(CompMapper.mapToDto(compilation, eShortDtos));
        }
        log.info("Получен список колекций. Параметр pinned = {}", pinned);
        return compDtoList;
    }

    @Override
    public CompilationDto getCompById(int compId) {
        Optional<Compilation> compilation = compRepository.findById(compId);
        if (compilation.isEmpty()) {
            throw new ObjectNotFoundException("Подборки с ID = " + compId + " не найдено.");
        }
        Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                compilation.get().getEvents(),
                RequestState.CONFIRMED);
        List<EventShortDto> eShortDtos = EventMapper.getListOfEventShortDto(compilation.get().getEvents(), confirmList);
        for (EventShortDto dto : eShortDtos) {
            dto.setViews(statsService.getStatViewEvents(dto.getId()));
        }
        log.info("Получена колекция ID = {}.", compId);
        return CompMapper.mapToDto(compilation.get(), eShortDtos);
    }

    private void getListEventDtoWithStats(List<EventShortDto> eventsDtos) {
        for (EventShortDto dto : eventsDtos) {
            dto.setViews(statsService.getStatViewEvents(dto.getId()));
        }
    }

}
