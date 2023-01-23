package dev.migx3.core.managers;

import com.github.mattnicee7.mattlib.cooldown.CooldownMap;
import dev.migx3.core.entities.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class ReportManager {


    private final CooldownMap<Report> reportCooldownMap = CooldownMap.of(Report.class);
    private final Set<Report> reports = new HashSet<>();

    public Report findReport(UUID reporter, UUID reported) {
        Optional<Report> reportFind = reports.stream().filter(report ->

                report.getReporter().equals(reporter)
                        && report.getReported().equals(reported)).findFirst();

        return reportFind.orElse(null);
    }
}
