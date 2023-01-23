package dev.migx3.core.api.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity("ranks")
public class Rank {

    @Id
    private String name;
    private String displayName;
    private String color;
    private boolean staff;
    private List<String> permissions = new ArrayList<>();

    public Rank() {}

    public Rank(String name, String displayName, String color, boolean staff) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return Objects.equals(name, rank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}