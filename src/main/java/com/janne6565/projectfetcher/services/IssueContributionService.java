package com.janne6565.projectfetcher.services;

import com.janne6565.projectfetcher.dtos.ContributionEvent;
import com.janne6565.projectfetcher.dtos.ContributionType;
import com.janne6565.projectfetcher.dtos.GitHubQueries;
import com.janne6565.projectfetcher.dtos.IssueResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class IssueContributionService {

    private final GitHubGraphQLClient client;

    public IssueContributionService(GitHubGraphQLClient client) {
        this.client = client;
    }

    public List<ContributionEvent> fetch(
            String username,
            Instant from,
            Instant to
    ) {
        List<ContributionEvent> result = new ArrayList<>();
        String after = null;

        IssueResponse.IssueContributions ic;
        do {
            IssueResponse response =
                    client.execute(
                            GitHubQueries.ISSUE_CONTRIBUTIONS,
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
                            IssueResponse.class
                    );

            ic = response.data()
                    .user()
                    .contributionsCollection()
                    .issueContributions();

            for (IssueResponse.Node issue : ic.nodes()) {
                result.add(new ContributionEvent(
                        issue.occurredAt().atZone(ZoneOffset.UTC).toLocalDate(),
                        ContributionType.ISSUE,
                        issue.issue().repository().url()
                ));
            }

            after = ic.pageInfo().endCursor();
        } while (ic.pageInfo().hasNextPage());

        return result;
    }
}

