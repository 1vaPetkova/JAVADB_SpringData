package com.example.java_db_09_exercise.util.validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <E> Boolean isValid(E entity);
    <E> Set<ConstraintViolation<E>> getViolations(E entity);
}
