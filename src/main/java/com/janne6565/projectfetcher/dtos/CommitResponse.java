package com.janne6565.projectfetcher.dtos;

import java.net.URI;
import java.time.Instant;
import java.util.List;

public record CommitResponse(
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
            List<CommitRepo> commitContributionsByRepository
    ) {
    }

    public record CommitRepo(
            Repository repository,
            Contributions contributions
    ) {
    }

    public record Contributions(
            List<Node> nodes,
            PageInfo pageInfo
    ) {
    }

    public record Node(
            Instant occurredAt,
            int commitCount
    ) {
    }

    public record Repository(
            URI url,
            String name,
            Owner owner
    ) {
    }

    public record Owner(
            String login
    ) {
    }

    public record PageInfo(
            boolean hasNextPage,
            String endCursor
    ) {
    }
}

