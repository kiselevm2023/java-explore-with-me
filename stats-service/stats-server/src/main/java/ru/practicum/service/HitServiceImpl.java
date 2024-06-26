package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.StatsHitMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.ViewStats;
import ru.practicum.repositiry.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;
    private final StatsHitMapper statsHitMapper;

    @Transactional
    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        Hit newHit = hitRepository.save(statsHitMapper.toHit(endpointHitDto));

        return statsHitMapper.toEndpointHitDto(newHit);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUniq) {
        List<ViewStats> viewStatsList;

        if (start != null && end != null && start.isAfter(end)) {
            throw new BadRequestException("Начало события не может быть позже окончания.");
        }

        if (isUniq) {
            viewStatsList = (uris == null || uris.isEmpty())
                    ? hitRepository.getViewStatsUniq(start, end)
                    : hitRepository.getViewStatsUniq(start, end, uris);
        } else {

            viewStatsList = (uris == null || uris.isEmpty())
                    ? hitRepository.getViewStatsNotUniq(start, end)
                    : hitRepository.getViewStatsNotUniq(start, end, uris);
        }

        return statsHitMapper.toListViewStatsDto(viewStatsList);
    }
}