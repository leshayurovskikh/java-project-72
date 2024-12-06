package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
public class Url {
    private long id;
    private String name;
    private Timestamp createdAt;

    public Url(long id, String address, Timestamp createdAt) {
        this.id = id;
        this.name = address;
        this.createdAt = createdAt;
    }
}
