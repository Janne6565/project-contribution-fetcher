package com.janne6565.projectfetcher.dtos;


public record PullRequestResponse(
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
            PullRequestContributions pullRequestContributions
    ) {
    }
}

