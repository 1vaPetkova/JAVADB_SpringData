package softuni.exam.models.entities.enums;

import java.util.Arrays;

public enum Rating {
    GOOD, BAD, UNKNOWN;

    public boolean contains(String rating) {
        return Arrays.stream(Rating.values()).anyMatch(r -> r.name().equals(rating));
    }
}
