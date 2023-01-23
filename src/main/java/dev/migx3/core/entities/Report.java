package dev.migx3.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Report {

    private UUID reporter;
    private UUID reported;
    private String reason;
    private LocalDate moment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(reporter, report.reporter) && Objects.equals(reported, report.reported);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reporter, reported);
    }
}
