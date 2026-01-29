package com.janne6565.projectfetcher.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class GitHubGraphQLClient {
    private final WebClient webClient;

    public GitHubGraphQLClient(
            @Value("${github.token}") String token
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com/graphql")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

    public <T> T execute(String query, Map<String, Object> variables, Class<T> responseType) {
        return webClient.post()
                .bodyValue(Map.of("query", query, "variables", variables))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}
