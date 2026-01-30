package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.ContributionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubContributionService {

    private final PullRequestContributionService prs;
    private final IssueContributionService issues;
    private final CommitContributionService commits;
    private final ContributionAggregationService aggregator;
    @Value("${github.username}")
    private String username;
    private Map<LocalDate, List<ContributionEvent>> cached;

    public Map<LocalDate, List<ContributionEvent>> getCachedContributions() {
        if (cached == null) {
            return refresh();
        }
        return cached;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 10)
    public Map<LocalDate, List<ContributionEvent>> refresh() {
        log.info("Refreshing contributions cache");
        List<Integer> lastFiveYears = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lastFiveYears.add(LocalDate.now().minusYears(i).getYear());
        }
        Map<LocalDate, List<ContributionEvent>> newCache = new HashMap<>();
        for (int year : lastFiveYears) {
            newCache.putAll(fetchYear(year));
        }
        cached = newCache;
        log.info("Cache refreshed");
        return cached;
    }

    private Map<LocalDate, List<ContributionEvent>> fetchYear(
            int year
    ) {
        Instant from = LocalDate.of(year, 1, 1)
                .atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = LocalDate.of(year, 12, 31)
                .atTime(23, 59, 59)
                .toInstant(ZoneOffset.UTC);

        List<ContributionEvent> all = new ArrayList<>();
        all.addAll(prs.fetch(username, from, to));
        all.addAll(issues.fetch(username, from, to));
        all.addAll(commits.fetch(username, from, to));

        return aggregator.aggregate(
                all,
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        );
    }
}
