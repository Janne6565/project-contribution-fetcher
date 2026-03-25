package com.janne6565.projectfetcher.dtos;

import java.net.URI;
import java.util.List;

public record ContributionCalendarResponse(Data data) {

    public record Data(User user) {}

    public record User(ContributionsCollection contributionsCollection) {}

    public record ContributionsCollection(
            ContributionCalendar contributionCalendar,
            List<RepoContribution> commitContributionsByRepository,
            List<RepoContribution> issueContributionsByRepository,
            List<RepoContribution> pullRequestContributionsByRepository,
            List<RepoContribution> pullRequestReviewContributionsByRepository,
            int totalCommitContributions,
            int totalIssueContributions,
            int totalPullRequestContributions,
            int totalPullRequestReviewContributions
    ) {}

    public record ContributionCalendar(
            int totalContributions,
            List<Week> weeks
    ) {}

    public record Week(List<ContributionDay> contributionDays) {}

    public record ContributionDay(String date, int contributionCount) {}

    public record RepoContribution(
            Repository repository,
            Contributions contributions
    ) {}

    public record Repository(URI url, String name) {}

    public record Contributions(int totalCount) {}
}
