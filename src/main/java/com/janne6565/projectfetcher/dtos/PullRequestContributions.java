package com.janne6565.projectfetcher.dtos;

import java.time.Instant;
import java.net.URI;
import java.util.List;

public record PullRequestContributions(
        List<Node> nodes,
        PageInfo pageInfo
) {
    public record Node(
            Instant occurredAt,
            PullRequest pullRequest
    ) {
    }

    public record PullRequest(
            Repository repository
    ) {
    }

    public record Repository(
            URI url
    ) {
    }
}

