package com.janne6565.projectfetcher.dtos;

public final class GitHubQueries {

    private GitHubQueries() {
    }

    public static final String CONTRIBUTIONS = """
            query ($username: String!, $from: DateTime!, $to: DateTime!) {
              user(login: $username) {
                contributionsCollection(from: $from, to: $to) {
                  contributionCalendar {
                    totalContributions
                    weeks {
                      contributionDays {
                        date
                        contributionCount
                      }
                    }
                  }
                  commitContributionsByRepository {
                    repository { url, name }
                    contributions { totalCount }
                  }
                  issueContributionsByRepository {
                    repository { url, name }
                    contributions { totalCount }
                  }
                  pullRequestContributionsByRepository {
                    repository { url, name }
                    contributions { totalCount }
                  }
                  pullRequestReviewContributionsByRepository {
                    repository { url, name }
                    contributions { totalCount }
                  }
                  totalCommitContributions
                  totalIssueContributions
                  totalPullRequestContributions
                  totalPullRequestReviewContributions
                }
              }
            }
            """;
}
