package org.example.demo.preservingstate.data;

import java.io.Serializable;
import java.util.Objects;

public final class PersonName implements Serializable {

    public static final int MAX_LENGTH = 150;
    private final String name;

    public PersonName(String name) {
        this.name = validate(name);
    }

    public String value() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static String validate(String name) {
        Objects.requireNonNull(name, "Name must not be null");
        var stripped = name.strip();
        if (stripped.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Name is too long");
        }
        if (stripped.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        // TODO Check for legal (or illegal) characters
        return stripped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonName that = (PersonName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
