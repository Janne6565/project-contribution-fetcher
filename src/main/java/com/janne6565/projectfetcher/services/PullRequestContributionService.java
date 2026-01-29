package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PullRequestContributionService {
    private final GitHubGraphQLClient client;

    public List<ContributionEvent> fetch(
            String username,
            Instant from,
            Instant to
    ) {
        List<ContributionEvent> result = new ArrayList<>();
        String after = null;

        PullRequestContributions page;
        do {
            PullRequestResponse response = client.execute(
                    GitHubQueries.PULL_REQUEST_CONTRIBUTIONS,
                    after == null ?
                            Map.of(
                                    "username", username,
                                    "from", from,
                                    "to", to
                            ) :
                            Map.of(
                                    "username", username,
                                    "from", from,
                                    "to", to,
                                    "after", after
                            ),
                    PullRequestResponse.class
            );

            page = response.data().user()
                    .contributionsCollection()
                    .pullRequestContributions();

            for (PullRequestContributions.Node pr : page.nodes()) {
                result.add(new ContributionEvent(
                        pr.occurredAt().atZone(ZoneOffset.UTC).toLocalDate(),
                        ContributionType.PULL_REQUEST,
                        pr.pullRequest().repository().url(),
                        pr.pullRequest().url()
                ));
            }

            after = page.pageInfo().endCursor();
        } while (page.pageInfo().hasNextPage());

        return result;
    }
}
