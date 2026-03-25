package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.ContributionCalendarResponse;
import com.janne6565.projectfetcher.dtos.ContributionCalendarResponse.ContributionsCollection;
import com.janne6565.projectfetcher.dtos.ContributionCalendarResponse.RepoContribution;
import com.janne6565.projectfetcher.dtos.ContributionSummary;
import com.janne6565.projectfetcher.dtos.ContributionSummary.ContributionTotals;
import com.janne6565.projectfetcher.dtos.ContributionSummary.RepositoryContribution;
import com.janne6565.projectfetcher.dtos.GitHubQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubContributionService {

    private final GitHubGraphQLClient client;
    @Value("${github.username}")
    private String username;
    private ContributionSummary cached;
    @Value("${github.years-into-past:5}")
    private int YEARS_INTO_PAST;

    public ContributionSummary getCachedContributions() {
        if (cached == null) {
            return refresh();
        }
        return cached;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 10)
    public ContributionSummary refresh() {
        log.info("Refreshing contributions cache");

        Map<LocalDate, Integer> calendar = new TreeMap<>();
        Map<URI, RepoAccumulator> repoMap = new LinkedHashMap<>();
        int totalCommits = 0, totalPRs = 0, totalIssues = 0, totalReviews = 0;

        for (int i = 0; i < YEARS_INTO_PAST; i++) {
            int year = LocalDate.now().minusYears(i).getYear();
            ContributionsCollection cc = fetchYear(year);
            if (cc == null) continue;

            for (var week : cc.contributionCalendar().weeks()) {
                for (var day : week.contributionDays()) {
                    LocalDate date = LocalDate.parse(day.date());
                    calendar.merge(date, day.contributionCount(), Integer::sum);
                }
            }

            for (var repo : cc.commitContributionsByRepository()) {
                accumulator(repoMap, repo).commits += repo.contributions().totalCount();
            }
            for (var repo : cc.pullRequestContributionsByRepository()) {
                accumulator(repoMap, repo).pullRequests += repo.contributions().totalCount();
            }
            for (var repo : cc.issueContributionsByRepository()) {
                accumulator(repoMap, repo).issues += repo.contributions().totalCount();
            }
            for (var repo : cc.pullRequestReviewContributionsByRepository()) {
                accumulator(repoMap, repo).reviews += repo.contributions().totalCount();
            }

            totalCommits += cc.totalCommitContributions();
            totalPRs += cc.totalPullRequestContributions();
            totalIssues += cc.totalIssueContributions();
            totalReviews += cc.totalPullRequestReviewContributions();
        }

        List<RepositoryContribution> repositories = repoMap.values().stream()
                .map(a -> new RepositoryContribution(
                        a.url, a.name,
                        a.commits, a.pullRequests, a.issues, a.reviews
                ))
                .toList();

        cached = new ContributionSummary(
                calendar,
                repositories,
                new ContributionTotals(totalCommits, totalPRs, totalIssues, totalReviews)
        );

        log.info("Cache refreshed");
        return cached;
    }

    private ContributionsCollection fetchYear(int year) {
        Instant from = LocalDate.of(year, 1, 1)
                .atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = LocalDate.of(year, 12, 31)
                .atTime(23, 59, 59)
                .toInstant(ZoneOffset.UTC);

        ContributionCalendarResponse response = client.execute(
                GitHubQueries.CONTRIBUTIONS,
                Map.of("username", username, "from", from, "to", to),
                ContributionCalendarResponse.class
        );

        if (response == null || response.data() == null || response.data().user() == null) {
            return null;
        }
        return response.data().user().contributionsCollection();
    }

    private static RepoAccumulator accumulator(Map<URI, RepoAccumulator> map, RepoContribution repo) {
        return map.computeIfAbsent(repo.repository().url(), url ->
                new RepoAccumulator(url, repo.repository().name()));
    }

    private static class RepoAccumulator {
        final URI url;
        final String name;
        int commits, pullRequests, issues, reviews;

        RepoAccumulator(URI url, String name) {
            this.url = url;
            this.name = name;
        }
    }
}
