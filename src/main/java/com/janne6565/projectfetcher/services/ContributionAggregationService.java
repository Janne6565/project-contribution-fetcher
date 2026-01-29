package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.ContributionEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ContributionAggregationService {

    public Map<LocalDate, List<ContributionEvent>> aggregate(
            Collection<ContributionEvent> events,
            LocalDate start,
            LocalDate end
    ) {
        Map<LocalDate, List<ContributionEvent>> result =
                new TreeMap<>();

        for (var event : events) {
            result
                    .computeIfAbsent(event.day(), d -> new ArrayList<>())
                    .add(event);
        }

        LocalDate d = start;
        while (!d.isAfter(end)) {
            result.putIfAbsent(d, List.of());
            d = d.plusDays(1);
        }

        return result;
    }
}
