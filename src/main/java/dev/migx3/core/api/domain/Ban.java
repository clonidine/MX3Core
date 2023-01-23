package dev.migx3.core.api.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Data
@Entity("bans")
public class Ban implements Serializable {

    @Id
    private String id;
    private String reason;
    private Date date;
    private boolean valid;

    public Ban() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ban ban = (Ban) o;
        return Objects.equals(id, ban.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
