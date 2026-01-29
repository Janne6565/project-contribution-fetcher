package com.janne6565.projectfetcher.dtos;

import java.net.URI;
import java.time.Instant;
import java.util.List;

public record IssueResponse(
        Data data
) {
    public record Data(
            User user
    ) {
    }

    public record User(
            ContributionsCollection contributionsCollection
    ) {
    }

    public record ContributionsCollection(
            IssueContributions issueContributions
    ) {
    }

    public record IssueContributions(
            List<Node> nodes,
            PageInfo pageInfo
    ) {
    }

    public record Node(
            Instant occurredAt,
            Issue issue
    ) {
    }

    public record Issue(
            Repository repository,
            URI url
    ) {
    }

    public record Repository(
            URI url
    ) {}
}

