package com.janne6565.projectfetcher.dtos;

public final class GitHubQueries {

    private GitHubQueries() {
    }

    public static final String PULL_REQUEST_CONTRIBUTIONS = """
            query ($username: String!, $from: DateTime!, $to: DateTime!, $after: String) {
              user(login: $username) {
                contributionsCollection(from: $from, to: $to) {
                  pullRequestContributions(first: 100, after: $after) {
                    nodes {
                      occurredAt
                      pullRequest {
                        repository {
                          url
                        }
                        url
                      }
                    }
                    pageInfo {
                      hasNextPage
                      endCursor
                    }
                  }
                }
              }
            }
            """;

    public static final String ISSUE_CONTRIBUTIONS = """
        query ($username: String!, $from: DateTime!, $to: DateTime!, $after: String) {
          user(login: $username) {
            contributionsCollection(from: $from, to: $to) {
              issueContributions(first: 100, after: $after) {
                nodes {
                  occurredAt
                  issue {
                    repository {
                      url
                    }
                    url
                  }
                }
                pageInfo {
                  hasNextPage
                  endCursor
                }
              }
            }
          }
        }
        """;

    public static final String COMMIT_INITIAL = """
        query ($username: String!, $from: DateTime!, $to: DateTime!) {
          user(login: $username) {
            contributionsCollection(from: $from, to: $to) {
              commitContributionsByRepository {
                repository {
                  url
                }
                contributions(first: 100) {
                  nodes {
                    occurredAt
                    commitCount
                    url
                  }
                  pageInfo {
                    hasNextPage
                    endCursor
                  }
                }
              }
            }
          }
        }
        """;

    public static final String COMMIT_PAGE = """
        query ($username: String!, $from: DateTime!, $to: DateTime!, $after: String) {
          user(login: $username) {
            contributionsCollection(from: $from, to: $to) {
              commitContributionsByRepository {
                repository {
                  url
                }
                contributions(first: 100, after: $after) {
                  nodes {
                    occurredAt
                    commitCount
                    url
                  }
                  pageInfo {
                    hasNextPage
                    endCursor
                  }
                }
              }
            }
          }
        }
        """;

}

