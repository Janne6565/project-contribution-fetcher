package com.janne6565.projectfetcher.controller;

import com.janne6565.projectfetcher.dtos.ContributionSummary;
import com.janne6565.projectfetcher.services.GitHubContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contributions")
public class ContributionController {

    private final GitHubContributionService service;

    @GetMapping
    public ContributionSummary get() {
        return service.getCachedContributions();
    }
}
