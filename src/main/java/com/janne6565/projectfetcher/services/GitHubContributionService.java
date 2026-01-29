package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.ContributionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubContributionService {

    private final PullRequestContributionService prs;
    private final IssueContributionService issues;
    private final CommitContributionService commits;
    private final ContributionAggregationService aggregator;
    @Value("${github.username}")
    private String username;

    public Map<LocalDate, List<ContributionEvent>> fetchYear(
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
