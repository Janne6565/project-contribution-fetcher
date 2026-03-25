package com.janne6565.projectfetcher.dtos;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ContributionSummary(
        Map<LocalDate, Integer> calendar,
        List<RepositoryContribution> repositories,
        ContributionTotals totals
) {
    public record RepositoryContribution(
            URI url,
            String name,
            int commits,
            int pullRequests,
            int issues,
            int reviews
    ) {}

    public record ContributionTotals(
            int commits,
            int pullRequests,
            int issues,
            int reviews
    ) {}
}
