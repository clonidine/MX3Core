package dev.migx3.core.api.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity("users")
public class User implements Serializable {

    @Id
    private String id;
    private String name;
    private Rank rank;
    private String ip;
    private List<Ban> bans = new ArrayList<>();

    public User() {
    }

    public User(String id, String name, Rank rank, String ip) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
