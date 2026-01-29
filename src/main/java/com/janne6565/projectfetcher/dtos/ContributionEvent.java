package com.janne6565.projectfetcher.dtos;

import java.net.URI;
import java.time.LocalDate;

public record ContributionEvent(
        LocalDate day,
        ContributionType type,
        URI repositoryUrl,
        URI reference
) {
}
