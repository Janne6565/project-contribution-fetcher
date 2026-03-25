package com.janne6565.projectfetcher.config;

import com.janne6565.projectfetcher.dtos.ContributionCalendarResponse;
import com.janne6565.projectfetcher.dtos.ContributionSummary;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(NativeRuntimeHintsConfig.RecordHintsRegistrar.class)
public class NativeRuntimeHintsConfig {

    static class RecordHintsRegistrar implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            MemberCategory[] cats = {
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.ACCESS_DECLARED_FIELDS
            };

            // GraphQL response DTOs
            hints.reflection()
                    .registerType(ContributionCalendarResponse.class, cats)
                    .registerType(ContributionCalendarResponse.Data.class, cats)
                    .registerType(ContributionCalendarResponse.User.class, cats)
                    .registerType(ContributionCalendarResponse.ContributionsCollection.class, cats)
                    .registerType(ContributionCalendarResponse.ContributionCalendar.class, cats)
                    .registerType(ContributionCalendarResponse.Week.class, cats)
                    .registerType(ContributionCalendarResponse.ContributionDay.class, cats)
                    .registerType(ContributionCalendarResponse.RepoContribution.class, cats)
                    .registerType(ContributionCalendarResponse.Repository.class, cats)
                    .registerType(ContributionCalendarResponse.Contributions.class, cats)
                    // API response DTOs
                    .registerType(ContributionSummary.class, cats)
                    .registerType(ContributionSummary.RepositoryContribution.class, cats)
                    .registerType(ContributionSummary.ContributionTotals.class, cats);
        }
    }
}
