package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.CommitResponse;
import com.janne6565.projectfetcher.dtos.ContributionEvent;
import com.janne6565.projectfetcher.dtos.ContributionType;
import com.janne6565.projectfetcher.dtos.GitHubQueries;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommitContributionService {

    private final GitHubGraphQLClient client;

    public CommitContributionService(GitHubGraphQLClient client) {
        this.client = client;
    }

    public List<ContributionEvent> fetch(
            String username,
            Instant from,
            Instant to
    ) {
        List<ContributionEvent> result = new ArrayList<>();

        CommitResponse initial =
                client.execute(
                        GitHubQueries.COMMIT_INITIAL,
                        Map.of(
                                "username", username,
                                "from", from,
                                "to", to
                        ),
                        CommitResponse.class
                );

        var repos = initial.data()
                .user()
                .contributionsCollection()
                .commitContributionsByRepository();

        for (var repo : repos) {
            URI repoUrl = repo.repository().url();
            var contributions = repo.contributions();

            // First page
            addAll(result, contributions.nodes(), repoUrl);

            String after = contributions.pageInfo().endCursor();

            // Additional pages
            while (contributions.pageInfo().hasNextPage()) {
                CommitResponse page =
                        client.execute(
                                GitHubQueries.COMMIT_PAGE,
                                Map.of(
                                        "username", username,
                                        "from", from,
                                        "to", to,
                                        "after", after
                                ),
                                CommitResponse.class
                        );

                var currentRepo = page.data()
                        .user()
                        .contributionsCollection()
                        .commitContributionsByRepository()
                        .stream()
                        .filter(r -> r.repository().url().equals(repoUrl))
                        .findFirst()
                        .orElseThrow();

                contributions = currentRepo.contributions();

                addAll(result, contributions.nodes(), repoUrl);
                after = contributions.pageInfo().endCursor();
            }
        }

        return result;
    }

    private void addAll(
            List<ContributionEvent> target,
            List<CommitResponse.Node> nodes,
            URI repoUrl
    ) {
        for (var c : nodes) {
            target.add(new ContributionEvent(
                    c.occurredAt().atZone(ZoneOffset.UTC).toLocalDate(),
                    ContributionType.COMMIT,
                    repoUrl,
                    c.url()
            ));
        }
    }
}


