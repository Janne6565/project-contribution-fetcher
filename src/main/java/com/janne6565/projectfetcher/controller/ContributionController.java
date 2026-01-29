package com.janne6565.projectfetcher.controller;

import com.janne6565.projectfetcher.dtos.ContributionEvent;
import com.janne6565.projectfetcher.services.GitHubContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contributions")
public class ContributionController {

    private final GitHubContributionService service;

    @GetMapping
    public Map<LocalDate, List<ContributionEvent>> get(
    ) {
        return service.getCachedContributions();
    }
}