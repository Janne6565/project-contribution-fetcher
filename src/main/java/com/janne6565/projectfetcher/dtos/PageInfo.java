package com.janne6565.projectfetcher.dtos;

public record PageInfo(
        boolean hasNextPage,
        String endCursor
) {
}

